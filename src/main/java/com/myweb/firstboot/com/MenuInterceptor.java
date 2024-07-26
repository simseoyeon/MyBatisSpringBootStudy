package com.myweb.firstboot.com;

import com.myweb.firstboot.dto.BoardDto;
import com.myweb.firstboot.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class MenuInterceptor implements HandlerInterceptor {
    @Autowired
    private BoardService service;

    public void preHandle() throws Exception{
        //컨트롤러 메서드 호출 직전 실행
    }

    //뷰 렌더링 직전 실행
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object Handler, Model model) throws Exception{
        List<BoardDto> menu = service.getBoardMenu();
        model.addAttribute("menu", menu);
    }

    public void afterCompletion() throws Exception{
        //뷰 렌더링 후 실행
    }

}
