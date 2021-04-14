package etu.etu.controller;


import etu.etu.domain.*;
import etu.etu.repository.*;
import etu.etu.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PhraseRepository phraseRepository;
    @Autowired
    private JoueurRepository joueurRepository;
    @Autowired
    private ExpertRepository expertRepository;
    @Autowired
    private IntermidaireRepository intermidaireRepository;

    @GetMapping("/")
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
    public List<Rank> getRanks()
    {

        return joueurRepository.getRank();
    }



    @GetMapping("/getall")
    public List<Phrase>  get()
    {
        return phraseRepository.findAll();
    }

    @GetMapping("/me")
    public String  getProfile(Model model)
    {
        Utilisateur user=  utilisateurRepository.findById(getUserId()).get();
        Joueur joueur = joueurRepository.getById(getUserId());
        Profile profile;
       if (intermidaireRepository.getById(getUserId()) == null)
       {
            profile = new Profile(user.getId() , user.getPseudo()
                   , user.getEmail(), joueur.getPoint() , joueur.getCredit());
       } else
       {
           Intermédiaire intermédiaire = intermidaireRepository.getById(getUserId());
           if (expertRepository.getById(getUserId()) == null)
           {

               profile = new Profile(user.getId() , user.getPseudo()
                       , user.getEmail(), joueur.getPoint() ,
                       joueur.getCredit() , intermédiaire.getNbGloses());
           } else
           {
               Expert expert = expertRepository.getById(getUserId());
               profile = new Profile(user.getId() , user.getPseudo()
                       , user.getEmail(), joueur.getPoint() ,
                       joueur.getCredit() , intermédiaire.getNbGloses() ,expert.getNbPhrases());
           }
       }
       model.addAttribute("profil" , profile);

        return "profile";
    }
    private long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String myUser = authentication.getName();
        System.out.println(myUser + "fhgfjhgdfsdghj");
        UserDetailsImpl userDetails;
        if (authentication instanceof UserDetails) {
            userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getId();
        } else {
            Optional<Utilisateur> utilisateur = utilisateurRepository.findByPseudo(myUser);
            return utilisateur.get().getId();

        }
    }

}
