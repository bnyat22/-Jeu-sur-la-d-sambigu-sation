package etu.etu.domain;

import lombok.Data;

@Data
public class Phrase_mot {
    private int phrase_id;
    private int mot_id;

    public Phrase_mot(int phrase_id, int mot_id) {
        this.phrase_id = phrase_id;
        this.mot_id = mot_id;
    }
}
