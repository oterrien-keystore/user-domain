package com.ote.user.rights.api;

import com.ote.user.rights.business.RightServiceFactory;
import lombok.Getter;

public final class RightServiceProvider {

    @Getter
    private static final RightServiceProvider Instance = new RightServiceProvider();

    @Getter
    private final RightServiceFactory factory;

    private RightServiceProvider() {
        this.factory = new RightServiceFactory();
    }
}
