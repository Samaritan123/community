package com.community.controller;

import com.community.dto.PaginationDTO;
import com.community.dto.QuestionDTO;
import com.community.mapper.QuestionMapper;
import com.community.mapper.UserMapper;
import com.community.model.Question;
import com.community.model.User;
import com.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserMapper userMapper;

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name="page", defaultValue = "1") Integer page,
                        @RequestParam(name="size", defaultValue = "5") Integer size) {
        PaginationDTO pagination = questionService.list(page, size);
//        System.out.println(pagination.getQuestions());
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
