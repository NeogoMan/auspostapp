package com.conducthq.auspost.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.conducthq.auspost.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mSignInButton;
    private Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        applyListener();
    }


    private void initializeViews() {
        mSignInButton = (Button) findViewById(R.id.bSignIn);
        mSignUpButton = (Button) findViewById(R.id.bSignUp);
    }

    private void applyListener() {
        mSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case  R.id.bSignIn:
                Intent intentToSignIn = new Intent(this, SignInActivity.class);
                startActivity(intentToSignIn);
                break;
            case  R.id.bSignUp:
                Intent intentToSignUp = new Intent(this, SignUpActivity.class);
                startActivity(intentToSignUp);
                break;
        }

    }
}
