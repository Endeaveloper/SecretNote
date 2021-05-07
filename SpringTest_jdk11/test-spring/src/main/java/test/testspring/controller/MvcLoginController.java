package test.testspring.controller;

import org.thymeleaf.spring5.expression.Mvc;
import test.testspring.MvcLoginDAO.MvcMemberDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/mvcLogin.do")
public class MvcLoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MvcLoginController() {
        super();
    }

    // get 방식으로 호출할 경우
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 사용자가 요청한 URL
        String url = request.getRequestURL().toString();

        // getRequestURL StringBuffer 로 되어있어서 toString으로 변환해 주어야 한다.
        // 스트링.indexOf("검색어") 검색어를 찾은 위치값, 없으면 -1 리턴
        if(url.indexOf("mvcLogin.do") != -1) {

            // 폼에서 입력한 값
            String id = request.getParameter("id");
            String pass = request.getParameter("pass");
            System.out.println(id + "," + pass);

            MvcMemberDAO dao = new MvcMemberDAO();
            String name = dao.loginCheck(id, pass);
            System.out.println("이름 : " + name);

            // 로그인 여부
            String message = new String();
            String page = new String();

            if(name != null) {
                message = name + "님 환영합니다.";
                page = "/ch02/login_success.jsp";

                // session 객체 인스턴스
                HttpSession session = request.getSession();
                session.setAttribute("userid", id);
                session.setAttribute("message", message);
            } else {
                message = "아이디 또는 비밀번호가 일치하지 않습니다.";

                // 로그인 페이지로 돌아감
                page = "/ch02/login.jsp?message="+ URLEncoder.encode(message, "utf-8");
            }

            response.sendRedirect(request.getContextPath() + page);

        } else if (url.indexOf("logout.do") != -1) {
            // session 객체 만들기
            HttpSession session = request.getSession();

            // session을 초기화
            session.invalidate();

            //로그인 페이지로 되돌아감
            String message = "로그아웃되었습니다.";
            response.sendRedirect(request.getContextPath() + "/ch02/login.jsp?message="+URLEncoder.encode(message, "utf-8"));
        }
    }

    // post 방식으로 호출할 경우
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
