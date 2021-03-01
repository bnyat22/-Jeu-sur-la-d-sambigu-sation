package etu.demo.domain;

import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "points")
@Getter
@Setter
public class Points {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "phrase_id", nullable = false)
    private Phrase phrase;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mot_id" ,nullable = false)
    private MotAmbigu motAmbigu_id;

    private int point_choix1;
    private int nJouer_choix1;

    private int point_choix2;
    private int nJouer_choix2;

    private int point_choix3;
    private int nJouer_choix3;

    private int point_choix4;
    private int nJouer_choix4;

    public Points() {
    }
    public Points(Phrase phrase , MotAmbigu motAmbigu , int point_choix1) {
        this.phrase = phrase;
        motAmbigu_id = motAmbigu;
        this.point_choix1  = point_choix1;
        this.nJouer_choix1 = 0;
    }

}
