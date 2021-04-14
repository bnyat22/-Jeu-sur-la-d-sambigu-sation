package etu.etu.domain;

import lombok.Data;

@Data
public class Profile {
    public long id;
    public String pseudo;
    public String email;
    public int points;
    public int credit;
    public int nPhrase;
    public int nGlose;

    public Profile(long id, String pseudo, String email, int points, int credit, int nPhrase, int nGlose) {
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.points = points;
        this.credit = credit;
        this.nPhrase = nPhrase;
        this.nGlose = nGlose;
    }

    public Profile(long id, String pseudo, String email, int points , int credit) {
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.points = points;
        this.credit = credit;
        this.nGlose =0;
        this.nPhrase = 0;
    }

    public Profile(long id, String pseudo, String email, int points, int credit, int nGlose) {
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.points = points;
        this.credit = credit;
        this.nGlose = nGlose;
    }


    public Profile() {
    }
}
