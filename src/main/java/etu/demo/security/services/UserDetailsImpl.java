package etu.demo.security.services;

import etu.demo.domain.Utilisateur;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private long id;
    private String pseudo;
    private String email;

    @JsonIgnore
    private String password;

    private int nPhrase;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(long id, String pseudo, String email, String password, int nPhrase, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
        this.nPhrase = nPhrase;
        this.authorities = authorities;
    }



    public static UserDetailsImpl build(Utilisateur utilisateur)
    {
        List<GrantedAuthority> authorities = utilisateur.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                utilisateur.getId(),
                utilisateur.getPseudo(),
                utilisateur.getEmail(),
                utilisateur.getPassword(),
                utilisateur.getN_phrase(),
                authorities);

    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public long getId()
    {
        return this.id;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return pseudo;
    }

    public String getEmail()
    {
        return email;
    }
    public int getNdePhrase()
    {
        return nPhrase;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
