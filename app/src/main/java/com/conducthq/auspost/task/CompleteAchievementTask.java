package com.conducthq.auspost.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.bus.StatusAchievement;
import com.conducthq.auspost.model.data.Achievement;
import com.conducthq.auspost.model.data.AchievementComplete;
import com.conducthq.auspost.model.data.AchievementLock;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.model.response.StandardResponse;
import com.conducthq.auspost.service.AusPostApi;
import com.conducthq.auspost.service.RestAdapter;
import com.conducthq.auspost.view.QuizActivity;
import com.conducthq.auspost.view.ScanCodeActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by conduct19 on 26/10/2016.
 */

public class CompleteAchievementTask extends AsyncTask<Void, Void, StandardResponse> {

    private Achievement achievement;
    private QuizActivity mContext;
    private AusPostApi auspostAusPostApi;
    private AchievementComplete complete;

    Integer principalId;

    public CompleteAchievementTask(QuizActivity context,
                                   Integer id,
                                   Achievement achievement, AchievementComplete complete) {
        mContext = context;
        principalId = id;
        this.achievement = achievement;
        this.complete = complete;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected StandardResponse doInBackground(Void... params) {

        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        String token = prefs.getString(Constants.TOKEN, "");
        auspostAusPostApi = RestAdapter.retrofit.create(AusPostApi.class);

        ContentResponse content = EventBus.getDefault().getStickyEvent(ContentResponse.class);

        Call<StandardResponse> call = auspostAusPostApi.CompleteAchievement(token, content.getResponse().getEvent().getId(), achievement.getId(), complete);

        try {
            Response<StandardResponse> response = call.execute();
            return response.body();
        } catch (NullPointerException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new StandardResponse();

    }

    @Override
    protected void onPostExecute(StandardResponse contentResponse) {
        if (contentResponse.getSuccess()) {
            EventBus.getDefault().postSticky(achievement);
            StatusAchievement status = new StatusAchievement(achievement.getId(), Constants.ACHIEVEMENT_COMPLETED);
            EventBus.getDefault().post(status);
            mContext.onBackPressed();
        }
    }

}
