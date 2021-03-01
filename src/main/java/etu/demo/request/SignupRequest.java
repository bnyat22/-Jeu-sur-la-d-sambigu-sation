package etu.demo.request;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    private String pseudo;
    private String email;

    private Set<String> role;

    private String password;

}
