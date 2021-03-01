package etu.demo.controller;


import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@org.springframework.stereotype.Controller
public class Controller {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

}
