package com.leyunone.codex.model.vo;

import com.leyunone.codex.dao.entry.ProjectUser;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO {

    private String userName;

    private String userEmail;

    private String userRealName;

    private Integer codeAdditions;

    private Integer codeDeletions;

    private Integer codeTotal;

    private List<ProjectUser> projectList;
}
