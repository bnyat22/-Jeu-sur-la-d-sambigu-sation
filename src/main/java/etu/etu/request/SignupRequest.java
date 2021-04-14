package etu.etu.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    private String pseudo;
    private String email;

    private Set<String> role;

    private String password;

}
