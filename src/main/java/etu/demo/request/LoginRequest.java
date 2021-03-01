package etu.demo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String pseudo;
    private String password;
}
