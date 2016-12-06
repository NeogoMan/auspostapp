package com.conducthq.auspost.view.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.helper.MessageViewAdapter;
import com.conducthq.auspost.model.bus.MessageUpdated;
import com.conducthq.auspost.model.bus.StatusAchievement;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.model.response.MessageResponse;
import com.conducthq.auspost.service.AusPostApi;
import com.conducthq.auspost.service.RestAdapter;
import com.conducthq.auspost.task.AsyncContentResponse;
import com.conducthq.auspost.task.ContentTask;
import com.conducthq.auspost.view.PinActivity;
import com.conducthq.auspost.view.ProfileActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by conduct19 on 28/10/2016.
 */

public class DashboardFragment extends Fragment implements AsyncContentResponse {

    SwipeRefreshLayout swipeContainer;
    RecyclerView recyclerView;
    private SharedPreferences prefs;
    FloatingActionButton buttonNewMessage;
    protected AusPostApi auspostAusPostApi;
    MessageViewAdapter adapter;
    EventBus eventBus = EventBus.getDefault();
    ContentResponse content;

    public static final String FRAGMENT_TAG = Constants.DASHBOARD_FRAGMENT_TAG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        eventBus.register(this);
        content = eventBus.getStickyEvent(ContentResponse.class);

        auspostAusPostApi = RestAdapter.retrofit.create(AusPostApi.class);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.colorRedAusPost);
        buttonNewMessage = (FloatingActionButton) view.findViewById(R.id.button_new_message);

        buttonNewMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                PostFragment dialogFragment = new PostFragment();
                dialogFragment.show(fm, Constants.POST_FRAGMENT_TAG);
            }
        });

        prefs =  getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);

        refreshItems();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        return view;

    }

    public void refreshItems() {

        String token = prefs.getString(Constants.TOKEN, "");
        if(token.equals("")){
            Intent i = new Intent(getActivity(), PinActivity.class);
            startActivity(i);
            getActivity().overridePendingTransition(0, 0);
            getActivity().finish();
            return;
        }

        swipeContainer.setRefreshing(true);
        if (content == null) {
            ContentTask asyncTask = new ContentTask(getContext());
            asyncTask.delegate = this;
            asyncTask.execute();
            return;
        }

        final Call<MessageResponse> call = auspostAusPostApi.GetMessages(token, content.getResponse().getEvent().getId());
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                final MessageResponse messageResponse = response.body();
                if (response.isSuccessful()) {
                    adapter = new MessageViewAdapter(messageResponse.getResponse().getItems(), getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    onItemsLoadComplete();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                onItemsLoadComplete();
                Toast.makeText(getContext(), R.string.message_error_fetch, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    void onItemsLoadComplete() {
        swipeContainer.setRefreshing(false);
    }

    @Subscribe()
    public void onMessageUpdatedEvent(MessageUpdated event) {
        if (adapter != null) {
            adapter.update(event.getMessage());
        }
    }

    @Override
    public void processFinish(ContentResponse contentResponse) {
        content = contentResponse;
        refreshItems();
    }
}
