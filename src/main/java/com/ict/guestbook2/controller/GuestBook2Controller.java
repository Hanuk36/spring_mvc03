package com.ict.guestbook2.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ict.guestbook2.dao.VO;
import com.ict.guestbook2.service.GuestBook2Service;

@Controller
public class GuestBook2Controller {
	@Autowired
	private GuestBook2Service guestBook2Service;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("gb2_list.do")
	public ModelAndView getGuestBook2List() {
		ModelAndView mv = new ModelAndView("guestbook2/list");
		List<VO> list = guestBook2Service.getGuestBook2List();
		if(list != null) {
			mv.addObject("list", list);
			return mv;
		}
		return new ModelAndView("error");
	}
	
	@GetMapping("gb2_write.do")
	public ModelAndView getGuestBook2Write() {
		return new ModelAndView("guestbook2/write");
	}
	
	// 이미지 올리면 에러남~~~~~~~~~~~~~
	// 이미지 올리면 에러남~~~~~~~~~~~~~
	// 이미지 올리면 에러남~~~~~~~~~~~~~
	@PostMapping("gb2_write_ok.do")
	public ModelAndView getGuestBook2WriteOK(VO vo, HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("redirect:gb2_list.do");
			String path = request.getSession().getServletContext().getRealPath("/resources/upload");
			
			// 넘어온 파일의 정보 중 파일의 이름은 f_name에 넣어줘야 DB에 저장 할 수있다.
			MultipartFile file = vo.getFile();
			if(file.isEmpty()) {
				vo.setF_name("");
			}else {
				// 파라미터로 받은 file을 이용해서 DB에 저장할 f_name을 채워주자
				// 그러나 같은이름의 파일이 있으면 업로드가 안되므로 
				// 파일이름을 UUID를 이용해서 변경 후 DB에 저장 하자.
				UUID uuid = UUID.randomUUID();
				String f_name = uuid.toString()+"_"+file.getOriginalFilename();
				vo.setF_name(f_name);
				
				// 이미지 저장
				byte[] in = vo.getFile().getBytes();
				File out = new File(path, f_name);
				FileCopyUtils.copy(in, out);
			}
			
			// 패스워드 암호화
			String pwd = passwordEncoder.encode(vo.getPwd());
			vo.setPwd(pwd);
			
			// DB 저장
			int result = guestBook2Service.getGuestBook2Insert(vo);
			if(result > 0) {
				return mv;
			}
			return new ModelAndView("guestbook2/error");
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ModelAndView("guestbook2/error");
	} 
	
	@GetMapping("gb2_detail.do")
	public ModelAndView getGuestBook2Detail(String idx) {
		ModelAndView mv = new ModelAndView("guestbook2/onelist");
		VO vo = guestBook2Service.getGuestBook2Detail(idx);
		if(vo != null) {
			mv.addObject("vo", vo);
			return mv;
		}
		return new ModelAndView("guestbook2/error");
	}
	
	@GetMapping("guestbook2_down.do")
	public void getGuestBook2Down(HttpServletRequest request, HttpServletResponse response) {
		try {
			String f_name = request.getParameter("f_name");
			String path = request.getSession().getServletContext().getRealPath("/resources/upload/"+f_name);
			String r_path = URLEncoder.encode(path, "UTF-8");
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename="+r_path);
			
			File file = new File(new String(path.getBytes(), "UTF-8"));
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			FileCopyUtils.copy(in, out);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@PostMapping("gb2_delete.do")
	public ModelAndView getGuestBook2Delete(@ModelAttribute("vo2")VO vo) {
		return new ModelAndView("guestbook2/delete");
	}
	
	@PostMapping("gb2_delete_ok.do")
	public ModelAndView getGuestBook2DeleteOK(VO vo) {
		ModelAndView mv = new ModelAndView();
		
		String cpwd = vo.getPwd();
		
		VO vo2 = guestBook2Service.getGuestBook2Detail(vo.getIdx());
				
		String dpwd = vo2.getPwd();
		
		if(! passwordEncoder.matches(cpwd, dpwd)) {
			mv.setViewName("guestbook2/delete");
			mv.addObject("pwdchk", "fail");
			mv.addObject("vo2", vo);
			return mv;
		}else {
			int result = guestBook2Service.getGuestBook2Delete(vo.getIdx());
			if(result > 0) {
				mv.setViewName("redirect:gb2_list.do");
				return mv;
			}
		}
		return new  ModelAndView("guestbook2/error");
	}
	
	@PostMapping("gb2_update.do")
	public ModelAndView getGuestBook2Update(String idx) {
		ModelAndView mv = new ModelAndView("guestbook2/update");
		VO vo = guestBook2Service.getGuestBook2Detail(idx);
		if(vo != null) {
			mv.addObject("vo",vo);
			return mv;
		}
		return new ModelAndView("guestbook2/error");
	}
	
	@PostMapping("gb2_update_ok.do")
	public ModelAndView getGuestBook2UpdateOK(VO vo, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		
		String cpwd = vo.getPwd();
		
		VO vo2 = guestBook2Service.getGuestBook2Detail(vo.getIdx());
		String dpwd = vo2.getPwd();
		
		if(! passwordEncoder.matches(cpwd, dpwd)) {
			mv.setViewName("guestbook2/update");
			mv.addObject("pwdchk", "fail");
			mv.addObject("vo", vo2);
			return mv;
		}else {
			try {
				String path = request.getSession().getServletContext().getRealPath("/resources/upload");
				MultipartFile file = vo.getFile();
				String old_f_name = vo.getOld_f_name();
				if(file.isEmpty()) {
					vo.setF_name(old_f_name);
				}else {
					UUID uuid = UUID.randomUUID();
					String f_name = uuid.toString()+"_"+file.getOriginalFilename();
					vo.setF_name(f_name);
					
					byte[] in = file.getBytes();
					File out = new File(path, f_name);
					FileCopyUtils.copy(in, out);			
				}
				int result = guestBook2Service.getGuestBook2Update(vo);
				if(result > 0) {
					mv.setViewName("redirect:gb2_detail.do?idx="+vo.getIdx());
					return mv;
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return new ModelAndView("guestbook2/error");
	}
}