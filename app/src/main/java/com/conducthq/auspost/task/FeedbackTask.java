package com.conducthq.auspost.task;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.bus.DashboardResume;
import com.conducthq.auspost.model.data.Feedback;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.model.response.FeedbackResponse;
import com.conducthq.auspost.service.AusPostApi;
import com.conducthq.auspost.service.RestAdapter;
import com.conducthq.auspost.view.MainActivity;
import com.conducthq.auspost.view.MoreFeedBackActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by conduct19 on 26/10/2016.
 */

public class FeedbackTask extends AsyncTask<Void, Void, FeedbackResponse> {

    private Context mContext;
    private AusPostApi auspostAusPostApi;
    private MoreFeedBackActivity feedbackActivity;

    Integer eventId;
    Feedback feedback;

    public FeedbackTask(Context context, Integer eventId, Feedback feedback) {
        mContext = context;
        this.eventId = eventId;
        this.feedback = feedback;
        feedbackActivity = (MoreFeedBackActivity) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected FeedbackResponse doInBackground(Void... params) {

        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        String token = prefs.getString(Constants.TOKEN, "");
        auspostAusPostApi = RestAdapter.retrofit.create(AusPostApi.class);

        ContentResponse content = EventBus.getDefault().getStickyEvent(ContentResponse.class);

        Call<FeedbackResponse> call = auspostAusPostApi.Feedback(token, content.getResponse().getEvent().getId(), feedback);

        try {
            Response<FeedbackResponse> response = call.execute();
            return response.body();
        } catch (NullPointerException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FeedbackResponse();

    }

    @Override
    protected void onPostExecute(FeedbackResponse contentResponse) {
        singleDialog();
    }

    public void singleDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setMessage(R.string.feedback_completed);
        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.create().show();
    }
}
