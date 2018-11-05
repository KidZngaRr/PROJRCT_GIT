package com.echart.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.echart.po.Stu;
import com.echart.po.pie;
import com.echart.service.StuService;

@Controller
public class StuController {
	@Autowired
	StuService service;

	@RequestMapping("/getallstu")
	@ResponseBody
	public List<Stu> getAllStu(){
		
		return service.getAllStu();
		
	}
	
	@RequestMapping("/getallstupie")
	@ResponseBody
	public List<pie> getallStuPie(){
		List<pie> listpie=new ArrayList<pie>();
		List<Stu> liststu = service.getAllStu();
		for (Stu stu : liststu) {
			pie p = new pie();
			p.setName(stu.getName());
			p.setValue(stu.getScore());
			
			listpie.add(p);
		}
		
		return listpie;
	}
}
