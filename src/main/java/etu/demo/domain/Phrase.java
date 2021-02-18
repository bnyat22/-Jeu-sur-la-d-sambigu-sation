package etu.demo.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "phras")
public class Phrase {

@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String phrase;

    private String niveau;

    private int joueur_id;

    private int mot1_id;
    private int mot2_id;
    private int mot3_id;

    private int n_Joueur;

}
