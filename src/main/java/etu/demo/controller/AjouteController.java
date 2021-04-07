package etu.demo.controller;

import etu.demo.domain.*;
import etu.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    private IntermidaireRepository intermidaireRepository;

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
    public String addPhrase(@ModelAttribute("addPhrase") Question question, Model model) {
        Expert expert = expertRepository.getById(1);
        // if (!checkCredit(joueur))
        //    return null;
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
        expertRepository.save(expert);
        return "phrase";
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

    @GetMapping("/getGlose")
    public String getGlose(Model model)
    {
        Glose glose = new Glose() ;
        model.addAttribute("glose" , glose);
        return "glose";
    }
    @PostMapping("/ajouteGlose")
   // @PreAuthorize("hasRole('ADMIN') or ('JOUEUR_INTERMIDAIRE') or ('JOUEUR_EXPERT')")
    public String addGlose(@ModelAttribute("glose") Glose glose) {
    Phrase phrase = phraseRepository.getByPhrase(glose.getPhrase());
        System.out.println(phrase.getPhrase() + "atw");
        System.out.println(phrase.getId() + "atw");
        System.out.println(glose.getMot() + "atw");
        System.out.println(glose.getGlose() + "atw");
      Questions questions = phraseRepository.getByWord(phrase.getId(), glose.getMot());
        System.out.println(questions.getPhrase() + "kura ");
      for (MotAmbigu m:questions.getMot()) {
          System.out.println("atw");
          if (m.getMot().equals(glose.getMot()))
          {
              Points points = pointsRepository.getPointsByMotAmbigu_id(m.getId());
              System.out.println("hata era");
              if (m.getChoix3().equals(""))
              {
                  m.setChoix3(glose.getGlose());
                  points.setPoint_choix3(glose.getPoint());
              } else if (m.getChoix4().equals("")) {
                  m.setChoix4(glose.getGlose());
                  points.setPoint_choix4(glose.getPoint());
              }
              else {
                  return "index";
              }
              System.out.println("hata erash");
              Intermédiaire intermédiaire = intermidaireRepository.getById(1);
              intermédiaire.setNbGloses(intermédiaire.getNbGloses() +1);
              intermidaireRepository.save(intermédiaire);
              motAmbiguRepository.save(m);
              return "index";
          }
      }
      return "index";

    }
    public boolean checkCredit(Joueur joueur) {

        if (joueur.getCredit() < 30)
            return false;
        else
            return true;
    }
}
