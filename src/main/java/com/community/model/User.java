package com.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String cookie;
    private String token;
    private String avatarUrl;
    private Long gmtCreate;
    private Long gmtModified;
}
