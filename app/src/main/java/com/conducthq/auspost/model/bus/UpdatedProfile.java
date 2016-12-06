package com.conducthq.auspost.model.bus;

import com.conducthq.auspost.model.data.Principal;

public class UpdatedProfile {

    Principal principal;

    public UpdatedProfile(Principal principal) {
        this.principal = principal;
    }

    public Principal getPrincipal() {
        return principal;
    }
}
