package etu.demo.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="motAmbigu")
@NoArgsConstructor
public class MotAmbigu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

  private String mot;
  private String choix1;
  private String choix2;
  private String choix3;
  private String choix4;


  public MotAmbigu(String mot , String choix1) {
    this.mot = mot;
    this.choix1 = choix1;
  }
  public MotAmbigu(String mot , String choix1 , String choix2) {
    this.mot = mot;
    this.choix1 = choix1;
    this.choix2 = choix2;
  }
  public MotAmbigu(String mot , String choix1 , String choix2 ,String choix3) {
    this.mot = mot;
    this.choix1 = choix1;
    this.choix2 = choix2;
    this.choix3 = choix3;
  }
  public MotAmbigu(String mot , String choix1 , String choix2 ,String choix3,String choix4) {
    this.mot = mot;
    this.choix1 = choix1;
    this.choix2 = choix2;
    this.choix3 = choix3;
    this.choix4 = choix4;
  }

}
