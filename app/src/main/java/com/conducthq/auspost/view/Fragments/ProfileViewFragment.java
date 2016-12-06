package com.conducthq.auspost.view.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.bus.UpdatedProfile;
import com.conducthq.auspost.model.data.DietaryRequirements;
import com.conducthq.auspost.model.data.Principal;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.task.AsyncContentResponse;
import com.conducthq.auspost.task.ContentTask;
import com.conducthq.auspost.view.EditProfileActivity;
import com.conducthq.auspost.view.MainActivity;
import com.conducthq.auspost.view.ProfileActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class ProfileViewFragment extends Fragment implements AsyncContentResponse {

    private static final String TAG = "ProfileViewFragment";
    public static final String FRAGMENT_TAG = Constants.PROFILE_VIEW_FRAGMENT_TAG;

    ContentResponse content;

    private TextView name;
    private TextView email;
    private TextView phone_number;
    private TextView dietary;
    private ImageView profile_pic;
    private EventBus eventBus = EventBus.getDefault();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_view, container, false);

        eventBus.register(this);
        name = (TextView) view.findViewById(R.id.name);
        email = (TextView) view.findViewById(R.id.email);
        phone_number = (TextView) view.findViewById(R.id.phone_number);
        dietary = (TextView) view.findViewById(R.id.dietary);
        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);

        content = EventBus.getDefault().getStickyEvent(ContentResponse.class);
        if (content != null) {
            populateProfile(content);
        } else {
            ContentTask asyncTask = new ContentTask(getContext());
            asyncTask.delegate = this;
            asyncTask.execute();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    public void populateProfile(ContentResponse content) {
        name.setText(content.getResponse().getPrincipal().getFirstName()+" "+content.getResponse().getPrincipal().getLastName());
        email.setText(content.getResponse().getPrincipal().getEmail());
        phone_number.setText(content.getResponse().getPrincipal().getPhone());

        Glide.with(getContext()).load(content.getResponse().getPrincipal().getImageUrl()).into(profile_pic);

        ArrayList<DietaryRequirements> dietaryReqs = content.getResponse().getDietaryRequirements();

        dietary.setText("N/A");
        for (DietaryRequirements dietaryTmp : dietaryReqs) {
            if(dietaryTmp.getId().equals(content.getResponse().getPrincipal().getDietaryRequirements())) {
                dietary.setText(dietaryTmp.getValue());
                break;
            }
        }
    }

    public void refreshData(Principal principal) {
        name.setText(principal.getFirstName() + " " + principal.getLastName());
        email.setText(principal.getEmail());
        phone_number.setText(principal.getPhone());
    }

    @Override
    public void processFinish(ContentResponse contentResponse) {
        try {
            EventBus.getDefault().postSticky(contentResponse);
            populateProfile(contentResponse);
            content = contentResponse;
        } catch (NullPointerException e) {
            Log.e(TAG, "processFinish: " + e.getMessage());
        }
    }

    @Subscribe()
    public void onUpdatedProfileEvent(UpdatedProfile event) {
        refreshData(event.getPrincipal());
    }

}
