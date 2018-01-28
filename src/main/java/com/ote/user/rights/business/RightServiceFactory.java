package com.ote.user.rights.business;

import com.ote.user.rights.api.IRightCheckerService;
import com.ote.user.rights.spi.IRightCheckerRepository;

public class RightServiceFactory {

    public IRightCheckerService createService(IRightCheckerRepository userRightRepository) {
        return new RightCheckerService(userRightRepository);
    }

}
