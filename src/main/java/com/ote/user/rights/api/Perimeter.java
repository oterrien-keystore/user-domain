package com.ote.user.rights.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = "code")
@RequiredArgsConstructor
public class Perimeter {

    private final String code;

    private final Set<String> privileges = new HashSet<>();

    public Path getPath() {
        return new Path.Parser(code).get();
    }
}
