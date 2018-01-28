package com.ote.user.rights.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Privilege {

    @Getter
    private final String code;

    private final Privilege parent;

    public Privilege(String code) {
        this(code, null);
    }

    public boolean isDefined(String code) {
        boolean result = this.code.equalsIgnoreCase(code);
        if (!result && this.parent != null) {
            result = this.parent.isDefined(code);
        }
        return result;
    }

    /*public boolean isSameHierarchy(Privilege other){
        return isDefined(other.getCode()) || other.isDefined(code);
    }*/
}
