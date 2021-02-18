package etu.demo.response;

import lombok.Data;
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

    public JwtResponse(String token, long id, String pseudo, String email, int ndePhrase, List<String> roles) {
        this.token = token;
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        NdePhrase = ndePhrase;
        this.roles = roles;
    }
}
