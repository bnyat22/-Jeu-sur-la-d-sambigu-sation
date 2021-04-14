package etu.etu.controller;

import etu.etu.domain.*;
import etu.etu.repository.*;
import etu.etu.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private ExpertRepository expertRepository;
    @Autowired
    private IntermidaireRepository intermidaireRepository;
    @Autowired
    private RoleRepository roleRepository;

    List<Phrase> questions;
    int currentIndex;



    @GetMapping("/jeu")
    public String jouer() {
        questions = new ArrayList<>();
        //  model.addAttribute("motambigus",questions);
        return "jouer";
    }

    @GetMapping("/commence")
    public String playGame(Model model , @ModelAttribute("jwt") String pseudo) {
        questions = new ArrayList<>();
        currentIndex = 0;

        questions = phraseRepository.findAll();
        System.out.println("qdghfdjhdsfgh" + questions.size());
        Collections.shuffle(questions);

        Phrase premiereQuestion = questions.get(currentIndex);

        List<ChoixReponse> choixReponses = getMotsDePhrase(premiereQuestion.getId());
        System.out.println("size reponse wordakan "+ choixReponses.size());
        Options optionsList = new Options();
        for (MotAmbigu m : premiereQuestion.getMot_id())
        {
            optionsList.optionList.add(new Option());
        }
        System.out.println("size listakam " + optionsList.optionList.size());
        System.out.println("current index " + currentIndex);
        model.addAttribute("options" , optionsList);
        model.addAttribute("question", choixReponses);
        currentIndex++;
        return "jouer";

    }

    @GetMapping("/suivante")
    public String nextQuestion(Model model) {
        if (currentIndex >= questions.size()) {
            return "fin";
        }
        Phrase questionsSuivante = questions.get(currentIndex);

        List<ChoixReponse> choixReponses = getMotsDePhrase(questionsSuivante.getId());
        System.out.println("size reponse wordakan "+ choixReponses.size());
        Options optionsList = new Options();
        for (MotAmbigu m : questionsSuivante.getMot_id())
        {
            optionsList.optionList.add(new Option());
        }
        System.out.println("size listakam " + optionsList.optionList.size());
        model.addAttribute("options" , optionsList);
        model.addAttribute("question", choixReponses);
        System.out.println("current index " + currentIndex);
        currentIndex++;
        return "jouer";
    }



    @PostMapping("/choisir")
    @ResponseStatus(value = HttpStatus.OK)
    public String pickAnswer(@ModelAttribute("options") Options options , Model  model) {
        Phrase question = questions.get(currentIndex - 1);
        List<ChoixReponse> choixReponses = getMotsDePhrase(question.getId());
        MotAmbigu motAmbigu = new MotAmbigu();
        System.out.println("my word size " + question.getMot_id().size());
        for (ChoixReponse m : choixReponses) {
            for (Option option : options.getOptionList()) {
                System.out.println(m + "wshakay era");
                System.out.println(option.getChoix() + " wshakay era optionaka");
                if (m.getMot().equals(option.getWord())) {

                    MotAmbigu mo = motAmbiguRepository.getById(m.getMot_id());
                    System.out.println("wshaka " + mo.getMot());
                    motAmbigu.setId(mo.getId());
                    motAmbigu.setMot(mo.getMot());
                    motAmbigu.setChoix1(mo.getChoix1());
                    motAmbigu.setChoix2(mo.getChoix2());
                    motAmbigu.setChoix3(mo.getChoix3());
                    motAmbigu.setChoix4(mo.getChoix4());

                    Phrase phrase = phraseRepository.getById(question.getId());
                    System.out.println(phrase.getPhrase() + "heyo");
                    Points p = getPoint(phrase.getId(), motAmbigu.getId());
                    long userId = getUserId();
                    Joueur joueur = joueurRepository.getById(userId);
                    System.out.println(joueur.getId());
                    int updatedPoint = 0;
                    int updateCredit = 0;
                    System.out.println(option.getWord());
                    System.out.println(motAmbigu.getChoix1());

                    System.out.println(p.getId() + "point id");
                    if (option.getChoix() != null && motAmbigu.getChoix1().equals(option.getChoix())) {
                        System.out.println("point " + p.getPoint_choix1());
                        System.out.println("joueur  " + joueur.getPoint());
                        updatedPoint = joueur.getPoint() + p.getPoint_choix1();
                        updateCredit = joueur.getCredit() + p.getPoint_choix1();
                        option.setPoint(p.getPoint_choix1());
                        p.setNJouer_choix1(p.getNJouer_choix1() + 1);//quand un jouer choisi ce choix le numéro de jouer qui ont choisi ce choix augement
                    } else if (option.getChoix() != null && motAmbigu.getChoix2().equals(option.getChoix())) {
                        updatedPoint = joueur.getPoint() + p.getPoint_choix2();
                        updateCredit = joueur.getCredit() + p.getPoint_choix2();
                        p.setNJouer_choix2(p.getNJouer_choix2() + 1);
                        option.setPoint(p.getPoint_choix2());
                    } else if (option.getChoix() != null && motAmbigu.getChoix3() != null && motAmbigu.getChoix3().equals(option.getChoix())) {
                        updatedPoint = joueur.getPoint() + p.getPoint_choix3();
                        updateCredit = joueur.getCredit() + p.getPoint_choix3();
                        p.setNJouer_choix3(p.getNJouer_choix3() + 1);
                        option.setPoint(p.getPoint_choix3());
                    } else if (option.getChoix() != null && motAmbigu.getChoix4() != null && motAmbigu.getChoix4().equals(option.getChoix())) {
                        updatedPoint = joueur.getPoint() + p.getPoint_choix4();
                        updateCredit = joueur.getCredit() + p.getPoint_choix4();
                        p.setNJouer_choix4(p.getNJouer_choix4() + 1);
                        option.setPoint(p.getPoint_choix4());
                    }
                    joueur.setPoint(updatedPoint);
                    joueur.setCredit(updateCredit);
                    joueurRepository.save(joueur);
                    updatePoints(p);
                    pointsRepository.save(p);
                }
            }
        }

        checkUserPoints(getUserId());
        options.isValide = true;
        model.addAttribute("options" , options);
        model.addAttribute("question", choixReponses);
        return "jouer";
    }

    @GetMapping("/getGlose")
    public String getGlose(Model model)
    {
        setCurrentQuestion(model);
        model.addAttribute("glose" , new Glose());
        return "jouer";
    }
    @PostMapping("/addGlose")
    // @PreAuthorize("hasRole('ADMIN') or ('JOUEUR_INTERMIDAIRE') or ('JOUEUR_EXPERT')")
    public String addGlose(@ModelAttribute("glose") Glose glose , Model model) {
        setCurrentQuestion(model);
        Expert expert = expertRepository.getById(getUserId());
        if (!checkCreditGlose(expert))
            return "errorCredit";
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
                    model.addAttribute("place" , "Il n'y a plus de place pour ajouter une glose");
                    return "jouer";
                }
                System.out.println("hata erash");
                Intermédiaire intermédiaire = intermidaireRepository.getById(1);
                intermédiaire.setNbGloses(intermédiaire.getNbGloses() +1);
                intermédiaire.setCredit(intermédiaire.getCredit()-15);
                intermidaireRepository.save(intermédiaire);
                motAmbiguRepository.save(m);
                model.addAttribute("suc" , "Ajouté!");
                return "jouer";
            }
        }
        return "jouer";

    }
    private void checkUserPoints(long id) {

        Joueur joueur = joueurRepository.getById(id);
        if (joueur.getPoint()> 100 && intermidaireRepository.countById(id) == 0)
        {
            intermidaireRepository.insert(id);
            Utilisateur utilisateur = utilisateurRepository.findById(id).get();
            Set<Role> roles = utilisateur.getRoles();
            roles.add(roleRepository.findByName(ERoles.ROLE_INTERMIDAIRE));
            utilisateurRepository.save(utilisateur);
        }
        if (joueur.getPoint()> 200 && expertRepository.countById(id) == 0)
        {
            expertRepository.insert(id);
            Utilisateur utilisateur = utilisateurRepository.findById(id).get();
            Set<Role> roles = utilisateur.getRoles();
            roles.add(roleRepository.findByName(ERoles.ROLE_EXPERT));
            utilisateurRepository.save(utilisateur);
        }
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


    //ici on vérifie si certain jouer choisissent un choix et
    // ce choix n'a pas assez de point donc le point augement
    private void updatePoints(Points points) {
        //  Points points = pointsRepository.getByMotAmbigu_id(motAmbigu);
        if (points.getPoint_choix1() < 5 && points.getNJouer_choix1() > 5) {
            System.out.println("deta erokana sytuyktjhrgfdqsuykhtgrfed");
            points.setPoint_choix1(10);
        }
        if (points.getPoint_choix2() < 5 && points.getNJouer_choix2() > 5)
            points.setPoint_choix2(10);
        if (points.getPoint_choix3() < 5 && points.getNJouer_choix3() > 5)
            points.setPoint_choix3(10);
        if (points.getPoint_choix4() < 5 && points.getNJouer_choix4() > 5)
            points.setPoint_choix4(10);


    }
    private boolean checkCreditGlose(Joueur joueur) {

        return joueur.getCredit()>15;
    }
    private void setCurrentQuestion(Model model)
    {
        if (currentIndex >= questions.size()) {
            return;
        }
        Phrase questionsSuivante = questions.get(currentIndex - 1);

        List<ChoixReponse> choixReponses = getMotsDePhrase(questionsSuivante.getId());
        System.out.println("size reponse wordakan "+ choixReponses.size());
        Options optionsList = getOptionList(questionsSuivante.getMot_id());
        System.out.println("size listakam " + optionsList.optionList.size());
        model.addAttribute("options" , optionsList);
        model.addAttribute("question", choixReponses);
        System.out.println("current index " + currentIndex);
    }
    private Options getOptionList(List<MotAmbigu> motAmbiguList)
    {
        Options optionsList = new Options();
        for (MotAmbigu m : motAmbiguList)
        {
            optionsList.optionList.add(new Option());
        }
        return optionsList;
    }
}



