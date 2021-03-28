package etu.demo.controller;

import etu.demo.domain.*;
import etu.demo.repository.*;
import etu.demo.request.LoginRequest;
import etu.demo.request.SignupRequest;
import etu.demo.response.JwtResponse;
import etu.demo.response.MessageReponse;
import etu.demo.security.jwt.JwtUtils;
import etu.demo.security.services.UserDetailsImpl;
import org.apache.tomcat.util.http.ServerCookies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UtilisateurRepository utilisateurRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JoueurRepository joueurRepository;
    @Autowired
    PhraseRepository phraseRepository;
   // @Autowired
  //  RankRepository rankRepository;
    @GetMapping("/")
  public String getLogin(Model model)
  {
      LoginRequest loginRequest = new LoginRequest();
      model.addAttribute("personForm", loginRequest);
      return "login";
  }
    @PostMapping("/log")
    public ResponseEntity<?> log(@RequestBody LoginRequest loginRequest) {
        //  model.addAttribute("personForm", loginRequest);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPseudo(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.genereJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return     ResponseEntity.ok(new JwtResponse(jwt));
    }
    @GetMapping("/jeu")
    public String getl()
    {
        return "jouer";
    }
    @PostMapping("/signin")
    public String authenticateUtilisateur(@ModelAttribute("personForm") LoginRequest loginRequest , Model model)
    {
      //  model.addAttribute("personForm", loginRequest);

       Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPseudo() , loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.genereJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
System.out.println("dfsdgsdf" + userDetails.getUsername());
model.addAttribute("jwt" , userDetails.getUsername());


        return "index";


    }
    @GetMapping("/phras")
    private List<Phrase> getPhrase()
    {

        return phraseRepository.findAll();
    }
    @GetMapping("/reg")
    public String getReg(Model model)
    {
        SignupRequest signupRequest = new SignupRequest();
        model.addAttribute("regForm", signupRequest);
        return "register";
    }
    @PostMapping("/signup")
    public ResponseEntity<?> engistrerUtilisateur(@ModelAttribute("regForm") SignupRequest signupRequest) throws URISyntaxException {
        if (utilisateurRepository.existsByPseudo(signupRequest.getPseudo()))
        {
            return ResponseEntity.badRequest()
                    .body(new MessageReponse("Erreur: ce pseudo existe déja!"));
        }
        if (utilisateurRepository.existsByEmail(signupRequest.getEmail()))
        {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageReponse("Erreur: cet email existe déja!"));
        }
        Utilisateur utilisateur = new Utilisateur(signupRequest.getPseudo() ,
                signupRequest.getEmail()
        ,passwordEncoder.encode(signupRequest.getPassword()));

        Role roleDeUtilisateur = roleRepository.findByName(ERoles.JOUEUR_DEBUTANT);

        Set<Role> roles = new HashSet<>();
        roles.add(roleDeUtilisateur);
        utilisateur.setRoles(roles);

        utilisateurRepository.save(utilisateur);
        Joueur joueur = new Joueur(utilisateur);
        joueurRepository.save(joueur);
Rank rank = new Rank();
//rank.setJoueur(joueur);
//rankRepository.save(rank);
        URI home = new URI("http://localhost:9656/");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(home);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
