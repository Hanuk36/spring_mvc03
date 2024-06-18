package com.ict.guestbook.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ict.guestbook2.dao.DAO;

@Repository
public class GuestBookDAO {
	private static final Logger logger = LoggerFactory.getLogger(DAO.class);
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public List<GuestBookVO> getGuestList(){
		try {
			return sqlSessionTemplate.selectList("guestbook.list");
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public int getGuestInsert(GuestBookVO gvo) {
		try {
			return sqlSessionTemplate.insert("guestbook.insert", gvo);
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}
	
	public GuestBookVO getGuestDetail(String idx) {
		try {
			return sqlSessionTemplate.selectOne("guestbook.detail", idx);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public int getGuestDelete(String idx) {
		try {
			return sqlSessionTemplate.delete("guestbook.delete",idx);
		} catch (Exception e) {
			System.out.println(e);
		}
		return -1;
	}
	
	public int getGuestUpdate(GuestBookVO gvo) {
		try {
			return sqlSessionTemplate.update("guestbook.update", gvo);
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
