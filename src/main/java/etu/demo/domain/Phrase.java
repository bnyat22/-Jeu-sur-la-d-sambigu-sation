package etu.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "phras")
@NoArgsConstructor
public class Phrase {

@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



     private int nbJouer;

    private String phrase;

    private String niveau;

    @ManyToOne
    @JoinColumn(name = "jouer_id" , nullable = false)
    private Joueur joueur;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "phrase_mot",
            joinColumns = @JoinColumn(name = "phrase_id") ,
            inverseJoinColumns = @JoinColumn(name = "mot_id"))

    private Set<MotAmbigu> mot_id = new HashSet<>();



    public Phrase(String phrase, Joueur joueur) {
        this.phrase = phrase;
        this.niveau = "niveau A";
        this.joueur = joueur;
        this.nbJouer = 0;
    }

}
