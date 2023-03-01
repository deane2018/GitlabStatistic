package com.leyunone.codex.model.query;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CodeTimeQuery {

    private String startDate;

    private String endDate;

    private Integer groupId;

    private Integer projectId;
}
