package etu.demo.controller;

import etu.demo.domain.Rank;
import etu.demo.repository.JoueurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/rank")
public class RankController {
    @Autowired
    private JoueurRepository joueurRepository;
    @GetMapping("/get")
    public String getRank(Model model)
    {
        model.addAttribute("ranks" , joueurRepository.getRank());
        return "rank";
    }

}
