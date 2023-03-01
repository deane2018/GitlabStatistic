package com.leyunone.codex.model.bo;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchesBO {

    private Integer projectId;

    private String branchName;
}
