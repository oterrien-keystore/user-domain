package com.ote.user.rights.business.common;

import com.ote.user.rights.api.Perimeter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRightTest {

    private String user;
    private String application;
    private final List<Perimeter> perimeters = new ArrayList<>();
}
