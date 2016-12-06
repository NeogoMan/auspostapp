package com.conducthq.auspost.model.response;

import com.conducthq.auspost.model.data.Principal;
import java.io.Serializable;

/**
 * Created by conduct19 on 21/10/2016.
 */

public class PrincipalUpdateResponse implements Serializable {

    Principal data;
    boolean success;
    boolean error;

    public boolean isSuccess() {
        return success;
    }

    public Principal getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }
}
