package com.community.model;

import lombok.Data;
import org.apache.ibatis.annotations.Insert;

@Data
public class Question {
    private Insert id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
}
