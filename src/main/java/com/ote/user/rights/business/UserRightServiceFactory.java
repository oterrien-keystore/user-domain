package com.ote.user.rights.business;

import com.ote.user.rights.api.IUserRightService;
import com.ote.user.rights.spi.IUserRightRepository;

public class UserRightServiceFactory {

    public IUserRightService createService(IUserRightRepository userRightRepository) {
        return new UserRightService(userRightRepository);
    }

}
