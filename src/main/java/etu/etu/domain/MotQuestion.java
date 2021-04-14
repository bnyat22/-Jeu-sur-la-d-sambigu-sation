package etu.etu.domain;

import lombok.Data;

@Data
public class MotQuestion {
    private String mot;
    private String choix1;
    private int point1;
    private String choix2;
    private int point2;
    private String choix3;
    private int point3;
    private String choix4;
    private int point4;


    public MotQuestion() {
    }

    public MotQuestion(String mot, String choix1, int point1, String choix2, int point2) {
        this.mot = mot;
        this.choix1 = choix1;
        this.point1 = point1;
        this.choix2 = choix2;
        this.point2 = point2;
    }

    public MotQuestion(String mot, String choix1, int point1, String choix2, int point2, String choix3, int point3) {
        this.mot = mot;
        this.choix1 = choix1;
        this.point1 = point1;
        this.choix2 = choix2;
        this.point2 = point2;
        this.choix3 = choix3;
        this.point3 = point3;
    }

    public MotQuestion(String mot, String choix1, int point1, String choix2, int point2, String choix3, int point3, String choix4, int point4) {
        this.mot = mot;
        this.choix1 = choix1;
        this.point1 = point1;
        this.choix2 = choix2;
        this.point2 = point2;
        this.choix3 = choix3;
        this.point3 = point3;
        this.choix4 = choix4;
        this.point4 = point4;
    }
}
