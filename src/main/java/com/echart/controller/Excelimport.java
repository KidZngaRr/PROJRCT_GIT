package com.echart.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.echart.po.Mobile;
import com.echart.service.StuService;

public class Excelimport {

	public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-dao.xml",
				"spring-service.xml");

		StuService service = context.getBean(StuService.class);

		Workbook worbook = WorkbookFactory.create(new File("C:\\Users\\ibm\\Desktop\\图片\\Mobile.xls"));
		int numberOfSheets = worbook.getNumberOfSheets();
		List<Mobile> list=new ArrayList<>();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = worbook.getSheetAt(i);
			int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
			for (int j = 0; j < physicalNumberOfRows; j++) {
				Row row = sheet.getRow(j);
				int physicalNumberOfCells = row.getPhysicalNumberOfCells();
				StringBuffer sb = new StringBuffer();
				for (int k = 0; k < physicalNumberOfCells; k++) {
					Cell cell = row.getCell(k);
					if (cell.getCellTypeEnum() == CellType.STRING) {
						sb.append(cell.getStringCellValue() + ",");
					} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
						sb.append(cell.getNumericCellValue() + ",");
					}
				}
				String[] split = sb.toString().split(",");
				Mobile mobile = new Mobile();
				mobile.setNumber(split[1]);
				mobile.setArea(split[2]);
				mobile.setMtype(split[3]);
				mobile.setAcode(split[4]);
				mobile.setPcode(split[5]);
				list.add(mobile);
			}
		}
		try {
			exec(list,service);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		worbook.close();
	}

	public static void exec(List<Mobile> list,StuService service) throws InterruptedException {
		int count = 5000; // 一个线程处理1000条数据
		int listSize = list.size(); // 数据集合大小
		int runSize = (listSize / count) + 1; // 开启的线程数
		List<Mobile> newlist = null; // 存放每个线程的执行数据
		ExecutorService executor = Executors.newFixedThreadPool(runSize); // 创建一个线程池，数量和开启线程的数量一样
		// 创建两个个计数器
		CountDownLatch begin = new CountDownLatch(1);
		CountDownLatch end = new CountDownLatch(runSize);
		// 循环创建线程
		for (int i = 0; i < runSize; i++) {
			// 计算每个线程执行的数据
			if ((i + 1) == runSize) {
				int startIndex = (i * count);
				int endIndex = list.size();
				newlist = list.subList(startIndex, endIndex);
			} else {
				int startIndex = (i * count);
				int endIndex = (i + 1) * count;
				newlist = list.subList(startIndex, endIndex);
				
			}
			service.save(newlist);
		}

		begin.countDown();
		end.await();

		executor.shutdown();
	}

}
