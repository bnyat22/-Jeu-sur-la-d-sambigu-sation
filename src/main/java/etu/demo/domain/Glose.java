package etu.demo.domain;

import lombok.Data;

@Data
public class Glose {
    public int id;
    private String phrase;
    private String mot;
    private String glose;
    private int point;

    public Glose() {
    }
}
