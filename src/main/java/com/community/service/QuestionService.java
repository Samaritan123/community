package com.community.service;

import com.community.dto.PaginationDTO;
import com.community.dto.QuestionDTO;
import com.community.exception.CustomizeErrorCode;
import com.community.exception.CustomizeException;
import com.community.mapper.QuestionExtMapper;
import com.community.mapper.QuestionMapper;
import com.community.model.Question;
import com.community.model.QuestionExample;
import com.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.community.mapper.UserMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Resource
    private QuestionExtMapper questionExtMapper;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        paginationDTO.setPagination(totalCount, page, size);

        page = paginationDTO.getPage();

        Integer offset = size * (page - 1);
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        //        System.out.println(questions);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question: questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

        paginationDTO.setPagination(totalCount, page, size);

        page = paginationDTO.getPage();

        Integer offset = size * (page - 1);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

//        System.out.println(questions);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question: questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return  questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        } else {
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated != 1) throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
    }

    public void incView(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(questionDTO.getTag(), ",");
        String regexTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexTag);
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(q, dto);
            return dto;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
