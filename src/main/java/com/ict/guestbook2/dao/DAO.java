package com.ict.guestbook2.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ict.guestbook2.dao.VO;

@Repository
public class DAO {
	private static final Logger logger = LoggerFactory.getLogger(DAO.class);
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public List<VO> getGuestBook2List(){
		try {
			List<VO> vo = null;
			vo = sqlSessionTemplate.selectList("guestbook2.list");
			return vo;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public int getGuestBook2Insert(VO vo) {
		try {
			return sqlSessionTemplate.insert("guestbook2.insert", vo);
		} catch (Exception e) {
			System.out.println(e);
		}
		return -1;
	}
	
	public VO getGuestBook2Detail(String idx) {
		try {
			return sqlSessionTemplate.selectOne("guestbook2.detail", idx);
		}catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public int getGuestBook2Delete(String idx) {
		try {
			return sqlSessionTemplate.delete("guestbook2.delete", idx);
		} catch (Exception e) {
			System.out.println(e);
		}
		return -1;
	}
	
	public int getGuestBook2Update(VO vo) {
		try {
			return sqlSessionTemplate.update("guestbook2.update", vo);
		}catch (Exception e) {
			System.out.println(e);
		}
		return -1;
	}
	
	
	
	
	
	
	
	
	

}
