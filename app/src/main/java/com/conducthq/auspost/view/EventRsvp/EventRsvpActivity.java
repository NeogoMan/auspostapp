package com.conducthq.auspost.view.EventRsvp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.helper.Utils;
import com.conducthq.auspost.model.request.RequestPrincipalEvent;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.model.response.StandardResponse;
import com.conducthq.auspost.task.AsyncContentResponse;
import com.conducthq.auspost.task.ContentTask;
import com.conducthq.auspost.view.BaseActivity;
import com.conducthq.auspost.view.MainActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by conduct19 on 25/10/2016.
 */

public class EventRsvpActivity extends BaseActivity implements AsyncContentResponse {

    private Button mSubmitButton;
    private Button mBtn_attend;
    public AVLoadingIndicatorView mProgress;

    private TextView txt_title;
    private TextView txt_day;
    private TextView txt_month;
    private TextView txt_event_name;
    private TextView txt_date;
    private TextView txt_location_name;
    private TextView txt_map;
    private ImageView imageEvent;
    private ImageView profilePic;
    private TextView txt_cant_attend;

    String lat;
    String lon;

    ContentResponse content;

    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_rsvp);

        mProgress = (AVLoadingIndicatorView) findViewById(R.id.avi_loader);
        mSubmitButton = (Button) findViewById(R.id.button);
        mSubmitButton.setText(R.string.event_rsvp_button);

        mBtn_attend = (Button) findViewById(R.id.btn_attend);

        prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_day = (TextView) findViewById(R.id.txt_day);
        txt_month = (TextView) findViewById(R.id.txt_month);
        txt_event_name = (TextView) findViewById(R.id.txt_event_name);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_location_name = (TextView) findViewById(R.id.txt_location_name);
        txt_map = (TextView) findViewById(R.id.txt_map);
        imageEvent = (ImageView) findViewById(R.id.imageEvent);
        profilePic = (ImageView) findViewById(R.id.profilePic);
        txt_cant_attend = (TextView) findViewById(R.id.txt_cant_attend);

        content = EventBus.getDefault().getStickyEvent(ContentResponse.class);
        if (content != null) {
            populateEvent(content);
        } else {
            ContentTask asyncTask = new ContentTask(getApplicationContext());
            asyncTask.delegate = this;
            asyncTask.execute();
        }

        txt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplication(), MapActivity.class);
                Bundle extras = new Bundle();
                extras.putString(Constants.MAP_LATITUDE, lat);
                extras.putString(Constants.MAP_LONGITUDE, lon);
                i.putExtras(extras);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String token = prefs.getString(Constants.TOKEN, "");

                mProgress.smoothToShow();

                final Call<StandardResponse> call = auspostAusPostApi.UpdatePrincipalEvent(new RequestPrincipalEvent(true, null), content.getResponse().getPrincipal().getId(), content.getResponse().getEvent().getId(), token);

                call.enqueue(new Callback<StandardResponse>() {
                    @Override
                    public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                        mProgress.smoothToHide();
                        final StandardResponse standardResponse = response.body();
                        if (response.isSuccessful()) {
                            mSubmitButton.setVisibility(View.GONE);
                            mBtn_attend.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(getApplication(), MainActivity.class);
                                    startActivity(i);
                                    overridePendingTransition(0, 0);
                                }
                            }, 400);
                        }
                    }
                    @Override
                    public void onFailure(Call<StandardResponse> call, Throwable t) {
                        mProgress.smoothToHide();
                        String Response = t.getMessage();
                    }
                });
            }
        });

        txt_cant_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplication(), NoticeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

    }

    public void populateEvent(ContentResponse contentResponse) {
        Resources res = getResources();
        txt_title.setText(String.format(res.getString(R.string.event_rsvp_name), contentResponse.getResponse().getPrincipal().getFirstName()));
        txt_event_name.setText(contentResponse.getResponse().getEvent().getName());

        txt_location_name.setText(contentResponse.getResponse().getEvent().getLocationName());

        String dtStart = contentResponse.getResponse().getEvent().getStart();
        String dtEnd = contentResponse.getResponse().getEvent().getEnd();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        try {
            Date dateStart = format.parse(dtStart);
            Date dateEnd = format.parse(dtEnd);

            txt_day.setText(android.text.format.DateFormat.format("dd", dateStart));
            txt_month.setText(android.text.format.DateFormat.format("MMM", dateStart));

            String initialHour = (String) android.text.format.DateFormat.format("h:mm", dateStart);
            String finalHour = (String) android.text.format.DateFormat.format("h:mm a", dateEnd);
            String finalDay = Utils.ordinal(Integer.parseInt((String) android.text.format.DateFormat.format("d", dateEnd)));
            String finalMonth = (String) android.text.format.DateFormat.format("MMMM", dateEnd);
            String finalYear = (String) android.text.format.DateFormat.format("yyyy", dateEnd);

            txt_date.setText(initialHour + " - " + finalHour.toLowerCase() + ", " + finalDay + " " + finalMonth + " " + finalYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Glide.with(getApplicationContext()).load(contentResponse.getResponse().getEvent().getImageUrl()).override(342, 160).centerCrop().into(imageEvent);
        Glide.with(getApplicationContext()).load(contentResponse.getResponse().getPrincipal().getImageUrl()).into(profilePic);

        lat = contentResponse.getResponse().getEvent().getLat();
        lon = contentResponse.getResponse().getEvent().getLon();
    }

    @Override
    public void processFinish(ContentResponse contentResponse) {
        EventBus.getDefault().postSticky(contentResponse);
        populateEvent(contentResponse);
    }

}
