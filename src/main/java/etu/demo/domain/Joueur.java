package etu.demo.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;

@Entity
@Table(name = "joueur")
@Getter
@Setter
@NoArgsConstructor
public class Joueur {

    @Id
    @Column(name = "user_id")
    long id;

    private int point;

    private int credit;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private Utilisateur utilisateur;

    private int nPhrase;
    public Joueur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
     point = 0;
     credit = 0;
    }
}
