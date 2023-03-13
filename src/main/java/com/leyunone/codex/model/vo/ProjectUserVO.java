package com.leyunone.codex.model.vo;

import lombok.Data;

@Data
public class ProjectUserVO {
    /**
     * 人员名
     */
    private String userName;
    /**
     * 人员真实姓名
     */
    private String userRealName;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 项目名
     */
    private Integer projectId;
}
