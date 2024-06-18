package com.ict.guestbook2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ict.guestbook2.dao.VO;

public interface GuestBook2Service {
	//전체보기
	List<VO> getGuestBook2List();
		
	//상세보기
	VO getGuestBook2Detail(String idx);
	
	//삽입
	int getGuestBook2Insert(VO vo);
	
	//수정
	int getGuestBook2Delete(String idx);
	
	//삭제
	int getGuestBook2Update(VO vo);
}
