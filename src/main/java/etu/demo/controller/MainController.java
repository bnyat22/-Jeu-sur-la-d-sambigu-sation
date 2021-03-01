package etu.demo.controller;

import etu.demo.domain.Joueur;
import etu.demo.domain.MotAmbigu;
import etu.demo.domain.Phrase;
import etu.demo.domain.Points;
import etu.demo.repository.JoueurRepository;
import etu.demo.repository.MotAmbiguRepository;
import etu.demo.repository.PhraseRepository;
import etu.demo.repository.PointsRepository;
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

   List<Phrase> questions;
   int currentIndex;

   public boolean checkCredit(Joueur joueur)
   {

       if (joueur.getCredit()<30)
           return false;
      else
          return true;
   }
    @PostMapping("/ajouterPhrase/{phrase}/point/{point}")
  //  @PreAuthorize("hasRole('ADMIN')")
    public Points addPhrase(@PathVariable(value = "phrase") String phrase , @PathVariable(value = "point") int point, @RequestBody MotAmbigu mot)
    {
        Joueur joueur  = joueurRepository.getById(getUserId());
        if (!checkCredit(joueur))
          return null;
        Phrase p = new Phrase(phrase , joueur);
        Set<MotAmbigu> mots = new HashSet<>();
        mots.add(mot);
        p.setMot_id(mots);
        Points points = new Points(p , mot , point);
        motAmbiguRepository.save(mot);
        phraseRepository.save(p);
        pointsRepository.save(points);
        joueur.setNPhrase(joueur.getNPhrase()+1);//quand ce joeur ajoute une phrase son nombre de phrase ajoutée augemente

        joueurRepository.save(joueur);
        return points;

    }
    @GetMapping("/commence")
    public Phrase playGame()
    {
        questions = new ArrayList<>();
        currentIndex = 0;
        questions = phraseRepository.findAll();
        Collections.shuffle(questions);
        Phrase premiereQuestion = questions.get(currentIndex);
        currentIndex++;
        return premiereQuestion;

    }
    @GetMapping("/suivante")
    public Phrase nextQuestion()
    {
        if (currentIndex >= questions.size())
        return null;

            Phrase question = questions.get(currentIndex);
            currentIndex++;
        return phraseRepository.getById(question.getId());
    }

    @PostMapping("/choisir/{choix}")
    public void pickAnswer(@PathVariable String choix)
    {
        Phrase phrase = questions.get(currentIndex-1);
        Points points = pointsRepository.getPointsByPhrase_id(phrase.getId());
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
        }

        joueur.setPoint(updatedPoint);
        joueurRepository.save(joueur);
        updatePoints(phrase.getId());
        pointsRepository.save(points);
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
