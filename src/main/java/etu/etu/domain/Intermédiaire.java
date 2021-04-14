package etu.etu.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "intermidaire")
@Getter
@Setter
public class Intermédiaire extends Joueur{

int nbLike;
private int nbGloses;

    public Intermédiaire(Utilisateur joueur) {
        super(joueur);
    }

    public Intermédiaire() {

    }
}
