package etu.etu.domain;

import lombok.Data;

import java.util.List;

@Data
public class ChoixReponse {
   public int id;
    public String phrase;
    public String mot;
    public int mot_id;
    public List<String> choix;

}
