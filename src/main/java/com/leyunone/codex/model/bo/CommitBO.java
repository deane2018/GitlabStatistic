package com.leyunone.codex.model.bo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommitBO {

    private Date commitDate;

    private String committerEmail;

    private String committerName;

    private String title;

    private String id;

    private String projectId;

    private String message;

    /**
     * 新增
     */
    private Integer additions;

    /**
     * 刪除
     */
    private Integer deletions;

    /**
     * 总数
     */
    private Integer total;

    private String storageUrl;
}
