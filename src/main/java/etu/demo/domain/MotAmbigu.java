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

}
