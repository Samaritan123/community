package com.community.controller;

import com.community.dto.CommentDTO;
import com.community.dto.QuestionDTO;
import com.community.enums.CommentTypeEnum;
import com.community.service.CommentService;
import com.community.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id")Long id,
                           Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);

        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentDTOS);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
