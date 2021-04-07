package etu.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "expert")
@Getter
@Setter
public class Expert extends Intermédiaire{


    private int nbPhrases;
}
