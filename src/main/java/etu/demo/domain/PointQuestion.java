package etu.demo.domain;

import lombok.Data;

@Data
public class PointQuestion {
    private int p1;
    private int p2;
    private int p3;
    private int p4;

    public PointQuestion(int p1, int p2, int p3, int p4) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public PointQuestion() {
    }
}
