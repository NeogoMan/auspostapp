package com.conducthq.auspost.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.bus.MessageUpdated;
import com.conducthq.auspost.model.bus.StatusAchievement;
import com.conducthq.auspost.model.data.Achievement;
import com.conducthq.auspost.model.data.AchievementLock;
import com.conducthq.auspost.model.data.Message;
import com.conducthq.auspost.model.data.MessageLike;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.model.response.StandardResponse;
import com.conducthq.auspost.service.AusPostApi;
import com.conducthq.auspost.service.RestAdapter;
import com.conducthq.auspost.view.ScanCodeActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by conduct19 on 26/10/2016.
 */

public class UnlockAchievementTask extends AsyncTask<Void, Void, StandardResponse> {

    private Achievement achievement;
    private AVLoadingIndicatorView progress;
    private Context mContext;
    private AusPostApi auspostAusPostApi;
    private AchievementLock lock;

    public AsyncUnlockAchievementResponse delegate = null;

    Integer principalId;

    public UnlockAchievementTask(Context context,
                                 Integer id,
                                 Achievement achievement,
                                 AVLoadingIndicatorView progress, AchievementLock lock) {
        mContext = context;
        principalId = id;
        this.achievement = achievement;
        this.progress = progress;
        this.lock = lock;
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

        Call<StandardResponse> call = auspostAusPostApi.UnlockAchievement(token, content.getResponse().getEvent().getId(), lock);

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
            StatusAchievement status = new StatusAchievement(achievement.getId(), Constants.ACHIEVEMENT_UNLOCKED);
            EventBus.getDefault().post(status);
            progress.smoothToHide();
            delegate.processFinish();
        }
    }

}
