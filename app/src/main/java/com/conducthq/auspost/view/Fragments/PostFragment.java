package com.conducthq.auspost.view.Fragments;

import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.request.RequestPost;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.model.response.StandardResponse;
import com.conducthq.auspost.service.AusPostApi;
import com.conducthq.auspost.service.RestAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by conduct19 on 4/11/2016.
 */

public class PostFragment extends DialogFragment {

    TextView btn_post;
    TextView edit_text;
    public AVLoadingIndicatorView mProgress;
    private SharedPreferences prefs;
    protected AusPostApi auspostAusPostApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.post_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        auspostAusPostApi = RestAdapter.retrofit.create(AusPostApi.class);

        prefs = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        btn_post = (TextView) rootView.findViewById(R.id.btn_post);
        edit_text = (TextView) rootView.findViewById(R.id.edit_text);
        mProgress = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi_loader);

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ed_text = edit_text.getText().toString().trim();

                if (ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("")) {
                    Toast.makeText(getActivity(), R.string.post_empty_field, Toast.LENGTH_SHORT).show();
                    return;
                }

                mProgress.smoothToShow();
                String token = prefs.getString(Constants.TOKEN, "");

                ContentResponse content = EventBus.getDefault().getStickyEvent(ContentResponse.class);

                final Call<StandardResponse> call = auspostAusPostApi.PostMessage(token, new RequestPost(edit_text.getText().toString()), content.getResponse().getEvent().getId());
                call.enqueue(new Callback<StandardResponse>() {
                    @Override
                    public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                        mProgress.smoothToHide();
                        final StandardResponse result = response.body();
                        if (response.isSuccessful()) {
                            ((DashboardFragment) getActivity().getSupportFragmentManager()
                                    .findFragmentByTag(DashboardFragment.FRAGMENT_TAG)).refreshItems();
                            dismiss();
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
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
