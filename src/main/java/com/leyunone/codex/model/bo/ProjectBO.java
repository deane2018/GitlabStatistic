package com.leyunone.codex.model.bo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectBO {

    private String projectId;

    private String projectName;

    private String path;

    private Date createDate;
}
