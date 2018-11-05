package com.echart.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.echart.dao.StuDao;
import com.echart.po.Mobile;
import com.echart.po.Stu;
import com.echart.service.StuService;
@Service
public class StuServiceImpl implements StuService {

	@Autowired
	StuDao dao;
	@Override
	public List<Stu> getAllStu() {
		return dao.getAllStu();
	}
	@Override
	public void save(Stu stu) {
		dao.save(stu);
		
	}
	@Override
	public void save(List<Mobile> list) {
		dao.save1(list);
		
	}

}
