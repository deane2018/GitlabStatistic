package com.leyunone.codex.model.query;

import lombok.Data;

@Data
public class CodeTimeQuery {

    private String startDate;

    private String endDate;

    private Integer groupId;

    private Integer projectId;
}
