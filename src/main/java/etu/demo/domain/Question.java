package etu.demo.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Question {
   private int id;
   private String phrase;
   private String mot ;
   private String choix1;
   private String choix2;
   private String choix3;
   private String choix4;

   public Question(int id, String phrase, String mot, String choix1,
                   String choix2, String choix3, String choix4) {
      this.id = id;
      this.phrase = phrase;
      this.mot = mot;
      this.choix1 = choix1;
      this.choix2 = choix2;
      this.choix3 = choix3;
      this.choix4 = choix4;
   }
}
