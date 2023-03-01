package com.leyunone.codex.model.bo;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBO {

    private String userName;

    private String userEmail;

    private Integer codeAdditions;

    private Integer codeDeletions;

    private Integer codeTotal;
}
