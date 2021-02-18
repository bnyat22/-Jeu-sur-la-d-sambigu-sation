package etu.demo.controller;

import etu.demo.domain.ERoles;
import etu.demo.domain.Role;
import etu.demo.domain.Utilisateur;
import etu.demo.repository.RoleRepository;
import etu.demo.repository.UtilisateurRepository;
import etu.demo.request.LoginRequest;
import etu.demo.request.SignupRequest;
import etu.demo.response.JwtResponse;
import etu.demo.response.MessageReponse;
import etu.demo.security.jwt.JwtUtils;
import etu.demo.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUtilisateur(@RequestBody LoginRequest loginRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPseudo() , loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.genereJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt ,
                userDetails.getId(),
                userDetails.getUsername() ,
                userDetails.getEmail() ,
                userDetails.getNdePhrase(),
                roles));

    }

    @PostMapping("/signup")
    public ResponseEntity<?> engistrerUtilisateur(@RequestBody SignupRequest signupRequest)
    {
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
        return ResponseEntity.ok(new MessageReponse("Vous êtes  engistré!"));
    }
}