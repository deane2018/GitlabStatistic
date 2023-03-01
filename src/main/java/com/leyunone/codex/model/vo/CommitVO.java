package com.leyunone.codex.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommitVO {

    private String committerName;

    private Integer total;

    private LocalDateTime commitDate;

    /**
     * 日期
     */
    private String date;
}
