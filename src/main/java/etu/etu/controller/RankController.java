package etu.etu.controller;

import etu.etu.repository.JoueurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
