package com.conducthq.auspost.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.helper.CustomPagerAdapter;
import com.conducthq.auspost.helper.CustomViewPager;
import com.conducthq.auspost.model.data.Achievement;
import com.conducthq.auspost.model.bus.StatusAchievement;

import org.greenrobot.eventbus.EventBus;

public class AchievementActivity extends BaseActivity {

    private CustomViewPager viewPager;
    private CustomPagerAdapter adapter;
    int[] resLayout = new int[]{
            R.layout.page_intro, R.layout.page_info, R.layout.page_quiz, R.layout.page_summary
    };

    private Button nextButton;

    // State progress bar
    private TextView introText;
    private TextView infoText;
    private TextView quizText;
    private TextView summaryText;

    private View introState;
    private View infoState;
    private View quizState;
    private View summaryState;

    private View introToInfoLine;
    private View infoToQuizLine;
    private View quizToSummaryLine;

    private static int BIG = 30;
    private static int SMALL = 20;

    private int stepNumber = 0;

    private boolean secondAccepted = false;
    private boolean quizAccepted = false;
    private boolean quizCompleted = false;

    private EventBus eventBus = EventBus.getDefault();
    private Achievement achievement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        setTopActionBar(R.string.achievement_title, Constants.ARROW, -1);

        achievement = EventBus.getDefault().getStickyEvent(Achievement.class);

        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        viewPager.setPagingEnabled(false);

        adapter = new CustomPagerAdapter(this, resLayout, null);
        viewPager.setAdapter(adapter);

        introText = (TextView) findViewById(R.id.lblIntro);
        introText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepNumber = 0;
                step(stepNumber, false);
            }
        });

        infoText = (TextView) findViewById(R.id.lblInfo);
        infoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secondAccepted) {
                    stepNumber = 1;
                    step(stepNumber, false);
                }
            }
        });

        quizText = (TextView) findViewById(R.id.lblQuiz);
        quizText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizAccepted) {
                    stepNumber = 2;
                    step(stepNumber, false);
                }
            }
        });

        summaryText = (TextView) findViewById(R.id.lblSummary);
        summaryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizCompleted) {
                    stepNumber = 3;
                    step(stepNumber, false);
                }
            }
        });

        introState = findViewById(R.id.stateIntro);
        introState.setLayoutParams(new LinearLayout.LayoutParams(BIG, BIG));
        introState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepNumber = 0;
                step(stepNumber, false);
            }
        });

        infoState = findViewById(R.id.stateInfo);
        infoState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));
        infoState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secondAccepted) {
                    stepNumber = 1;
                    step(stepNumber, false);
                }
            }
        });

        quizState = findViewById(R.id.stateQuiz);
        quizState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));
        quizState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizAccepted) {
                    stepNumber = 2;
                    step(stepNumber, false);
                }
            }
        });

        summaryState = findViewById(R.id.stateSummary);
        summaryState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));
        summaryState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizCompleted) {
                    stepNumber = 3;
                    step(stepNumber, false);
                }
            }
        });

        introToInfoLine = findViewById(R.id.introToInfo);
        infoToQuizLine = findViewById(R.id.infoToQuiz);
        quizToSummaryLine = findViewById(R.id.quizToSummary);

        nextButton = (Button) findViewById(R.id.button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step(stepNumber + 1, true);
            }
        });
        nextButton.setText(R.string.achievement_intro_button);

        if (actionBarBack != null) {
            actionBarBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        //redirect to overview if completed
        if(achievement.getStatus().equals(Constants.ACHIEVEMENT_COMPLETED_STRING)) {
            secondAccepted = true;
            quizAccepted = true;
            quizCompleted = true;
            stepNumber = 3;
            step(stepNumber, false);
        }

    }

    public void step(int stepPosition, boolean button) {
        switch (stepPosition) {
            case 0:
                viewPager.setCurrentItem(stepPosition);
                nextButton.setText(R.string.achievement_intro_button);
                nextButton.setVisibility(View.VISIBLE);

                introText.setTextColor(getResources().getColor(R.color.colorRedAusPost));
                introState.setBackgroundResource(R.drawable.state_selected);
                introState.setLayoutParams(new LinearLayout.LayoutParams(BIG, BIG));

                introToInfoLine.setBackgroundColor(getResources().getColor(R.color.colorGreyDark));

                infoText.setTextColor(getResources().getColor(R.color.colorGreyDark));
                infoState.setBackgroundResource(R.drawable.state_disable);
                infoState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));

                infoToQuizLine.setBackgroundColor(getResources().getColor(R.color.colorGreyDark));

                quizText.setTextColor(getResources().getColor(R.color.colorGreyDark));
                quizState.setBackgroundResource(R.drawable.state_disable);
                quizState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));

                quizToSummaryLine.setBackgroundColor(getResources().getColor(R.color.colorGreyDark));

                summaryText.setTextColor(getResources().getColor(R.color.colorGreyDark));
                summaryState.setBackgroundResource(R.drawable.state_disable);
                summaryState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));
                break;
            case 1:
                viewPager.setCurrentItem(stepPosition);
                nextButton.setText(R.string.achievement_information_button);
                nextButton.setVisibility(View.VISIBLE);

                introText.setTextColor(getResources().getColor(R.color.colorGreyDark));
                introState.setBackgroundResource(R.drawable.state_enable);
                introState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));

                introToInfoLine.setBackgroundColor(getResources().getColor(R.color.colorRedAusPost));

                infoText.setTextColor(getResources().getColor(R.color.colorRedAusPost));
                infoState.setBackgroundResource(R.drawable.state_selected);
                infoState.setLayoutParams(new LinearLayout.LayoutParams(BIG, BIG));

                infoToQuizLine.setBackgroundColor(getResources().getColor(R.color.colorGreyDark));

                quizText.setTextColor(getResources().getColor(R.color.colorGreyDark));
                quizState.setBackgroundResource(R.drawable.state_disable);
                quizState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));

                quizToSummaryLine.setBackgroundColor(getResources().getColor(R.color.colorGreyDark));

                summaryText.setTextColor(getResources().getColor(R.color.colorGreyDark));
                summaryState.setBackgroundResource(R.drawable.state_disable);
                summaryState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));

                if (button) {
                    secondAccepted = true;
                }

                break;
            case 2:
                viewPager.setCurrentItem(stepPosition);
                nextButton.setText(R.string.achievement_quiz_start);
                nextButton.setVisibility(View.VISIBLE);

                introText.setTextColor(getResources().getColor(R.color.colorGreyDark));
                introState.setBackgroundResource(R.drawable.state_enable);
                introState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));

                introToInfoLine.setBackgroundColor(getResources().getColor(R.color.colorRedAusPost));

                infoText.setTextColor(getResources().getColor(R.color.colorGreyDark));
                infoState.setBackgroundResource(R.drawable.state_enable);
                infoState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));

                infoToQuizLine.setBackgroundColor(getResources().getColor(R.color.colorRedAusPost));

                quizText.setTextColor(getResources().getColor(R.color.colorRedAusPost));
                quizState.setBackgroundResource(R.drawable.state_selected);
                quizState.setLayoutParams(new LinearLayout.LayoutParams(BIG, BIG));

                quizToSummaryLine.setBackgroundColor(getResources().getColor(R.color.colorGreyDark));

                summaryText.setTextColor(getResources().getColor(R.color.colorGreyDark));
                summaryState.setBackgroundResource(R.drawable.state_disable);
                summaryState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));

                if (button) {
                    quizAccepted = true;
                }

                break;
            case 3:
                if (button) {
                    startActivityForResult(new Intent(getApplicationContext(), QuizActivity.class), 9001);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
                } else {
                    viewPager.setCurrentItem(stepPosition);
                    nextButton.setVisibility(View.GONE);

                    introText.setTextColor(getResources().getColor(R.color.colorGreyDark));
                    introState.setBackgroundResource(R.drawable.state_enable);
                    introState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));

                    introToInfoLine.setBackgroundColor(getResources().getColor(R.color.colorRedAusPost));

                    infoText.setTextColor(getResources().getColor(R.color.colorGreyDark));
                    infoState.setBackgroundResource(R.drawable.state_enable);
                    infoState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));

                    infoToQuizLine.setBackgroundColor(getResources().getColor(R.color.colorRedAusPost));

                    quizText.setTextColor(getResources().getColor(R.color.colorGreyDark));
                    quizState.setBackgroundResource(R.drawable.state_enable);
                    quizState.setLayoutParams(new LinearLayout.LayoutParams(SMALL, SMALL));

                    quizToSummaryLine.setBackgroundColor(getResources().getColor(R.color.colorRedAusPost));

                    summaryText.setTextColor(getResources().getColor(R.color.colorRedAusPost));
                    summaryState.setBackgroundResource(R.drawable.state_selected);
                    summaryState.setLayoutParams(new LinearLayout.LayoutParams(BIG, BIG));
                }
                break;
        }
        stepNumber = viewPager.getCurrentItem();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001) {
            if (resultCode == RESULT_OK) {
                quizCompleted = true;
                stepNumber = 3;
                step(stepNumber, false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (quizCompleted) {
            StatusAchievement status = new StatusAchievement(achievement.getId(), Constants.ACHIEVEMENT_COMPLETED);
            eventBus.post(status);
        }
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
    }
}
