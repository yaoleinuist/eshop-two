local cjson = require("cjson")
local http = require("resty.http")
local redis = require("resty.redis")  

local function close_redis(red)  
	if not red then  
			return  
	end  
	local pool_max_idle_time = 10000 
	local pool_size = 100 
	local ok, err = red:set_keepalive(pool_max_idle_time, pool_size)  
	if not ok then  
			ngx.say("set keepalive error : ", err)  
	end  
end 

local uri_args = ngx.req.get_uri_args()
local productId = uri_args["productId"]
--首先获取nginx的本地缓存，如果没有则降级到本机房的redis集群
local cache_ngx = ngx.shared.my_cache
local productCacheKey = "product_"..productId
local productCache = cache_ngx:get(productCacheKey)

if productCache == "" or productCache == nil then
  --首先获取本地机房的redis降级状态，如果在降级中，则降级到数据直连服务
  local slaveRedisDegrade = cache_ngx:get("slaveRedisDegrade")
    
  if slaveRedisDegrade == "true" then
        --获取数据直连服务状态
        local dataLinkDegrade = cache_ngx:get("dataLinkDegrade")
		
		if dataLinkDegrade == true then 
		      --获取数据直连降级状态，如果在降级中，则降级到主集群
			  local red = redis:new()  
			  red:set_timeout(1000)  
			  local ip = "192.168.31.223"  
			  local port = 1111  
			  local ok, err = red:connect(ip, port) 
			  local redisResp, redisErr = red:get("dim_product_"..productId)
			  productCache = redisResp
			  --主机群如果也挂掉，那么整个系统就存在问题，
			  local curTime = os.time()
			  local diffTime = os.difftime(curTime, cache_ngx:get("startdataLinkDegradeTime"))
				 --尝试去访问数据直连服务，恢复数据直连
				if diffTime > 60 then
				    local httpc = http.new()
					local resp, err = httpc:request_uri("http://192.168.31.179:8767",{
					  method = "GET",
					  path = "/product?productId="..productId
					})
					--如果访问成功，则将数据直连服务进行升级
				    if resp then
				      cache_ngx:set("dataLinkDegrade", "false")
				    end
				end
		else 
		    --如果数据直连服务不是在降级中，则直接访问数据直连服务
			local httpc = http.new()
			local resp, err = httpc:request_uri("http://192.168.31.179:8767",{
			  method = "GET",
			  path = "/product?productId="..productId
			})
			 --如果数据直连服务访问失败，则记录访问错误的次数+1，如果次数大于10那么，将数据直连服务降低
			if not resp then  
				ngx.say("request error :", err)  
				
				local dataLinkFailureCnt = cache_ngx:get("dataLinkFailureCnt")
				cache_ngx:set("dataLinkFailureCnt", dataLinkFailureCnt + 1)
				
				if dataLinkFailureCnt > 10 then 
				  cache_ngx:set("dataLinkDegrade", "true")
				  cache_ngx:set("startDataLinkDegradeTime", os.time())
				end
			end
			
			productCache = resp.body
			--如果数据直连服务正常，那么尝试连接本机房从集群，如果成功则将本机房的redis降级状态改成false
			local curTime = os.time()
			local diffTime = os.difftime(curTime, cache_ngx:get("startSlaveRedisDegradeTime"))
			
			if diffTime > 60 then
			  local red = redis:new()  
			  red:set_timeout(1000)  
			  local ip = "192.168.31.223"  
			  local port = 1112  
			  local ok, err = red:connect(ip, port)  
			  
			  if ok then 
				cache_ngx:set("slaveRedisDegrade", "false")
			  end
			end
		end
  else
      --如果本地机房正常，访问本地机房，如果访问出错，则将本地机房失败次数+1，如果大于十次则降低
	  local red = redis:new()  
	  red:set_timeout(1000)  
	  local ip = "192.168.31.223"  
	  local port = 1112  
	  local ok, err = red:connect(ip, port)  
	  
	  if not ok then  
		ngx.say("connect to redis error : ", err)  
		
		local slaveRedisFailureCnt = cache_ngx:get("slaveRedisFailureCnt")
		cache_ngx:set("slaveRedisFailureCnt", slaveRedisFailureCnt + 1)
		
		if slaveRedisFailureCnt > 10 then 
		  cache_ngx:set("slaveRedisDegrade", "true")
		  cache_ngx:set("startSlaveRedisDegradeTime", os.time())
		end
		
		return close_redis(red)  
	  end
      --获取产品的其他维度信息
	  local redisResp, redisErr = red:get("dim_product_"..productId)
	  
	  if redisResp == ngx.null or redisResp == "" or redisResp == nil then  
	    local dataLinkDegrade = cache_ngx:get("dataLinkDegrade")
		
		if dataLinkDegrade == "true" then
			  local red = redis:new()  
			  red:set_timeout(1000)  
			  local ip = "192.168.31.223"  
			  local port = 1111  
			  local ok, err = red:connect(ip, port) 
			  local redisResp, redisErr = red:get("dim_product_"..productId)
			  productCache = redisResp
			  
			  local curTime = os.time()
				local diffTime = os.difftime(curTime, cache_ngx:get("startdataLinkDegradeTime"))
				
				if diffTime > 60 then
				    local httpc = http.new()
					local resp, err = httpc:request_uri("http://192.168.31.179:8767",{
					  method = "GET",
					  path = "/product?productId="..productId
					})
					
				    if resp then
				      cache_ngx:set("dataLinkDegrade", "false")
				    end
				end
		else
			local httpc = http.new()
			local resp, err = httpc:request_uri("http://192.168.31.179:8767",{
			  method = "GET",
			  path = "/product?productId="..productId
			})
			
			if not resp then  
				ngx.say("request error :", err)  
				
				local dataLinkFailureCnt = cache_ngx:get("dataLinkFailureCnt")
				cache_ngx:set("dataLinkFailureCnt", dataLinkFailureCnt + 1)
				
				if dataLinkFailureCnt > 10 then 
				  cache_ngx:set("dataLinkDegrade", "true")
				  cache_ngx:set("startDataLinkDegradeTime", os.time())
				end
			end

			productCache = resp.body
		end
	  else
		productCache = redisResp
	  end
  end
  --将productCacheKey进行不同的过期时间
  math.randomseed(tostring(os.time()):reverse():sub(1, 7))
  local expireTime = math.random(600, 1200)  

  cache_ngx:set(productCacheKey, productCache, expireTime)
end
--将数据整合到模板中
local context = {
  productInfo = productCache,
}

local template = require("resty.template")
template.render("product.html", context)