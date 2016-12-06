package com.conducthq.auspost.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.conducthq.auspost.R;

import com.conducthq.auspost.model.data.Achievement;
import com.conducthq.auspost.view.AchievementInfoFAQActivity;
import com.conducthq.auspost.view.AchievementInfoKeyActivity;
import com.conducthq.auspost.view.AchievementInfoMaterialActivity;
import com.conducthq.auspost.view.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int[] resLayout;
    private List<View> views;
    private Achievement achievement;
    private BaseActivity baseActivity;

    public CustomPagerAdapter(Context context, int[] resLayout, List<View> views) {
        mContext = context;
        baseActivity = (BaseActivity) context;
        this.resLayout = resLayout;
        this.views = views;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        if (views == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);

            achievement = EventBus.getDefault().getStickyEvent(Achievement.class);
            ViewGroup layout = (ViewGroup) inflater.inflate(resLayout[position], collection, false);

            switch (resLayout[position]) {
                case R.layout.page_intro:
                    TextView achievementIntro = (TextView) layout.findViewById(R.id.text_intro);
                    TextView achievementTitle = (TextView) layout.findViewById(R.id.achievementTitle);
                    TextView achievementStatus = (TextView) layout.findViewById(R.id.txt_achievement_status);
                    ImageView achievementPic = (ImageView) layout.findViewById(R.id.achievementPic);
                    ImageView status = (ImageView) layout.findViewById(R.id.status);

                    achievementIntro.setText(achievement.getDescription());
                    achievementTitle.setTextSize(24);
                    achievementTitle.setText(achievement.getName());
                    achievementStatus.setText(achievement.getStatus().substring(0, 1).toUpperCase() + achievement.getStatus().substring(1));
                    Glide.with(mContext).load(achievement.getIconUrl()).into(achievementPic);

                    if (achievement.getStatus().equals("completed")) {
                        status.setImageResource(R.drawable.completed);
                    } else if (achievement.getStatus().equals("locked")) {
                        status.setImageResource(R.drawable.locked);
                    } else {
                        status.setImageResource(R.drawable.unlocked);
                    }

                    break;
                case R.layout.page_info:
                    final LinearLayout videoHolderLayout = (LinearLayout) layout.findViewById(R.id.achievement_video_info);

                    if (achievement.getVideoUrl() != null && !achievement.getVideoUrl().isEmpty()) {
                        videoHolderLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                baseActivity.intentVideoLink(achievement.getVideoUrl());
                            }
                        });
                        videoHolderLayout.setVisibility(View.VISIBLE);
                    } else {
                        videoHolderLayout.setVisibility(View.GONE);
                    }

                    TextView message_video_title = (TextView) layout.findViewById(R.id.message_video_title);
                    View faqs = layout.findViewById(R.id.infoFaqs);
                    faqs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            baseActivity.startActivity(new Intent(baseActivity, AchievementInfoFAQActivity.class));
                            baseActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        }
                    });

                    View material = layout.findViewById(R.id.infoMaterial);
                    material.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            baseActivity.startActivity(new Intent(baseActivity, AchievementInfoMaterialActivity.class));
                            baseActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        }
                    });

                    View keys = layout.findViewById(R.id.infoKey);
                    keys.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            baseActivity.startActivity(new Intent(baseActivity, AchievementInfoKeyActivity.class));
                            baseActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        }
                    });
                    message_video_title.setText(achievement.getVideoTitle());

                    Glide.with(mContext)
                            .load(achievement.getVideoThumbnail())
                            .asBitmap()
                            .centerCrop()
                            .into(new SimpleTarget<Bitmap>(337, 126) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(mContext.getResources(), resource);
                            videoHolderLayout.setBackground(drawable);
                        }
                    });
                    break;
                case R.layout.page_quiz:
                    TextView text_quiz_title = (TextView) layout.findViewById(R.id.quizTitle);
                    text_quiz_title.setText(achievement.getName() + " Quiz");
                    TextView text_safety_quiz = (TextView) layout.findViewById(R.id.text_safety_quiz);
                    text_safety_quiz.setText(achievement.getQuizIntro());
                    break;
                case R.layout.page_summary:
                    TextView text_summary_quiz = (TextView) layout.findViewById(R.id.txt_summary);
                    text_summary_quiz.setText(achievement.getSummary());
                    break;
            }

            collection.addView(layout);
            return layout;
        } else {
            collection.addView(views.get(position));
            return views.get(position);
        }
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        if (views == null) {
            return resLayout.length;
        } else
            return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}