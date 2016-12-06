package com.conducthq.auspost.helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.conducthq.auspost.R;
import com.conducthq.auspost.model.data.Achievement;
import com.conducthq.auspost.model.data.Message;
import com.conducthq.auspost.task.LikeMessageTask;
import com.conducthq.auspost.view.AchievementActivity;
import com.conducthq.auspost.view.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * Created by conduct19 on 28/10/2016.
 */

public class AchievementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    Context context;
    BaseActivity baseActivity;
    List<Achievement> achievements = Collections.emptyList();

    public AchievementAdapter(List<Achievement> achievements, Context context) {
        this.achievements = achievements;
        this.context = context;
        baseActivity = (BaseActivity) context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    class ViewHolderAchievement extends RecyclerView.ViewHolder {
        TextView title;
        ImageView achievementPic;
        ImageView status;
        View view;

        public ViewHolderAchievement(View itemView) {
            super(itemView);
            view = itemView;
            title = (TextView) itemView.findViewById(R.id.achievementTitle);
            achievementPic = (ImageView) itemView.findViewById(R.id.achievementPic);
            status = (ImageView) itemView.findViewById(R.id.status);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_bagde, parent, false);
        return new ViewHolderAchievement(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final Achievement achievement = achievements.get(position);
        ((ViewHolderAchievement) holder).title.setText(achievement.getName());

        if (achievement.getStatus().equals("completed")) {
            ((ViewHolderAchievement) holder).status.setImageResource(R.drawable.completed);
            Glide.with(context).load(achievement.getIconUrl()).into(((ViewHolderAchievement) holder).achievementPic);
        } else if (achievement.getStatus().equals("locked")) {
            Glide.with(context)
                    .load(achievement.getIconUrl())
                    .bitmapTransform(new GrayscaleTransformation(context))
                    .into(((ViewHolderAchievement) holder).achievementPic);
            ((ViewHolderAchievement) holder).status.setImageResource(R.drawable.locked);
        } else {
            ((ViewHolderAchievement) holder).status.setImageResource(R.drawable.unlocked);
            Glide.with(context).load(achievement.getIconUrl()).into(((ViewHolderAchievement) holder).achievementPic);
        }

        ((ViewHolderAchievement) holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (achievement.getStatus().equals("locked")) {
                    baseActivity.lockedInfo();
                } else {
                    EventBus.getDefault().postSticky(achievement);
                    context.startActivity(new Intent(context, AchievementActivity.class));
                    baseActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return achievements.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Achievement data) {
        achievements.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Message data) {
        int position = achievements.indexOf(data);
        achievements.remove(position);
        notifyItemRemoved(position);
    }

    // Clean all elements of the recycler
    public void clear() {
        achievements.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Achievement> data) {
        achievements.addAll(data);
        notifyDataSetChanged();
    }

}