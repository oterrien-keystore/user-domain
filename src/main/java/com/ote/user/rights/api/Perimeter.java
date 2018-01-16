package com.ote.user.rights.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "code")
public class Perimeter {

    private final String code;
    private final Set<Perimeter> perimeters = new HashSet<>();
    private final Set<String> privileges = new HashSet<>();
    //private final boolean isAll;

    public Perimeter(String code) {
        this.code = code;
        // isAll = code.endsWith("*");
    }


}
