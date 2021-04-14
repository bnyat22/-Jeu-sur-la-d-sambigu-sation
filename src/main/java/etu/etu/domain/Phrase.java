package etu.etu.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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
    private Expert expert;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<MotAmbigu> mot_id = new ArrayList<>();



    public Phrase(String phrase, Expert expert) {
        this.phrase = phrase;
        this.niveau = "niveau A";
        this.expert = expert;
        this.nbJouer = 0;
    }

}
