package etu.demo.controller;

import etu.demo.domain.*;
import etu.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private PhraseRepository phraseRepository;

    @Autowired
    private MotAmbiguRepository motAmbiguRepository;
    @Autowired
    private PointsRepository pointsRepository;
    @Autowired
    private JoueurRepository joueurRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private ExpertRepository expertRepository;
    @Autowired
    private IntermidaireRepository intermidaireRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/")
    public String getAdmin()
    {
        return "admin";
    }
    @GetMapping("/getmakeAdmin")
    public String getMakeAdmin(Model model)
    {
        model.addAttribute("user" ,new Utilisateur());
        return "makeAdmin";
    }
    @PostMapping("/makeAdmin")
    public String makeAdmin(@ModelAttribute("user") Utilisateur user)
    {
      Utilisateur utilisateur = utilisateurRepository.findByPseudo(user.getPseudo()).get();
      Set<Role> roles = utilisateur.getRoles();
      roles.add(roleRepository.findByName(ERoles.ROLE_ADMIN));
      utilisateurRepository.save(utilisateur);
        return "success";
    }
    @GetMapping("/getdelUser")
    public String getDeleteUser(Model model)
    {
        model.addAttribute("deluser" ,new Utilisateur());
        return "delUser";
    }
    @DeleteMapping("/deleteuser")
    @Transactional
    public String deleteUser(@ModelAttribute("deluser") Utilisateur user)
    {
        Utilisateur utilisateur = utilisateurRepository.findByPseudo(user.getPseudo()).get();
        Expert expert = expertRepository.getById(utilisateur.getId());
        phraseRepository.deleteAllByExpert(expert);
        utilisateurRepository.deleteById(utilisateur.getId());


        return "success";
    }
    @GetMapping("/getdelPhrase")
    public String getDeletePhrase(Model model)
    {
        model.addAttribute("delphrase" ,new Phrase());
        return "delPhrase";
    }
    @DeleteMapping("/deletephrase")
    @Transactional
    public String deletePhrase(@ModelAttribute("delphrase") Phrase phrase)
    {

        phraseRepository.deleteById(phrase.getId());

        return "success";
    }

    @GetMapping("/getdelGlose")
    public String getDeleteGlose(Model model)
    {
        model.addAttribute("delglose" ,new Glose());
        return "delGlose";
    }
    @PostMapping("/deleteglose")
    public String deleteGlose(@ModelAttribute("delglose") Glose glose)
    {

       MotAmbigu motAmbigu = getMotsDePhrase(glose);
      if (!motAmbigu.getMot().equals("")) {
          motAmbiguRepository.save(motAmbigu);
          return "success";
      }
      else
          return "failed";
    }
    @GetMapping("/getphrases")
    public String getPhrases(Model model)
    {
        model.addAttribute("allPhrases" , phraseRepository.findPhras());
        return "phrases";
    }
    @GetMapping("/getusers")
    public String getUsers(Model model)
    {
        model.addAttribute("allusers" ,utilisateurRepository.findAll());
        return "users";
    }
    private MotAmbigu getMotsDePhrase(Glose glose)
    {
        List<Questions> mots = phraseRepository.findMot(glose.id);
        System.out.println("size listakam wordakan "+ mots.size());
        for (Questions q : mots)
        {
            ChoixReponse choixReponse = new ChoixReponse();
            choixReponse.setId(q.getPhrase_id());
            choixReponse.setPhrase(q.getPhrase());
            for (MotAmbigu m: q.getMot())
            {
                System.out.println("deta " + m.getMot() + "bro " +glose.getMot());
                if (m.getMot().trim().equals(glose.getMot().trim()))
                {
                    System.out.println("deta erukana");
                    if(m.getChoix1().equals(glose.getGlose()))
                        m.setChoix1("");
                    else if(m.getChoix2().equals(glose.getGlose()))
                        m.setChoix2("");
                    else if(m.getChoix3().equals(glose.getGlose()))
                        m.setChoix3("");
                    else if(m.getChoix4().equals(glose.getGlose()))
                        m.setChoix4("");
                    return m;
                }
            }


        }
       return new MotAmbigu();
    }
}
