package com.leyunone.codex.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ViewControl {

    @GetMapping("/")
    public String htmlView(){
        return "leyunoneCodeX";
    }
}
