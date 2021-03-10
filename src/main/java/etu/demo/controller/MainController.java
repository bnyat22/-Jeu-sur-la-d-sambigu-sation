package etu.demo.controller;

import etu.demo.domain.*;
import etu.demo.repository.*;
import etu.demo.response.QuestionReponse;
import etu.demo.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    private PhraseRepository phraseRepository;

    @Autowired
    private MotAmbiguRepository motAmbiguRepository;
    @Autowired
    private PointsRepository pointsRepository;
    @Autowired
     private JoueurRepository joueurRepository;
  //  @Autowired
    //        private RankRepository rankRepository;
    @Autowired
            private ExpertRepository expertRepository;

   List<Object[]> questions;
   int currentIndex;

   public boolean checkCredit(Expert expert)
   {

       if (expert.getCredit()<30)
           return false;
      else
          return true;
   }
   private void ajouterPoints(Phrase phrase , List<MotAmbigu> mots , List<Integer> points ,Expert expert)
   {
       for (MotAmbigu mot: mots) {
           if (mot == null)
               return;
           Points point;
            point = new Points(phrase, mot, points.get(0));
            if (points.get(1) !=null)
               point = new Points(phrase, mot, points.get(0) , points.get(1));
           else   if (points.get(2) !=null)
               point = new Points(phrase, mot, points.get(0) , points.get(1),points.get(2));
           else   if (points.get(3) !=null)
               point = new Points(phrase, mot, points.get(0) , points.get(1),points.get(2), points.get(3));
           motAmbiguRepository.save(mot);
           pointsRepository.save(point);
           expert.setNbGloses(expert.getNbGloses() + 1);
       }
   }
    @PostMapping("/ajouterPhrase/{phrase}/{point}")
    @PreAuthorize("hasRole('ADMIN') or ('JOUEUR_EXPERT')")
    @ResponseBody
    public String addPhrase(@PathVariable(value = "phrase") String phrase
            , @PathVariable(value = "point") List<Integer> points, @RequestBody List<MotAmbigu> mots)
    {
        Expert expert  = expertRepository.getById(getUserId());
        if (!checkCredit(expert))
          return null;
        Phrase p = new Phrase(phrase , expert);
        p.setMot_id(mots);
        ajouterPoints(p , mots , points , expert);
        phraseRepository.save(p);
        expert.setNbPhrases(expert.getNbPhrases()+1);//quand ce joeur ajoute une phrase son nombre de phrase ajoutée augemente
        expertRepository.save(expert);
        return "points";

    }
    @GetMapping("/commence")
    public Object[] playGame()
    {
        questions = new ArrayList<>();
        currentIndex = 0;
        questions = phraseRepository.findPhras();
        Collections.shuffle(questions);
        Object[] premiereQuestion = questions.get(currentIndex);
        currentIndex++;
        return premiereQuestion;

    }
    @GetMapping("/suivante")
    public Object nextQuestion()
    {
        if (currentIndex >= questions.size()) {
            return null;
        }
            Object question = questions.get(currentIndex);
            currentIndex++;
        return question;
    }
    @PostMapping("/ajouteGlose/{glose}")
    @PreAuthorize("hasRole('ADMIN') or ('JOUEUR_INTERMIDAIRE') or ('JOUEUR_EXPERT')")
    public String addGlose(@RequestBody MotAmbigu mot , @PathVariable(name = "glose") String glose)
    {
        if (mot.getChoix2() == null) {
            mot.setChoix2(glose);
            return "votre glose est ajouté";
        }

       else   if (mot.getChoix3() == null) {
            mot.setChoix3(glose);
            return "votre glose est ajouté";
        }
            else if (mot.getChoix4() == null) {
            mot.setChoix4(glose);
            return "votre glose est ajouté";
        } else
            return "Il n'y a pas de place à ajouter";

    }
    @PostMapping("/choisir/{choix}")
    public void pickAnswer(@PathVariable String choix)
    {
       /* Phrase phrase = questions.get(currentIndex-1);
     //   Points points = pointsRepository.getPointsByPhrase_id(phrase.getId());
        MotAmbigu motAmbigu = motAmbiguRepository.getById(points.getMotAmbigu_id().getId());
        long userId = getUserId();
        Joueur joueur = joueurRepository.getById(userId);
        int updatedPoint=0;
        if (motAmbigu.getChoix1().equals(choix)) {
            updatedPoint = joueur.getPoint() + points.getPoint_choix1();
            points.setNJouer_choix1(points.getNJouer_choix1()+1);//quand un jouer choisi ce choix le numéro de jouer qui ont choisi ce choix augement
        }
        else if(motAmbigu.getChoix2().equals(choix)) {
            updatedPoint = joueur.getPoint() + points.getPoint_choix2();
            points.setNJouer_choix2(points.getNJouer_choix2()+1);
        }
        else if (motAmbigu.getChoix3().equals(choix)) {
            updatedPoint = joueur.getPoint() + points.getPoint_choix3();
            points.setNJouer_choix3(points.getNJouer_choix3()+1);
        }
        else {
            updatedPoint = joueur.getPoint() + points.getPoint_choix4();
            points.setNJouer_choix4(points.getNJouer_choix4()+1);
        }*
Intermédiaire intermédiaire = new Intermédiaire(joueur.getUtilisateur());
        joueur.setPoint(updatedPoint);
        joueurRepository.save(intermédiaire);
        updatePoints(phrase.getId());
        pointsRepository.save(points);*/
    }
    private long getUserId()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();

    }
    //ici on vérifie si certain jouer choisissent un choix et
    // ce choix n'a pas assez de point donc le point augement
    private void updatePoints(int phrase_id)
    {
        Points points = pointsRepository.getPointsByPhrase_id(phrase_id);
     if (points.getPoint_choix1()<5 && points.getNJouer_choix1()>5)
         points.setPoint_choix1(10);
        if (points.getPoint_choix2()<5 && points.getNJouer_choix2()>5)
            points.setPoint_choix2(10);
        if (points.getPoint_choix3()<5 && points.getNJouer_choix3()>5)
            points.setPoint_choix3(10);
        if (points.getPoint_choix4()<5 && points.getNJouer_choix4()>5)
            points.setPoint_choix4(10);

    }


}
