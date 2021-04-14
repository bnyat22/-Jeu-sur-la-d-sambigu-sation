package etu.etu.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;



@Entity
@Table(name = "utilisateur" ,
uniqueConstraints = {
@UniqueConstraint(columnNames = "pseudo") ,
@UniqueConstraint(columnNames = "email")})
@Getter
@Setter
public class Utilisateur {


  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull
    private String pseudo;

     @NotNull
    private String email;

     @NotNull
    private String password;

     @ManyToMany(fetch = FetchType.LAZY)
     @JoinTable(name = "user_role" ,
     joinColumns = @JoinColumn(name = "user_id") ,
     inverseJoinColumns = @JoinColumn(name = "role_id"))
     private Set<Role> roles = new HashSet<>();



    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "utilisateur")
    @PrimaryKeyJoinColumn
    private Joueur joueur;



  public Utilisateur() {
  }

  public Utilisateur( String pseudo, String email, String password) {
    this.pseudo = pseudo;
    this.email = email;
    this.password = password;
  }



}
