package com.conducthq.auspost.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.conducthq.auspost.R;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class ExpandableContentView extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = "ExpandableContentView";
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private ExpandableRelativeLayout expandableLayoutContent;
    private RelativeLayout expandContent;
    private TextView title;
    private TextView link_name;
    private TextView content;
    private boolean contentExpanded = false;
    private View btnLink;
    private ImageView iconExpand;

    private String url;

    public ExpandableContentView(Context context) {
        super(context);
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initialize();
    }

    public ExpandableContentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initialize();
    }

    public void setTitle(String text){
        title.setText(text);
    }

    public void setLinkName(String text){
        link_name.setText(text);
    }

    public void setUrl(String _url) {
        url = _url;
    }

    public void setTitle(int text){
        title.setText(text);
    }

    public void setContent(String content){
        this.content.setText(content);
    }

    public void hideButton() {
        btnLink.setVisibility(GONE);
    }

    public void expanded(boolean status) {
        expandableLayoutContent.setExpanded(status);
    }

    private void initialize() {
        mLayoutInflater.inflate(R.layout.expandable_layout, this);

        expandableLayoutContent = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout);
        title = (TextView) findViewById(R.id.headerExpandableLayout);
        title.setOnClickListener(this);
        link_name = (TextView) findViewById(R.id.link_name);
        content = (TextView) findViewById(R.id.contentExpand);
        expandContent = (RelativeLayout) findViewById(R.id.expandContent);
        expandContent.setOnClickListener(this);
        btnLink = findViewById(R.id.btn_link);
        btnLink.setOnClickListener(this);
        iconExpand = (ImageView) findViewById(R.id.iconExpandable);
        expandableLayoutContent.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
//                contentExpanded = true;
                iconExpand.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.collapse));
            }

            @Override
            public void onPreClose() {
//                contentExpanded = true;
                iconExpand.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.expand));
            }
        });

        expandableLayoutContent.collapse();
        iconExpand.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.expand));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expandContent:
            case R.id.headerExpandableLayout:
                expandableLayoutContent.toggle();
                break;
            case R.id.btn_link:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ExpandableContentView)){
            return false;
        } else {
            return true;
        }
    }

}
