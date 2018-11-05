package com.echart.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.echart.po.Mobile;
import com.echart.po.Stu;

public interface StuDao {

	//读取全部学生信息
	public List<Stu> getAllStu();
	
	public void save(Stu stu);
	
	public void save1(@Param("list")List<Mobile> list);
}
