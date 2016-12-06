package com.conducthq.auspost.view.EventRsvp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.helper.ExpandableContentView;
import com.conducthq.auspost.helper.Utils;
import com.conducthq.auspost.model.data.Event;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.task.AsyncContentResponse;
import com.conducthq.auspost.task.ContentTask;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by conduct19 on 8/11/2016.
 */

public class EventInfoFragment extends Fragment implements AsyncContentResponse {

    private static final String TAG = "EventInfoFragment";
    private Button mAddToCalendar;
    private ExpandableContentView mEventInfo;
    private ExpandableContentView mGettingThere;
    private ExpandableContentView mBring;
    private ExpandableContentView mAccommodation;
    private View mFaq;
    private ImageView mImageEvent;
    private TextView txt_day;
    private TextView txt_month;
    private TextView txt_time_name;
    private TextView txt_calendar_name;
    private TextView txt_event_name;
    private TextView txt_location_name;

    public static final String FRAGMENT_TAG = Constants.EVENT_INFO_FRAGMENT_TAG;

    ContentResponse content;
    private Event event;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_info, container, false);

        mImageEvent = (ImageView) view.findViewById(R.id.imageEvent);
        txt_day = (TextView) view.findViewById(R.id.txt_day);
        txt_month = (TextView) view.findViewById(R.id.txt_month);
        txt_time_name = (TextView) view.findViewById(R.id.txt_time_name);
        txt_calendar_name = (TextView) view.findViewById(R.id.txt_calendar_name);
        txt_event_name = (TextView) view.findViewById(R.id.txt_event_name);
        txt_location_name = (TextView) view.findViewById(R.id.txt_location_name);

        mAddToCalendar = (Button) view.findViewById(R.id.btn_add_calendar);
        mAddToCalendar.setText(R.string.event_info_add_calendar);

        mEventInfo = (ExpandableContentView) view.findViewById(R.id.eventInformation);
        mEventInfo.setTitle(R.string.event_info_event_information);

        mGettingThere = (ExpandableContentView) view.findViewById(R.id.gettingThere);
        mGettingThere.setTitle(R.string.event_info_getting_there);

        mBring = (ExpandableContentView) view.findViewById(R.id.bring);
        mBring.setTitle(R.string.event_info_what_to_bring);

        mAccommodation = (ExpandableContentView) view.findViewById(R.id.accommodation);
        mAccommodation.setTitle(R.string.event_info_accommodation);

        mFaq = view.findViewById(R.id.faq);
        mFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event != null) {
                    EventBus.getDefault().postSticky(event);
                    startActivity(new Intent(getActivity(), EventInfoFAQActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }

            }
        });

        content = EventBus.getDefault().getStickyEvent(ContentResponse.class);
        if (content != null) {
            populateEvent(content);
        } else {
            ContentTask asyncTask = new ContentTask(getContext());
            asyncTask.delegate = this;
            asyncTask.execute();
        }

        mAddToCalendar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(content != null) {

                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date dateStart = format.parse(content.getResponse().getEvent().getStart());
                        Date dateEnd = format.parse(content.getResponse().getEvent().getEnd());

                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, dateStart.getTime());
                        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, dateEnd.getTime());
                        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                        intent.putExtra(CalendarContract.Events.TITLE, content.getResponse().getEvent().getName());
                        intent.putExtra(CalendarContract.Events.DESCRIPTION, content.getResponse().getEvent().getInformation());
                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, content.getResponse().getEvent().getLocation());
                        startActivity(intent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return view;
    }

    public void populateEvent(ContentResponse content) {

        event = content.getResponse().getEvent();
        mEventInfo.setContent(content.getResponse().getEvent().getInformation());
        mEventInfo.setLinkName(content.getResponse().getEvent().getInformationUrlTitle());
        mEventInfo.setUrl(content.getResponse().getEvent().getInformationUrl());
        mEventInfo.expanded(false);

        mGettingThere.setContent(content.getResponse().getEvent().getGettingThere());
        mGettingThere.setLinkName(content.getResponse().getEvent().getGettingThereUrlTitle());
        mGettingThere.setUrl(content.getResponse().getEvent().getGettingThereUrl());
        mGettingThere.expanded(false);

        mBring.setContent(content.getResponse().getEvent().getWhatToBring());
        mBring.setLinkName(content.getResponse().getEvent().getWhatToBringUrlTitle());
        mBring.setUrl(content.getResponse().getEvent().getWhatToBringUrl());
        mBring.expanded(false);

        mAccommodation.setContent(content.getResponse().getEvent().getAccommodation());
        mAccommodation.setLinkName(content.getResponse().getEvent().getAccommodationUrlTitle());
        mAccommodation.setUrl(content.getResponse().getEvent().getAccommodationUrl());
        mAccommodation.expanded(false);

        Glide.with(getContext()).load(content.getResponse().getEvent().getImageUrl()).centerCrop().into(mImageEvent);

        txt_event_name.setText(content.getResponse().getEvent().getName());
        txt_location_name.setText(content.getResponse().getEvent().getLocationName());

        String dtStart = content.getResponse().getEvent().getStart();
        String dtEnd = content.getResponse().getEvent().getEnd();
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

            txt_time_name.setText(initialHour + " - " + finalHour.toLowerCase());
            txt_calendar_name.setText(finalDay + " " + finalMonth + " " + finalYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(ContentResponse contentResponse) {
        try {
            EventBus.getDefault().postSticky(contentResponse);
            populateEvent(contentResponse);
            content = contentResponse;
        } catch (NullPointerException e) {
            Log.e(TAG, "processFinish: " + e.getMessage());
        }
    }

}
