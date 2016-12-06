package com.conducthq.auspost.view.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.view.BaseActivity;
import com.conducthq.auspost.view.MoreContactActivity;
import com.conducthq.auspost.view.MoreFeedBackActivity;

public class MoreFragment extends Fragment {

    public static final String FRAGMENT_TAG = Constants.MORE_FRAGMENT_TAG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_more, container, false);

        View feedback = view.findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MoreFeedBackActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        View contact = view.findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MoreContactActivity.class);
                i.putExtra(Constants.CONTACT_HELP_TITLE, false);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        return view;
    }

}
