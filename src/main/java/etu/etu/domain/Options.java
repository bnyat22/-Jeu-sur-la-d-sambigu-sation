package etu.etu.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Options {
    public List<Option> optionList;

public boolean isValide;
    public Options() {
        this.optionList = new ArrayList<>();
        this.isValide = false;
    }

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }
}
