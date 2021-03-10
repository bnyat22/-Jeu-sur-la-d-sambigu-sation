package etu.demo.controller;

import etu.demo.domain.*;
import etu.demo.repository.*;
import etu.demo.request.LoginRequest;
import etu.demo.request.SignupRequest;
import etu.demo.response.JwtResponse;
import etu.demo.response.MessageReponse;
import etu.demo.security.jwt.JwtUtils;
import etu.demo.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/signin")
    public String authenticateUtilisateur(@RequestBody LoginRequest loginRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPseudo() , loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.genereJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return "index";
                /*ResponseEntity.ok(new JwtResponse(jwt , userDetails.getId()
        ,userDetails.getUsername() ,
                userDetails.getPassword()
        ,userDetails.getNdePhrase(),
                roles,
                userDetails.getJoueur()));*/

    }
    @GetMapping("/phras")
    private List<Phrase> getPhrase()
    {

        return phraseRepository.findAll();
    }
    @PostMapping("/signup")
    public ResponseEntity<?> engistrerUtilisateur(@RequestBody SignupRequest signupRequest, Model model) throws URISyntaxException {
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
        ,signupRequest.getPassword());

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
