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
import org.springframework.security.authentication.BadCredentialsException;
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
    @GetMapping("/logout")
    public String Logout()
    {
        SecurityContextHolder.clearContext();
        return "index";
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

    @PostMapping("/signin")
    public String authenticateUtilisateur(@ModelAttribute("personForm") LoginRequest loginRequest , Model model)
    {

try {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getPseudo(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.genereJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    model.addAttribute("jwt", userDetails.getUsername());


    return "index";
} catch (BadCredentialsException e)
{

    model.addAttribute("bad" , "Votre pseudo ou mot de passe n'est pas correcte");
    return "login";
}

    }
    @GetMapping("/reg")
    public String getReg(Model model)
    {
        SignupRequest signupRequest = new SignupRequest();
        model.addAttribute("regForm", signupRequest);
        return "register";
    }
    @PostMapping("/signup")
    public String engistrerUtilisateur(@ModelAttribute("regForm") SignupRequest signupRequest , Model model) throws URISyntaxException {
        if (utilisateurRepository.existsByPseudo(signupRequest.getPseudo()))
        {
            model.addAttribute("errorPseudo" , "Cet pseudo exist déjà");
          /*  URI reg = new URI("http://localhost:9656/auth/reg");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(reg);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);*/
            return "register" ;
        }
        if (utilisateurRepository.existsByEmail(signupRequest.getEmail()))
        {
            model.addAttribute("errorEmail" , "Cet email exist déjà");
          /*  URI reg = new URI("http://localhost:9656/auth/reg");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(reg);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);*/
            return "register";

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

     /*   URI home = new URI("http://localhost:9656/");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(home);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);*/
        return "index";
    }
}
