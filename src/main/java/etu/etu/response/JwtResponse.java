package etu.etu.response;

import etu.etu.domain.Joueur;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private long id;
    private String pseudo;
    private String email;
    private int NdePhrase;
    private List<String> roles;
    private Joueur jouer;

    public JwtResponse(String token) {
        this.token = token;

    }
}
