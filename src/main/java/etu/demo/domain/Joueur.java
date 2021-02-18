package etu.demo.domain;

import lombok.Data;


import javax.persistence.*;

@Entity
@Table(name = "joueur")
@Data
public class Joueur {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long joueur_id;

    private int point;

    private int credit;

    @ManyToOne()
    @JoinColumn(name = "id")
    private Utilisateur n_utilisatuer;

}
