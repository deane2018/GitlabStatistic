package com.leyunone.codex.config;

import org.gitlab4j.api.GitLabApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 乐云一
 */
@Configuration
public class GitLabConfig {

    @Value("${gitlab.token:test}")
    private String privateToken;
    @Value("${gitlab.url:test}")
    private String url;

    @Bean
    public GitLabApi gitlabAPI(){
        return new GitLabApi(url,privateToken);
    }
}
