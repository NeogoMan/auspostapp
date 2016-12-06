package com.conducthq.auspost.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.helper.MessageViewAdapter;
import com.conducthq.auspost.model.bus.MessageUpdated;
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

public class UnLikeMessageTask extends AsyncTask<Void, Void, StandardResponse> {

    private Context mContext;
    private AusPostApi auspostAusPostApi;

    Integer messageId;
    Message message;

    public UnLikeMessageTask(Context context, Integer _messageId, Message message) {
        mContext = context;
        messageId = _messageId;
        this.message = message;
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

        Call<StandardResponse> call = auspostAusPostApi.UnLikeMessage(token, content.getResponse().getEvent().getId(), messageId);

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
            int likes =  message.getCountMessageLikes();
            if (likes > 0) {
                message.setCountMessageLikes(likes - 1);
            } else {
                message.setCountMessageLikes(0);
            }

            int pos = -1;
            for(int i = 0; i < message.getMessageLikes().size(); i++) {
                if(message.getMessageLikes().get(i).getPrincipalId().equals(message.getPrincipal().getId())) {
                    pos = i;
                    break;
                }
            }

            if (pos != -1) {
                message.getMessageLikes().remove(pos);
            }

            MessageUpdated status = new MessageUpdated(message);
            EventBus.getDefault().post(status);
        }
    }

}
