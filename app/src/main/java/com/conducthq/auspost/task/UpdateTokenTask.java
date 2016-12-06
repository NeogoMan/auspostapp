package com.conducthq.auspost.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.bus.MessageUpdated;
import com.conducthq.auspost.model.data.DeviceToken;
import com.conducthq.auspost.model.data.Message;
import com.conducthq.auspost.model.data.MessageLike;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.model.response.StandardResponse;
import com.conducthq.auspost.service.AusPostApi;
import com.conducthq.auspost.service.RestAdapter;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by conduct19 on 26/10/2016.
 */

public class UpdateTokenTask extends AsyncTask<Void, Void, StandardResponse> {

    private Context mContext;
    private AusPostApi auspostAusPostApi;

    String deviceToken;

    public UpdateTokenTask(Context context, String _deviceToken) {
        mContext = context;
        deviceToken = _deviceToken;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected StandardResponse doInBackground(Void... params) {

        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        String token = prefs.getString(Constants.TOKEN, "");

        if(token.equals("")) {
            return new StandardResponse();
        }

        auspostAusPostApi = RestAdapter.retrofit.create(AusPostApi.class);

        String version = android.os.Build.MODEL + " - " + android.os.Build.VERSION.RELEASE ;

        Call<StandardResponse> call = auspostAusPostApi.UpdateToken(new DeviceToken(deviceToken, version), token);

        try {
            Response<StandardResponse> response = call.execute();
        } catch (NullPointerException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new StandardResponse();
    }

    @Override
    protected void onPostExecute(StandardResponse standardResponse) {

    }

}
