package etu.demo.controller;


import etu.demo.domain.*;
import etu.demo.repository.JoueurRepository;
import etu.demo.repository.MotAmbiguRepository;
import etu.demo.repository.PhraseRepository;
import etu.demo.repository.UtilisateurRepository;
import etu.demo.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private MotAmbiguRepository motAmbiguRepository;

    @Autowired
    private PhraseRepository phraseRepository;
    @Autowired
    private JoueurRepository joueurRepository;
    @RequestMapping("/")
    public String index(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority admin:authentication.getAuthorities())
        {
            if (admin.getAuthority().equals("ADMIN"))
            {
                model.addAttribute("admin" , admin.getAuthority());
            }
        }
        return "index";
    }

    @GetMapping("/getRank")
    public List<Rank> getRanks(Model model)
    {

        return joueurRepository.getRank();
    }



    @GetMapping("/getall")
    public List<Phrase>  get()
    {
        return phraseRepository.findAll();
    }


}
