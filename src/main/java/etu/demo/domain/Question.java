package etu.demo.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Question {

   public String phrase;
  List<MotQuestion> mots;

   public Question(String phrase, List<MotQuestion> mots) {

      this.phrase = phrase;
      this.mots = mots;

   }

   public Question(List<MotQuestion> mots) {
      this.mots = mots;
   }
    public Question(String phrase) {
        this.phrase = phrase;
    }


    public Question() {
   }
}
