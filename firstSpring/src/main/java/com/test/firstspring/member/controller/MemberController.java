package com.test.firstspring.member.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.test.firstspring.member.model.service.MemberService;
import com.test.firstspring.member.model.vo.Member;
import com.test.firstspring.member.model.vo.SearchDate;

@Controller
public class MemberController { 
	// 로그 사용을 위한 것                                                 // 클래스이름.class  로 작성
	// 서비스 dao 클래스에도 똑같이 작성하고 뒤에 클래스 이름만 바꿔주면 됨 (클래스 자신의 이름으로)
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	//의존성 주입 (Dependency Injection)
	//사용할 클래스 레퍼런스 = new 생성자();
	
	@Autowired // 자동 DI 처리됨
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	// view 페이지 이동처리 용 메소드 ------------------------------------------------------------
	// 컨트롤러 안의 메소드는 public 여야함 (바깥에서 호출하니까)
	// 리턴타입은 다양하게 사용가능
	// void 는 ajax로 요청되는 컨트롤러 메소드에 사용
	// view 파일명을 리턴할때는 String 로 리턴함
	
	@RequestMapping ("loginPage.do")
	public String moveLoginPage() {// 페이지 이동 처리시 별도로 값을 바꿀 필요가 없으니 매개변수 없이 빈괄호로 작성
		return "member/loginPage";		
	}
	
	// 회원가입 페이지 보이게 함
	@RequestMapping("enrollPage.do")
	public String moveEnrollPage() {
		return "member/enrollPage";		
	}
	
		
	// ------------------------------------------------------------------------------------------------
	
//	@RequestMapping(value="login.do", method=RequestMethod.POST)
//	public String loginMethod(HttpServletRequest request, HttpServletResponse response) {
//		String userid = request.getParameter("userid");
//		String userpwd = request.getParameter("userpwd");
//		return "main";
//	}
	
//	@RequestMapping(value="login.do", method=RequestMethod.POST)
//	public String loginMethod(
//			@RequestParam("userid") String userid,
//			@RequestParam("userpwd") String userpwd
//			){
//		// 매개변수 수가 적을 때
//		System.out.println("login.do : "+userid+", "+userpwd);
//		Member member = new Member();
//		member.serUserid(userid);
//		member.setUserpwd(userpwd);
//		return "main";
//	}
	
	//command 객체 이용 
	@RequestMapping(value="login.do", method=RequestMethod.POST)
	public String loginMethod(Member member, HttpSession session, SessionStatus status, Model model){
		/*스프링의 특징
		 *  매개변수에 변수 선언하면 자동으로 객체 생성이 됨
		 *  자동 의존성 주입 (자동 DI가 됨)
		 */		
		// 매출을 목적으로 만든 완성코드에는 println 문구는 있으면 안됨 (테스트용으로 쓰는 거니까)
		//System.out.println("login.do : "+member);
		logger.info("login.do : " + member);  //println 대신 로거로 콘솔에 출력되게 함
		// src/mail/reaources 밑에 log4j.xml 이 로거 담당 파일
		
		// 패스워드 암호화 저리 전 코드
		//Member loginMember = memberService.selectLogin(member);
		
		// 패스워드 암호화 처리 후 코드
		// userid가 일치하는 회원정보 조회해 옴
		Member loginMember = memberService.selectMember(member.getUserid());
		
		// 위에서 조회해 온 회원정보의 암호화 된 패스워드와 클라이언트가 보낸 암호를 비교
		// matches(일반글자 암호 와 암호화 된 패스워드를 비교하고 일치하면 true, 아니면 false)
		
		
		if(loginMember != null && bcryptPasswordEncoder.matches(member.getUserpwd(),
							loginMember.getUserpwd()) && loginMember.getLogin_ok().equals("Y")) {
			// 성공시
			
			// 세션 객체 생성 > 세션 안에 회원 정보 저장
			session.setAttribute("loginMember", loginMember);
			status.setComplete();  // 로그인 요청이 성공 했다는 것 -> 성공에 대해 200이라는 코드를 클라이언트에게 전송 보냄
			
			return "common/main";			
		} else {
			// 실패시 메시지 출력
			model.addAttribute("message", "로그인 실패");
			
			return "common/error"; //view 파일 지정하면 자동으로 지정한 view에게 전달됨 (자동 포워딩 됨)	
		}
	}
	
	@RequestMapping("logout.do")
	public String logoutMethod(HttpServletRequest request, Model model) { 
		// 세션을 없애는 작업이니 일단 세션 객체가 필요한데, 로그아웃에선 세션 객체를 만들면 안되니까
		// 로그인 메소드처럼 매개변수에 변수 선언해서 자동 객체 생성되게 하면 안됨
		
		// 일단 세션이 있는지 확인 (로그인 상태인지 확인)
		HttpSession session = request.getSession(false);  // 새로 생성하면 안되니까 false 입력
		
		if(session != null) { //세션객체가 존재한다면 (로그인 되었다면)
			//세션을 없애라 (로그아웃해라)
			session.invalidate();
			return "common/main";			
		} else {
			model.addAttribute("message", "로그인 세션이 존재하지 않습니다.");
			return "common/error";
		}
	}
	
	// 아이디 중복체크 확인을 위한 ajax 요청 처리용 메소드
	// ajax 통신은 뷰 리졸버로 뷰 파일을 리턴하면 안됨 -> 뷰페이지가 바뀌니까(ajax 는 뷰가 바뀌면 안되고 요청한 페이지 그대로 가야함)
	// 그래서 void 사용
	// 요청한 클라이언트와 출력스트림을 만들어서 값을 전송함
	@RequestMapping(value="idchk.do", method=RequestMethod.POST)
	public void idCheckMethod(@RequestParam("userid") String userid, HttpServletResponse response) throws IOException {
		// @RequestParam("userid") String userid 이렇게 하면 리스트를 꺼내거나 get파라메터 안해도 됨
		// (아래 String userid= ..... 과 같은 것)
		//String userid = request.getParameter("userid");
		
		int idcount = memberService.selectCheckId(userid);
		
		String returnValue = null;
		if(idcount == 0) {
			returnValue = "ok";
		}else {
			returnValue = "dup";
		}
		
		//response 를 이용해서 클라이언트로 출력스트림 만들고 값 보내기
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.append(returnValue);
		out.flush();
		out.close();		
	}
	
	// 회원가입 되도록 처리용
	// 리턴타입은 void : ajax 통신일때 (반환값 없음), 뷰파일명 String 타입으로 반환, (한가지 더 있음)
	// 8월 20일 1교시 참고
	@RequestMapping(value="enroll.do", method=RequestMethod.POST)
	public String mamberInsert(Member member, Model model) {
		logger.info("enroll.do : " + member);
		
		// 패스워드 암호화 처리 -> 를 하고 서비스로 넘김
		member.setUserpwd(bcryptPasswordEncoder.encode(member.getUserpwd()));
		
		if(memberService.insertMember(member) > 0) {
			// 회원가입 성공시
			return "common/main";
		} else {
			model.addAttribute("message", "회원가입 실패!");
			return "common/error";
		}
	}
	
	// 마이페이지 처리
	@RequestMapping("myinfo.do")
	public ModelAndView myInfoMethod(@RequestParam("userid") String userid, ModelAndView mv) {
		
		Member member = memberService.selectMember(userid);
		
		if(member != null) {
			mv.addObject("member", member);
			mv.setViewName("member/myInfoPage");
		} else {
			mv.addObject("message", userid + "회원조회 실패!");
			mv.setViewName("common/error");
		}
		
		return mv;
	}
	@RequestMapping(value="mupdate.do", method=RequestMethod.POST)
	public String memberUpdateMethod(Member member, Model model, 
			@RequestParam("origin_userpwd") String originUserpwd) {
		logger.info("mupdate.do : " + member);
		logger.info("opwd : " + originUserpwd);
		
		// 새로운 암호가 전송이 왔다면
		String userpwd = member.getUserpwd().trim();
		if(userpwd != null && userpwd.length() > 0) {
			// 기존 암호와 다른 값이면 암호화 처리를 해라
			if( !bcryptPasswordEncoder.matches(userpwd, originUserpwd)) {
			member.setUserpwd(bcryptPasswordEncoder.encode(userpwd));
		} 
	} else { 
		// 새로운 암호값이 전송오지 않았다면 원래 암호를 기록
		member.setUserpwd(originUserpwd);
	}
		logger.info("after : " + member);
		
		
		if(memberService.updateMember(member) > 0) {
			// 컨트롤러의 메소드를 직접 호출할 수 있음
			return "redirect:myinfo.do?userid=" + member.getUserid();
		} else {
			model.addAttribute("message", member.getUserid() + "회원정보 수정 실패!");
			
			return "common/error";
		}
		
	}
	@RequestMapping("mdel.do")
	public String memberDeleteMethod(@RequestParam("userid") String userid, Model model) {
		if(memberService.deleteMember(userid) > 0) {
			return "redirect:logout.do";
		} else {
			model.addAttribute("message", userid + "회원 삭제 실패");
			return "common/error";
		}		
	}
	
	// ArrayList조회 회원관리 페이지니까 회원 목록이 리스트로 옴
	@RequestMapping("mlist.do")
	public String memberListViewMethod(Model model) {
		ArrayList<Member> list = memberService.selectList();
		
		if(list.size() > 0) {
			model.addAttribute("list", list);
			return "member/memberListView";
		} else {
			model.addAttribute("message", "회원 목록이 존재하지 않습니다.");
			return "common/error";
		}
		
	}
	
	// 회원관리에서 날짜로 검색시!! 시작과 끝 날짜를 당일로 하지말고 
	// 시작은 당일 - 1일/ 끝은 당일 +1일로 선택해야 그 안에 있는 당일 날짜와 관련된게 검색되어 보여짐
	@RequestMapping(value="msearch.do", method=RequestMethod.POST)
	public String memberSearchMethod(HttpServletRequest request, Model model) {
		String action = request.getParameter("action");
		String keyword = null, beginDate = null, endDate = null;
				
		if(action.equals("enrolldate")) {
			beginDate = request.getParameter("begin");
			endDate = request.getParameter("end");
		} else {
			keyword = request.getParameter("keyword");
		}
		// 서비스 메소드로 전송하고 결과 받을 리스트 준비
		ArrayList<Member> list = null;
		
		switch(action) {
		case "id": list = memberService.selectSearchUserid(keyword); break;
		case "gender": list = memberService.selectSearchGender(keyword); break;
		case "age": list = memberService.selectSearchAge(Integer.parseInt(keyword)); break;
		case "enrolldate": list= memberService.selectSearchEnrollDate(
							new SearchDate(Date.valueOf(beginDate), Date.valueOf(endDate))); break;
		case "loginok": list = memberService.selectSearchLoginOk(keyword);
		} 
		
		if(list.size() > 0) {
			model.addAttribute("list", list);
			return "member/memberListView";
		} else {
			model.addAttribute("message", action + " 검색에 대한 " + keyword + " 결과가 존재하지 않습니다. ");
			return "common/error";
		}
	}
	
	// 회원관리에서 로그인 제한 거는 메소드
	@RequestMapping("loginOK.do")
	public String changeLoginOKMethod(Member member, Model model) {
		logger.info("loginOK.do : " + member.getUserid() + ", " + member.getLogin_ok());
		
		if(memberService.updateLoginOk(member) > 0) {
			return "redirect:mlist.do";
		} else {
			model.addAttribute("message", "로그인 제한/허용 처리 오류");
			return "common/error";
		}
	}
	
}
