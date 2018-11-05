package com.echart.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.echart.po.Stu;
import com.echart.service.StuService;
import com.mysql.jdbc.ResultSetRow;

@Controller
public class ExcelDownloadExport {
	@Autowired
	StuService service;
	
	@RequestMapping("/download")
	public void download(HttpServletRequest request,HttpServletResponse response){
		List<Stu> list = service.getAllStu();
		String[] filetitle= {"编号","姓名","成绩","手机号"};
		
		Random random = new Random();
		int nextInt = random.nextInt(1000);
		String filename="学生成绩表"+nextInt;
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("成绩表");
		XSSFRow row0 = sheet.createRow(0);
		for(int m=0;m<4;m++) {
			row0.createCell(m).setCellValue(filetitle[m]);
		}
		int i=1;
		for (Stu stu : list) {
			XSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(stu.getId());
			row.createCell(1).setCellValue(stu.getName());
			row.createCell(2).setCellValue(stu.getScore());
			row.createCell(3).setCellValue(stu.getPhone());
			i++;
		}
		try {
			filename=new String(filename.getBytes("gbk"),"iso-8859-1");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename="+filename+".xls");
			response.setBufferSize(1024);
			ServletOutputStream os = response.getOutputStream();
			workbook.write(os);
			workbook.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
}
}