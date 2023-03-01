package com.leyunone.codex.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupUserVO {

    private String groupName;

    private Integer codeTotal;

    private Integer total;

    private LocalDateTime commitDate;

    private String date;
}
