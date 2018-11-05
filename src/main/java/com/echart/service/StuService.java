package com.echart.service;

import java.util.List;

import com.echart.po.Mobile;
import com.echart.po.Stu;

public interface StuService {
	//读取全部学生信息
		public List<Stu> getAllStu();
		public void save(Stu stu);
		public void save(List<Mobile> list);
}
