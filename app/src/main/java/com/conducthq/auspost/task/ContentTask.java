package com.conducthq.auspost.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.service.AusPostApi;
import com.conducthq.auspost.service.RestAdapter;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by conduct19 on 26/10/2016.
 */

public class ContentTask extends AsyncTask<Void, Void, ContentResponse> {

    private Context mContext;
    private AusPostApi auspostAusPostApi;
    public AsyncContentResponse delegate = null;

    public ContentTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected ContentResponse doInBackground(Void... params) {

        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        String token = prefs.getString(Constants.TOKEN, "");
        auspostAusPostApi = RestAdapter.retrofit.create(AusPostApi.class);

        Call<ContentResponse> call = auspostAusPostApi.GetContent(token);

        try {
            Response<ContentResponse> response =  call.execute();
            return response.body();
        } catch (NullPointerException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ContentResponse();

    }

    @Override
    protected void onPostExecute(ContentResponse contentResponse) {
        delegate.processFinish(contentResponse);
    }

}
