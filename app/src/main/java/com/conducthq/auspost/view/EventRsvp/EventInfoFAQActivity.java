package com.conducthq.auspost.view.EventRsvp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.helper.ExpandableContentView;
import com.conducthq.auspost.model.data.Achievement;
import com.conducthq.auspost.model.data.Event;
import com.conducthq.auspost.model.data.Faq;
import com.conducthq.auspost.view.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class EventInfoFAQActivity extends BaseActivity {

    private Event event;
    private ArrayList<Faq> faqs;
    private LinearLayout wrapperFaq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_info_faq);
        setTopActionBar(R.string.achievement_information_info_faq_title, Constants.ARROW, -1);


        event = EventBus.getDefault().getStickyEvent(Event.class);
        faqs = event.getFaqs();
        wrapperFaq = (LinearLayout) findViewById(R.id.wrapperFaq);


        if(actionBarBack != null) {
            actionBarBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onBackPressed();
                }
            });
        }

        if (faqs != null && !faqs.isEmpty()) {

            ExpandableContentView expandableContentView = new ExpandableContentView(this);
            expandableContentView.setTitle(faqs.get(0).getQuestion());
            expandableContentView.setContent(faqs.get(0).getContent());
            expandableContentView.hideButton();
            expandableContentView.expanded(false);

            wrapperFaq.addView(expandableContentView);

            for (int i = 1; i < faqs.size(); i++) {

                View line = new View(this);
                line.setBackgroundColor(getResources().getColor(R.color.colorEventLine));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        3
                );
                params.setMargins(20, 0, 20, 0);

                line.setLayoutParams(params);
                wrapperFaq.addView(line);

                expandableContentView = new ExpandableContentView(this);
                expandableContentView.setTitle(faqs.get(i).getQuestion());
                expandableContentView.setContent(faqs.get(i).getContent());
                expandableContentView.hideButton();
                expandableContentView.expanded(false);

                wrapperFaq.addView(expandableContentView);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
    }

}
