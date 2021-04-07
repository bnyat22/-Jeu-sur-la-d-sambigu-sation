package etu.demo.response;

import etu.demo.domain.MotAmbigu;
import lombok.Data;

import java.util.List;

@Data
public class QuestionReponse {
    String phrase;
   String niveau;
   String mot;
   String choix1;
   String choix2;
   String choix3;
   String choix4;

}
