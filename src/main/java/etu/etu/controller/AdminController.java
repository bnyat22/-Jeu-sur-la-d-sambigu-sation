package etu.etu.controller;

import etu.etu.domain.*;
import etu.etu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private ExpertRepository expertRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/")
    public String getAdmin()
    {
        return "admin";
    }


    @PostMapping("/makeadmin")
    public String makeAdmin(@ModelAttribute("user") Utilisateur user , Model model)
    {
      Utilisateur utilisateur = utilisateurRepository.findByPseudo(user.getPseudo()).get();
      Set<Role> roles = utilisateur.getRoles();
      roles.add(roleRepository.findByName(ERoles.ROLE_ADMIN));
      utilisateurRepository.save(utilisateur);
        return getUsers(model);
    }
    @PostMapping("/deleteuser")
    public String deleteUser(@ModelAttribute("user") Utilisateur user ,Model model)
    {
        deleteUserService(user);
        return getUsers(model);
    }

    @Transactional
    public void deleteUserService(Utilisateur user) {
        Utilisateur utilisateur = utilisateurRepository.findByPseudo(user.getPseudo()).get();
        Expert expert = expertRepository.getById(utilisateur.getId());
        phraseRepository.deleteAllByExpert(expert);
        utilisateurRepository.deleteById(utilisateur.getId());
    }

    @PostMapping("/deletephrase")
    @Transactional
    public String deletePhrase(@ModelAttribute("phrase") Phrase phrase , Model model)
    {
        deletePhraseService(phrase);
        return getPhrases(model);
    }

    @Transactional
    public void deletePhraseService(Phrase phrase) {
        phraseRepository.deleteById(phrase.getId());
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
        model.addAttribute("phrase" , new Phrase());
        model.addAttribute("allPhrases" , phraseRepository.findPhras());
        return "phrases";
    }
    @GetMapping("/getusers")
    public String getUsers(Model model)
    {
        model.addAttribute("user" , new Utilisateur());
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
