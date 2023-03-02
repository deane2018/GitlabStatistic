package com.leyunone.codex.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupUserVO {

    private String groupName;

    private Integer codeTotal;

    private LocalDateTime commitDate;

    private Integer total;

    private String date;

    private String userName;

    private Integer groupId;

    private Integer id;
}
