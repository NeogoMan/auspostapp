package com.conducthq.auspost.task;

import com.conducthq.auspost.model.response.ContentResponse;

/**
 * Created by conduct19 on 26/10/2016.
 */

public interface AsyncContentResponse {
    void processFinish(ContentResponse contentResponse);
}
