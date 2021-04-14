package etu.etu.controller;

import etu.etu.domain.*;
import etu.etu.repository.*;
import etu.etu.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ajoute")
public class AjouteController {


    @Autowired
    private ExpertRepository expertRepository;
    @Autowired
    private MotAmbiguRepository motAmbiguRepository;
    @Autowired
    private PhraseRepository phraseRepository;
    @Autowired
    private PointsRepository pointsRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/getAjoute")
    public String getPhraseAjouteClasse(Model model) {
         Question question;
         List<MotQuestion> mots;
        mots = new ArrayList<>();
        mots.add(new MotQuestion());
        question = new Question(mots);
        model.addAttribute("addPhrase", question);
        return "phrase";
    }

    @PostMapping(value = "/add", params = "action=la")
    public String ajouterNouveauMot(@ModelAttribute("addPhrase") Question question, Model model) {
        System.out.println(question.getMots().size() + "   size ");
        question.getMots().add(new MotQuestion());
        for (MotQuestion m : question.getMots()) {
            System.out.println(m.getMot() + "   mot ");
        }
//        this.question.setMots(question.getMots());
        model.addAttribute("addPhrase", question);
        return "phrase";
    }

    @PostMapping(value = "/add", params = "action=le")
    public String addPhrase(@ModelAttribute("addPhrase") Question question) {
        Expert expert = expertRepository.getById(getUserId());
         if (!checkCreditPhrase(expert))
            return "errorCredit";
        Phrase p = new Phrase(question.getPhrase(), expert);
        List<MotAmbigu> motAmbigus = new ArrayList<>();
        List<PointQuestion> points = new ArrayList<>();
        for (MotQuestion m : question.getMots()) {

            MotAmbigu mot = new MotAmbigu(m.getMot(), m.getChoix1(), m.getChoix2());
            PointQuestion pointQuestion = new PointQuestion();
            pointQuestion.setP1(m.getPoint1());
            pointQuestion.setP2(m.getPoint2());

            if (m.getChoix3() != null)
                mot.setChoix3(m.getChoix3());
            pointQuestion.setP3(m.getPoint3());

            if (m.getChoix4() != null)
                mot.setChoix4(m.getChoix4());
            pointQuestion.setP4(m.getPoint4());

            points.add(pointQuestion);
            motAmbigus.add(mot);
        }


        List<Points> pointsList = ajouterPoints(p, motAmbigus, points, expert);
        motAmbiguRepository.saveAll(motAmbigus);
        p.setMot_id(motAmbigus);
        phraseRepository.save(p);
        pointsRepository.saveAll(pointsList);
        expert.setNbPhrases(expert.getNbPhrases() + 1);//quand ce joeur ajoute une phrase son nombre de phrase ajoutée augemente
        expert.setCredit(expert.getCredit()-30);
        expertRepository.save(expert);
        return "success";
    }

    private List<Points> ajouterPoints(Phrase phrase, List<MotAmbigu> mots, List<PointQuestion> points, Expert expert) {
        List<Points> allPoints = new ArrayList<>();
        int i = 0;
        for (MotAmbigu mot : mots) {
            if (mot == null)
                return null;

            // System.out.println(points.get(0) + points.get(1) + points.get(2) + points.get(3) + "mot est");
            Points point = new Points(phrase, mot, points.get(i).getP1(), points.get(i).getP2(), points.get(i).getP3(), points.get(i).getP4());
            i++;

            allPoints.add(point);
            expert.setNbGloses(expert.getNbGloses() + 1);
        }
        return allPoints;
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
    private boolean checkCreditPhrase(Joueur joueur) {

       return joueur.getCredit()>=30;
    }

}
