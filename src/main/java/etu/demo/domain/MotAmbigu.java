package etu.demo.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="motAmbigu")
public class MotAmbigu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

  private   String mot;
  private String choix1;
  private String choix2;
  private String choix3;
  private String choix4;
}
