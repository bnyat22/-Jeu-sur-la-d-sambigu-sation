package etu.etu.controller;

import etu.etu.domain.*;
import etu.etu.repository.*;
import etu.etu.request.LoginRequest;
import etu.etu.request.SignupRequest;

import etu.etu.security.jwt.JwtUtils;
import etu.etu.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
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


import java.util.HashSet;

import java.util.Set;


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
  @GetMapping("/index")
  public String getIndex(Model model)
  {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      for (GrantedAuthority admin:authentication.getAuthorities())
      {
          if (admin.getAuthority().equals("ADMIN"))
          {
              model.addAttribute("admin" , admin.getAuthority());
          }
      }

      return "index";
  }
    @GetMapping("/logout")
    public String Logout(Model model)
    {
        SecurityContextHolder.clearContext();
        return getLogin(model);
    }

    @PostMapping("/signin")
    public String authenticateUtilisateur(@ModelAttribute("personForm") LoginRequest loginRequest , Model model)
    {

try {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getPseudo(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);


    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    model.addAttribute("jwt", userDetails.getUsername());


    return getIndex(model);
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
    public String engistrerUtilisateur(@ModelAttribute("regForm") SignupRequest signupRequest , Model model)  {
        if (utilisateurRepository.existsByPseudo(signupRequest.getPseudo()))
        {
            model.addAttribute("errorPseudo" , "Cet pseudo exist déjà");
            return "register" ;
        }
        if (utilisateurRepository.existsByEmail(signupRequest.getEmail()))
        {
            model.addAttribute("errorEmail" , "Cet email exist déjà");
            return "register";

        }
        Utilisateur utilisateur = new Utilisateur(signupRequest.getPseudo() ,
                signupRequest.getEmail()
        ,passwordEncoder.encode(signupRequest.getPassword()));

        Role roleDeUtilisateur = roleRepository.findByName(ERoles.ROLE_DEBUTANT);

        Set<Role> roles = new HashSet<>();
        roles.add(roleDeUtilisateur);
        utilisateur.setRoles(roles);

        utilisateurRepository.save(utilisateur);
        Joueur joueur = new Joueur(utilisateur);
        joueurRepository.save(joueur);

        return getLogin(model);
    }
}
