package com.roncoo.eshop.datasync.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.roncoo.eshop.datasync.service.EshopProductService;

@RestController
public class DataSyncController {

	@Autowired
	private EshopProductService eshopProductService;
	
	@RequestMapping("/findBrandById")
	@ResponseBody
	public String findBrandById(Long id) {
		return eshopProductService.findBrandById(id);
	}
	
}
