package com.test.firstspring.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	// 뷰 페이지 이동 처리용 ------------------------------------------------------------------
	@RequestMapping("moveCrypto.do")
	public String moveCryptoPage() {
		return "test/testCrypto";	
	}

	// 패스워드 암호화 테스트
	@RequestMapping(value="bcrypto.do", method=RequestMethod.POST)
	public String testBCryptMethod(@RequestParam("userpwd") String userpwd) {
		
		logger.info("전송 온 암호 : " + userpwd);
		
		//암호화 처리
		String bcryptPwd = bcryptPasswordEncoder.encode(userpwd);
		logger.info("암호화 된 패스워드 : " + bcryptPwd + ", 글자수 : "+ bcryptPwd.length());
		
		return "common/main";
	}
	
	
	
	
	
}
