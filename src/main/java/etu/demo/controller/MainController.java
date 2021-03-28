package etu.demo.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import etu.demo.domain.*;
import etu.demo.repository.*;
import etu.demo.response.QuestionReponse;
import etu.demo.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@Controller
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
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    //  @Autowired
    //        private RankRepository rankRepository;
    @Autowired
    private ExpertRepository expertRepository;

    List<Questions> questions;
    int currentIndex;

    public boolean checkCredit(Expert expert) {

        if (expert.getCredit() < 30)
            return false;
        else
            return true;
    }

    private void ajouterPoints(Phrase phrase, List<MotAmbigu> mots, List<Integer> points, Expert expert) {
        for (MotAmbigu mot : mots) {
            if (mot == null)
                return;
            Points point;
            point = new Points(phrase, mot, points.get(0));
            if (points.get(1) != null)
                point = new Points(phrase, mot, points.get(0), points.get(1));
            else if (points.get(2) != null)
                point = new Points(phrase, mot, points.get(0), points.get(1), points.get(2));
            else if (points.get(3) != null)
                point = new Points(phrase, mot, points.get(0), points.get(1), points.get(2), points.get(3));
            motAmbiguRepository.save(mot);
            pointsRepository.save(point);
            expert.setNbGloses(expert.getNbGloses() + 1);
        }
    }

    @PostMapping("/ajouterPhrase/{phrase}/{point}")
    @PreAuthorize("hasRole('ADMIN') or ('JOUEUR_EXPERT')")
    @ResponseBody
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    public String addPhrase(@PathVariable(value = "phrase") String phrase
            , @PathVariable(value = "point") List<Integer> points, @RequestBody List<MotAmbigu> mots ) {
        Expert expert = expertRepository.getById(getUserId());
        if (!checkCredit(expert))
            return null;
        Phrase p = new Phrase(phrase, expert);
        p.setMot_id(mots);
        ajouterPoints(p, mots, points, expert);
        phraseRepository.save(p);
        expert.setNbPhrases(expert.getNbPhrases() + 1);//quand ce joeur ajoute une phrase son nombre de phrase ajoutée augemente
        expertRepository.save(expert);
        return "points";

    }

    @GetMapping("/jeu")
    public String jouer(Model model) {
        questions = new ArrayList<>();
        //  model.addAttribute("motambigus",questions);
        return "jouer";
    }

    @GetMapping("/commence")
    public String playGame(Model model , @ModelAttribute("jwt") String pseudo) {
        questions = new ArrayList<>();
        currentIndex = 0;
        System.out.println("qdghfdjhdsfgh" + pseudo);
        questions = phraseRepository.findPhras();
        Collections.shuffle(questions);


        Questions premiereQuestion = questions.get(currentIndex);

       List<ChoixReponse> choixReponses = getMotsDePhrase(premiereQuestion.getPhrase_id());
        System.out.println("size reponse wordakan "+ choixReponses.size());
        Option option = new Option();
        model.addAttribute("option" , option);
        model.addAttribute("question", choixReponses);
        currentIndex++;
        return "jouer";

    }

    @GetMapping("/suivante")
    public String nextQuestion(Model model) {
        if (currentIndex >= questions.size()) {
            return null;
        }
        Questions questionSuivante = questions.get(currentIndex);
        int id = questionSuivante.getPhrase_id();
        List<Questions> mots = phraseRepository.findMot(id);
        model.addAttribute("question", mots);
        currentIndex++;
        return "jouer";
    }

    @PostMapping("/ajouteGlose/{glose}")
    @PreAuthorize("hasRole('ADMIN') or ('JOUEUR_INTERMIDAIRE') or ('JOUEUR_EXPERT')")
    public String addGlose(@RequestBody MotAmbigu mot, @PathVariable(name = "glose") String glose) {
        if (mot.getChoix2() == null) {
            mot.setChoix2(glose);
            return "votre glose est ajouté";
        } else if (mot.getChoix3() == null) {
            mot.setChoix3(glose);
            return "votre glose est ajouté";
        } else if (mot.getChoix4() == null) {
            mot.setChoix4(glose);
            return "votre glose est ajouté";
        } else
            return "Il n'y a pas de place à ajouter";

    }

    @PostMapping("/choisir")
    @ResponseStatus(value = HttpStatus.OK)
    public void pickAnswer(@ModelAttribute("option") Option option) {
        Questions question = questions.get(currentIndex - 1);
       List<ChoixReponse> choixReponses = getMotsDePhrase(question.getPhrase_id());
        MotAmbigu motAmbigu = new MotAmbigu();
        System.out.println("my word size " + question.getMot().size());
        for (ChoixReponse m : choixReponses)
        {
            System.out.println(m + "wshakay era");
            System.out.println(option.getWord() + " wshakay era optionaka");
            if (m.getMot().equals(option.getWord()))
            {

              MotAmbigu  mo = motAmbiguRepository.getById(m.getMot_id());
              System.out.println("wshaka " + mo.getMot());
              motAmbigu.setId(mo.getId());
              motAmbigu.setMot(mo.getMot());
              motAmbigu.setChoix1(mo.getChoix1());
              motAmbigu.setChoix2(mo.getChoix2());
              motAmbigu.setChoix3(mo.getChoix3());
              motAmbigu.setChoix4(mo.getChoix4());
                break;
            }
        }
        Phrase phrase = phraseRepository.getById(question.getPhrase_id());
        System.out.println(phrase.getPhrase() + "heyo");
       Points p = getPoint(phrase.getId() , motAmbigu.getId());
            long userId = getUserId();
            Joueur joueur = joueurRepository.getById(userId);
            System.out.println(joueur.getId());
            int updatedPoint = 0;
            System.out.println(option.getChoix());
            System.out.println(motAmbigu.getChoix1());

        System.out.println(p.getId() + "point id");
            if (option.getChoix() != null && motAmbigu.getChoix1().equals(option.getChoix())) {
                System.out.println("point "+ p.getPoint_choix1());
                System.out.println("joueur  "+ joueur.getPoint());
                updatedPoint = joueur.getPoint() + p.getPoint_choix1();
                p.setNJouer_choix1(p.getNJouer_choix1() + 1);//quand un jouer choisi ce choix le numéro de jouer qui ont choisi ce choix augement
            } else if (option.getChoix() != null && motAmbigu.getChoix2().equals(option.getChoix())) {
                updatedPoint = joueur.getPoint() + p.getPoint_choix2();
                p.setNJouer_choix2(p.getNJouer_choix2() + 1);
            } else if (option.getChoix() != null && motAmbigu.getChoix3() !=null && motAmbigu.getChoix3().equals(option.getChoix())) {
                updatedPoint = joueur.getPoint() + p.getPoint_choix3();
                p.setNJouer_choix3(p.getNJouer_choix3() + 1);
            } else if (option.getChoix() != null && motAmbigu.getChoix4() !=null) {
                updatedPoint = joueur.getPoint() + p.getPoint_choix4();
                p.setNJouer_choix4(p.getNJouer_choix4() + 1);
            }

          //  Intermédiaire intermédiaire = new Intermédiaire(joueur.getUtilisateur());
            joueur.setPoint(updatedPoint);
            joueurRepository.save(joueur);
            updatePoints(p);
            pointsRepository.save(p);


    }

    private List<ChoixReponse> getMotsDePhrase(int phrase_id)
    {
        List<Questions> mots = phraseRepository.findMot(phrase_id);
        System.out.println("size listakam wordakan "+ mots.size());
        List<ChoixReponse> choixReponses = new ArrayList<>();
        for (Questions q : mots)
        {
            ChoixReponse choixReponse = new ChoixReponse();
            choixReponse.setId(q.getPhrase_id());
            choixReponse.setPhrase(q.getPhrase());
            for (MotAmbigu m: q.getMot())
            {

                System.out.println("my quesition id " + m.getMot());
                choixReponse.setMot(m.getMot());
                choixReponse.setMot_id(m.getId());
                List<String> options = new ArrayList<>();
                options.add(m.getChoix1());
                options.add(m.getChoix2());
                options.add(m.getChoix3());
                options.add(m.getChoix4());
                choixReponse.setChoix(options);
            }
            choixReponses.add(choixReponse);

        }
        return choixReponses;
    }
    private Points getPoint(int phrase_id , int mot_id)
    {
        List<Points> points = pointsRepository.getPointsByPhrase_id(phrase_id);
        System.out.println(points.size() + "numero de point");
        for (Points p : points)
        {
            System.out.println("gaishta pointakan" + p.getMotAmbigu_id().getId());
            System.out.println("gaishta pointakan" + mot_id);
            if (p.getMotAmbigu_id().getId() == mot_id)
                return p;
        }
        return null;
    }
    private long getUserId() {
       /* Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String myUser = authentication.getName();
       System.out.println(myUser + "fhgfjhgdfsdghj");
        UserDetailsImpl userDetails;
       if (authentication instanceof UserDetails) {
           userDetails = (UserDetailsImpl) authentication.getPrincipal();
           return userDetails.getId();
       }
       else
       {
           Optional<Utilisateur> utilisateur = utilisateurRepository.findByPseudo(myUser);
           return utilisateur.get().getId();*/
      //  System.out.println( + "fhgfjhgdfsdghj");
     //   Optional<Utilisateur> utilisateur = utilisateurRepository.findByPseudo();
        return 3;
    }


    //ici on vérifie si certain jouer choisissent un choix et
    // ce choix n'a pas assez de point donc le point augement
    private void updatePoints(Points points) {
        //  Points points = pointsRepository.getByMotAmbigu_id(motAmbigu);
        if (points.getPoint_choix1() < 5 && points.getNJouer_choix1() > 5)
            points.setPoint_choix1(10);
        if (points.getPoint_choix2() < 5 && points.getNJouer_choix2() > 5)
            points.setPoint_choix2(10);
        if (points.getPoint_choix3() < 5 && points.getNJouer_choix3() > 5)
            points.setPoint_choix3(10);
        if (points.getPoint_choix4() < 5 && points.getNJouer_choix4() > 5)
            points.setPoint_choix4(10);

    }
}



