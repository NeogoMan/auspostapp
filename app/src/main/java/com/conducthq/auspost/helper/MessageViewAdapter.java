package com.conducthq.auspost.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.conducthq.auspost.R;
import com.conducthq.auspost.model.data.Message;
import com.conducthq.auspost.model.data.MessageLike;
import com.conducthq.auspost.task.LikeMessageTask;
import com.conducthq.auspost.task.UnLikeMessageTask;
import com.conducthq.auspost.view.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by conduct19 on 28/10/2016.
 */

public class MessageViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Message> list = Collections.emptyList();
    BaseActivity baseActivity;

    public MessageViewAdapter(List<Message> list, Context context) {
        this.list = list;
        this.context = context;
        baseActivity = (BaseActivity) context;
    }

    class ViewHolderComment extends RecyclerView.ViewHolder {
        CardView cv;
        TextView time;
        TextView content;
        ImageView principalPic;
        TextView likeCounter;
        TextView txt_principal;
        ImageButton imageButtonLike;

        public ViewHolderComment(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            time = (TextView) itemView.findViewById(R.id.txt_time);
            content = (TextView) itemView.findViewById(R.id.content);
            principalPic = (ImageView) itemView.findViewById(R.id.principalPic);
            likeCounter = (TextView) itemView.findViewById(R.id.likesCounter);
            txt_principal = (TextView) itemView.findViewById(R.id.txt_principal);
            imageButtonLike = (ImageButton) itemView.findViewById(R.id.imageButtonLike);

            imageButtonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message message = list.get(getAdapterPosition());
                    if ((int)imageButtonLike.getTag() == R.drawable.like) {
                        new LikeMessageTask(context, message.getId(), message).execute();
                    } else {
                        new UnLikeMessageTask(context, message.getId(), message).execute();
                    }
                }
            });

        }
    }

    class ViewHolderJoined extends RecyclerView.ViewHolder {
        CardView cv;
        TextView time;
        ImageView principalPic;
        TextView txt_participant;
        TextView likeCounter;
        ImageButton imageButtonLike;

        public ViewHolderJoined(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            time = (TextView) itemView.findViewById(R.id.txt_time);
            principalPic = (ImageView) itemView.findViewById(R.id.principalPic);
            txt_participant = (TextView) itemView.findViewById(R.id.txt_participant);
            likeCounter = (TextView) itemView.findViewById(R.id.likesCounter);
            imageButtonLike = (ImageButton) itemView.findViewById(R.id.imageButtonLike);

            imageButtonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message message = list.get(getAdapterPosition());
                    if ((int)imageButtonLike.getTag() == R.drawable.like) {
                        new LikeMessageTask(context, message.getId(), message).execute();
                    } else {
                        new UnLikeMessageTask(context, message.getId(), message).execute();
                    }
                }
            });


        }
    }

    class ViewHolderVideo extends RecyclerView.ViewHolder {
        TextView message_video_title;
        LinearLayout videoHolderLayout;

        public ViewHolderVideo(View itemView) {
            super(itemView);
            videoHolderLayout = (LinearLayout) itemView.findViewById(R.id.videoHolderLayout);
            message_video_title = (TextView) itemView.findViewById(R.id.message_video_title);
        }
    }

    class ViewHolderAchievement extends RecyclerView.ViewHolder {
        CardView cv;
        TextView time;
        ImageView principalPic;
        ImageView achievementPic;
        TextView txt_principal;
        TextView message_achievement_name;
        TextView likeCounter;
        ImageButton imageButtonLike;

        public ViewHolderAchievement(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            time = (TextView) itemView.findViewById(R.id.txt_time);
            principalPic = (ImageView) itemView.findViewById(R.id.principalPic);
            achievementPic = (ImageView) itemView.findViewById(R.id.achievementPic);
            txt_principal = (TextView) itemView.findViewById(R.id.txt_participant);
            message_achievement_name = (TextView) itemView.findViewById(R.id.message_achievement_name);
            likeCounter = (TextView) itemView.findViewById(R.id.likesCounter);
            imageButtonLike = (ImageButton) itemView.findViewById(R.id.imageButtonLike);

            imageButtonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message message = list.get(getAdapterPosition());
                    if ((int)imageButtonLike.getTag() == R.drawable.like) {
                        new LikeMessageTask(context, message.getId(), message).execute();
                    } else {
                        new UnLikeMessageTask(context, message.getId(), message).execute();
                    }
                }
            });

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == Constants.MESSAGE_TYPE_COMMENT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_comment_layout, parent, false);
            return new ViewHolderComment(itemView);
        } else if (viewType == Constants.MESSAGE_TYPE_JOINING) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_joined_layout, parent, false);
            return new ViewHolderJoined(itemView);
        } else if (viewType == Constants.MESSAGE_TYPE_VIDEO) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_video_layout, parent, false);
            return new ViewHolderVideo(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_achievement_layout, parent, false);
            return new ViewHolderAchievement(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Message message = list.get(position);

        String created = message.getCreated();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String createdRelative = "";

        try {
            Date createdAt = format.parse(created);
            createdRelative = DateUtils.getRelativeTimeSpanString(createdAt.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String principalName = "";
        if (message.getPrincipal() != null) {
            principalName = message.getPrincipal().getFirstName() + " " + String.valueOf(message.getPrincipal().getLastName().charAt(0));
        }

        if (holder.getItemViewType() == Constants.MESSAGE_TYPE_COMMENT) {

            ((ViewHolderComment) holder).time.setText("Commented " + createdRelative);
            ((ViewHolderComment) holder).content.setText(message.getContent());
            ((ViewHolderComment) holder).likeCounter.setText(String.valueOf(message.getCountMessageLikes()));
            Glide.with(context).load(message.getPrincipal().getImageUrl()).into(((ViewHolderComment) holder).principalPic);
            ((ViewHolderComment) holder).txt_principal.setText(principalName);
            ((ViewHolderComment) holder).imageButtonLike.setImageResource(R.drawable.like);
            ((ViewHolderComment) holder).likeCounter.setTextColor(context.getResources().getColor(R.color.colorLikeCounterInactive));
            ((ViewHolderComment) holder).imageButtonLike.setTag(R.drawable.like);

            for(MessageLike msgTemp : message.getMessageLikes()) {
                if(msgTemp.getPrincipalId().equals(message.getPrincipal().getId())) {
                    ((ViewHolderComment) holder).imageButtonLike.setImageResource(R.drawable.liked);
                    ((ViewHolderComment) holder).imageButtonLike.setTag(R.drawable.liked);
                    ((ViewHolderComment) holder).likeCounter.setTextColor(context.getResources().getColor(R.color.colorRedAusPost));
                    break;
                }
            }

        } else if (holder.getItemViewType() == Constants.MESSAGE_TYPE_JOINING) {

            ((ViewHolderJoined) holder).time.setText(createdRelative);
            ((ViewHolderJoined) holder).likeCounter.setText(String.valueOf(message.getCountMessageLikes()));
            Glide.with(context).load(message.getPrincipal().getImageUrl()).into(((ViewHolderJoined) holder).principalPic);
            Log.e("ATAG", message.getPrincipal().getImageUrl());
            ((ViewHolderJoined) holder).txt_participant.setText(principalName);
            ((ViewHolderJoined) holder).imageButtonLike.setImageResource(R.drawable.like);
            ((ViewHolderJoined) holder).likeCounter.setTextColor(context.getResources().getColor(R.color.colorLikeCounterInactive));
            ((ViewHolderJoined) holder).imageButtonLike.setTag(R.drawable.like);

            for(MessageLike msgTemp : message.getMessageLikes()) {
                if(msgTemp.getPrincipalId().equals(message.getPrincipal().getId())) {
                    ((ViewHolderJoined) holder).imageButtonLike.setImageResource(R.drawable.liked);
                    ((ViewHolderJoined) holder).imageButtonLike.setTag(R.drawable.liked);
                    ((ViewHolderJoined) holder).likeCounter.setTextColor(context.getResources().getColor(R.color.colorRedAusPost));
                    break;
                }
            }

        } else if (holder.getItemViewType() == Constants.MESSAGE_TYPE_VIDEO) {

            ((ViewHolderVideo) holder).message_video_title.setText(message.getTitle());
            Glide.with(context).load(message.getThumbnail()).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>(337, 126) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                    ((ViewHolderVideo) holder).videoHolderLayout.setBackground(drawable);
                    ((ViewHolderVideo) holder).videoHolderLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            baseActivity.intentVideoLink(message.getContent());
                        }
                    });
                }
            });

        } else if (holder.getItemViewType() == Constants.MESSAGE_TYPE_ACHIEVEMENT) {

            ((ViewHolderAchievement) holder).time.setText(createdRelative);
            Glide.with(context).load(message.getPrincipal().getImageUrl()).into(((ViewHolderAchievement) holder).principalPic);
            Glide.with(context).load(message.getAchievement().getIconUrl()).into(((ViewHolderAchievement) holder).achievementPic);
            ((ViewHolderAchievement) holder).txt_principal.setText(principalName);
            ((ViewHolderAchievement) holder).likeCounter.setText(String.valueOf(message.getCountMessageLikes()));
            ((ViewHolderAchievement) holder).message_achievement_name.setText("'" + message.getAchievement().getName() + "' achievement.");
            ((ViewHolderAchievement) holder).imageButtonLike.setImageResource(R.drawable.like);
            ((ViewHolderAchievement) holder).likeCounter.setTextColor(context.getResources().getColor(R.color.colorLikeCounterInactive));
            ((ViewHolderAchievement) holder).imageButtonLike.setTag(R.drawable.like);

            for(MessageLike msgTemp : message.getMessageLikes()) {
                if(msgTemp.getPrincipalId().equals(message.getPrincipal().getId())) {
                    ((ViewHolderAchievement) holder).imageButtonLike.setImageResource(R.drawable.liked);
                    ((ViewHolderAchievement) holder).imageButtonLike.setTag(R.drawable.liked);
                    ((ViewHolderAchievement) holder).likeCounter.setTextColor(context.getResources().getColor(R.color.colorRedAusPost));
                    break;
                }
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).getMessage_type()) {
            case "comment":
                return Constants.MESSAGE_TYPE_COMMENT;
            case "joining":
                return Constants.MESSAGE_TYPE_JOINING;
            case "video":
                return Constants.MESSAGE_TYPE_VIDEO;
            case "achievement":
                return Constants.MESSAGE_TYPE_ACHIEVEMENT;
            default:
                return Constants.MESSAGE_TYPE_COMMENT;
        }
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Message data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Message data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    // Clean all elements of the recycler
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void update(Message message) {
        try {
            int pos = list.indexOf(message);
            list.set(pos, message);
            notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    // Add a list of items
    public void addAll(List<Message> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }


}