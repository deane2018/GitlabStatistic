package com.leyunone.codex.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommitQuery {

    private Integer index;

    private Integer size;

    private String committerName;

    private Integer projectId;
}
