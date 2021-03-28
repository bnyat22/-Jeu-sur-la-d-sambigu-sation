package etu.demo.controller;


import etu.demo.domain.Expert;
import etu.demo.domain.Joueur;
import etu.demo.domain.MotAmbigu;
import etu.demo.domain.Phrase;
import etu.demo.repository.JoueurRepository;
import etu.demo.repository.MotAmbiguRepository;
import etu.demo.repository.PhraseRepository;
import etu.demo.repository.UtilisateurRepository;
import etu.demo.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String index() {
        return "index";
    }

    @PostMapping("/test")
    public void test()
    {
        MotAmbigu mot = new MotAmbigu("mot1" , "option2" , "option3");
        MotAmbigu mot2 = new MotAmbigu("mot2" , "option3" , "option6");

        List<MotAmbigu> motAmbigus = new ArrayList<>();
        motAmbigus.add(mot);
        motAmbigus.add(mot2);
       // Expert expert = joueurRepository.getById(1);
       // Phrase phrase = new Phrase("phrase3"  , expert);
       // phrase.setMot_id(motAmbigus);

        motAmbiguRepository.saveAll(motAmbigus);
      //  phraseRepository.save(phrase);

    }

    @GetMapping("/getall")
    public List<Phrase>  get()
    {
        return phraseRepository.findAll();
    }


}
