package com.project.eat.Register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

//HandlerInterceptor를 상속받는 클래스를 구현 : 컴포넌트로 등록
//WebMvcConfigurer를 상속받는 클래스에서 DI해서 사용
@Slf4j
@Component
public class RegisterInterceptor implements HandlerInterceptor {

    @Autowired
    HttpSession session;

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//        String url = request.getRequestURI();
//        log.info("===============================================");
//        log.info("==================== preHandle ====================");
//        log.info("request url : {}", url);
//
//        String user_id = (String)session.getAttribute("user_id");
//        log.info("session - user_id : {}", user_id);
//
//        if(user_id == null) { //user_id 값이 있다고 하면 출력이 안된다.
//
//            if(url.equals("/member/selectAll") || url.equals("/member/selectOne") //여기에 아이디 없이 selectAll에 접근할려고 해서 오류가 나왔다.
//                    || url.equals("/member/searchList")) {						  //오류가 나와서 여기서에선 접근이 안되기 떄문에 selectAll 은 없애는 게 좋다
//                response.sendRedirect("login");
//                return false;
//            }
//        }
//
//        return true;
//    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.info("==================== postHandle ======================");
        log.info("===============================================");
    }
}
