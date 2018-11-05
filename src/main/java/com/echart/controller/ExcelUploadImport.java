package com.echart.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.echart.po.Stu;
import com.echart.service.StuService;

@Controller
public class ExcelUploadImport {

	@Autowired
	StuService service;
	
	@RequestMapping("upload")
	public String upload(@RequestParam("file")MultipartFile part,HttpServletRequest request, Model model) {
		//1、获取服务器端的路径
		String path = request.getServletContext().getRealPath("upload");
		//2、把path路径转换成File
		File file1 = new File(path);
		//3、判断上传目录是否存在
		if(!file1.exists()) {
			file1.mkdir();
		}
		//4、获取上传的文件名字
		String filename = part.getOriginalFilename();
		//5、制定上传文件存储名称和目录
		File file = new File(path+"\\"+filename);
		//6、把上传文件对象的文件内容转存到制定上传目录
		try {
			part.transferTo(file);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			//7、读取excel遍历数据
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet(workbook.getSheetName(0));
			int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
			for(int i=0;i<physicalNumberOfRows;i++) {
				XSSFRow row = sheet.getRow(i);
				int physicalNumberOfCells = row.getPhysicalNumberOfCells();
				StringBuffer sb=new StringBuffer();
				for(int j=0;j<physicalNumberOfCells;j++) {
					XSSFCell cell = row.getCell(j);
					if(cell.getCellTypeEnum()==CellType.STRING) {
						sb.append(cell.getStringCellValue()+",");
					}else {
						DecimalFormat df = new DecimalFormat("####");
						sb.append(df.format(cell.getNumericCellValue())+",");
					}
				}
				String[] split = sb.toString().split(",");
				Stu stu = new Stu();
				stu.setName(split[1]);
				stu.setScore(Integer.valueOf(split[2]));
				stu.setPhone(split[3]);
				service.save(stu);
			}
			workbook.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("filename",filename);
		System.out.println("success");
	return "success.jsp";
	}
	
}
