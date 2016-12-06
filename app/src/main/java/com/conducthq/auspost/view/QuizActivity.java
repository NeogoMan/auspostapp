package com.conducthq.auspost.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.helper.CustomPagerAdapter;
import com.conducthq.auspost.helper.CustomViewPager;
import com.conducthq.auspost.model.data.Achievement;
import com.conducthq.auspost.model.data.AchievementComplete;
import com.conducthq.auspost.model.data.Question;
import com.conducthq.auspost.model.data.QuestionOption;
import com.conducthq.auspost.model.data.Quiz;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.task.CompleteAchievementTask;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends BaseActivity {

    private static final String TAG = "QuizActivity";
    private CustomViewPager viewPager;
    private CustomPagerAdapter adapter;
    private View quizCompleted;

    private Button nextButton;
    private TextView questionTotal;
    private TextView questionTitle;

    private Achievement achievement;

    private boolean isCompleted = false;
    ContentResponse content;
    private ArrayList<Question> questions;
    private List<View> views = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setTopActionBar(R.string.quiz_title, -1, Constants.BTN_X);

        quizCompleted = findViewById(R.id.quizCompleted);
        achievement = EventBus.getDefault().getStickyEvent(Achievement.class);

        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        viewPager.setPagingEnabled(false);

        nextButton = (Button) findViewById(R.id.btn_swipe);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeButton();
            }
        });

        questionTitle = (TextView) findViewById(R.id.questionTitle);
        questionTotal = (TextView) findViewById(R.id.questionTotal);

        populateQuizzes();

        if (actionBarClose != null) {
            actionBarClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

    }

    public void swipeButton() {
        if (!isCompleted) {
            if (viewPager.getCurrentItem() < questions.size()) {
                RadioGroup groupQuestion = (RadioGroup) views.get(viewPager.getCurrentItem()).findViewById(R.id.groupQuestion);
                if (groupQuestion.getCheckedRadioButtonId() != -1) {
                    if (viewPager.getCurrentItem() < questions.size()) {
                        int selectedOption = groupQuestion.getCheckedRadioButtonId();

                        for (QuestionOption option : questions.get(viewPager.getCurrentItem()).getOptions()) {
                            if (selectedOption == option.getId()) {
                                if (option.getCorrect()) {
                                    View view = getLayoutInflater().inflate(R.layout.custom_dialog_correct, null);
                                    final Button close = (Button) view.findViewById(R.id.btn_close);
                                    TextView feedbackText = (TextView) view.findViewById(R.id.feedback_response);
                                    feedbackText.setText(questions.get(viewPager.getCurrentItem()).getCorrectFeedback());
                                    AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
                                    mDialog.setView(view);
                                    mDialog.setCancelable(true);
                                    final AlertDialog dialog = mDialog.create();
                                    dialog.show();

                                    close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (dialog != null) {
                                                dialog.dismiss();

                                                try {
                                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                                                    questionTitle.setText(questions.get(viewPager.getCurrentItem()).getContent());
                                                    questionTotal.setText(String.format(getString(R.string.quiz_question),
                                                            viewPager.getCurrentItem() + 1, questions.size()));

                                                } catch (IndexOutOfBoundsException e) {
                                                    // QUIZ is completed
                                                    Log.e(TAG, "nextQuestion: " + e.getMessage());
                                                }
                                                nextButton.setBackgroundResource(R.drawable.background_button_red_disable);


                                                if (viewPager.getCurrentItem() == questions.size()) {
                                                    quizCompleted.setVisibility(View.VISIBLE);
                                                    nextButton.setText("OK");
                                                    nextButton.setBackgroundResource(R.drawable.background_button_red);
                                                    isCompleted = true;
                                                }
                                            }
                                        }
                                    });

                                } else {

                                    View view = getLayoutInflater().inflate(R.layout.custom_dialog_incorrect, null);
                                    final Button close = (Button) view.findViewById(R.id.btn_close);
                                    TextView feedbackText = (TextView) view.findViewById(R.id.feedback_response);
                                    feedbackText.setText(questions.get(viewPager.getCurrentItem()).getIncorrectFeedback());
                                    AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
                                    mDialog.setView(view);
                                    mDialog.setCancelable(true);
                                    final AlertDialog dialog = mDialog.create();
                                    dialog.show();

                                    close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (dialog != null) {
                                                dialog.dismiss();
                                            }
                                        }
                                    });

                                }
                            }
                        }

                    }
                }
            }
        } else {
            // Completed
            AchievementComplete complete = new AchievementComplete();
            complete.setEventId(16);
            complete.setAchievementId(achievement.getId());
            new CompleteAchievementTask(this, 23, achievement, complete).execute();
        }
    }

    @Override
    public void onBackPressed() {
        if (isCompleted) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }

        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_in_down);
    }

    public void populateQuizzes() {
        Quiz quiz = achievement.getQuiz();
        questions = quiz.getQuestions();
        questionTitle.setText(questions.get(viewPager.getCurrentItem()).getContent());
        questionTotal.setText(String.format(getString(R.string.quiz_question), viewPager.getCurrentItem() + 1, questions.size()));


        for (int i = 0; i < questions.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.page_quiz_question, null);
            RadioGroup groupQuestion = (RadioGroup) view.findViewById(R.id.groupQuestion);
            groupQuestion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (radioGroup.getCheckedRadioButtonId() != -1) {
                        nextButton.setBackgroundResource(R.drawable.background_button_red);
                    } else {
                        nextButton.setBackgroundResource(R.drawable.background_button_red_disable);
                    }
                }
            });

            RadioButton questionOne = (RadioButton) view.findViewById(R.id.questionOne);
            RadioButton questionTwo = (RadioButton) view.findViewById(R.id.questionTwo);
            RadioButton questionThree = (RadioButton) view.findViewById(R.id.questionThree);

            questionOne.setId(questions.get(i).getOptions().get(0).getId());
            questionOne.setText(questions.get(i).getOptions().get(0).getContent());
            questionTwo.setId(questions.get(i).getOptions().get(1).getId());
            questionTwo.setText(questions.get(i).getOptions().get(1).getContent());
            questionThree.setId(questions.get(i).getOptions().get(2).getId());
            questionThree.setText(questions.get(i).getOptions().get(2).getContent());

            views.add(view);
        }


        // Empty
        View view = new View(this);
        views.add(view);

        adapter = new CustomPagerAdapter(this, null, views);
        viewPager.setAdapter(adapter);
    }

}
