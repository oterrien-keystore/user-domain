package com.ote.user.rights.api;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class Perimeter {

    private final String code;
    private final List<Perimeter> perimeters = new ArrayList<>();
    private final List<String> privileges = new ArrayList<>();
    //private final boolean isAll;

    public Perimeter(String code) {
        this.code = code;
       // isAll = code.endsWith("*");
    }


}
