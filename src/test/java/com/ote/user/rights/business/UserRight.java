package com.ote.user.rights.business;

import com.ote.user.rights.api.Perimeter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class UserRight {

    private final String user;
    private final String application;
    private final List<Perimeter> perimeters = new ArrayList<>();
}
