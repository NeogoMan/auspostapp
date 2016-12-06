package com.conducthq.auspost.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener{

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button   mSignInButton;

    private String email;
    private String password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initializeViews();
        initializeObject();
        applyListener();
    }


    private void initializeViews() {
        mEmailField    = (EditText) findViewById(R.id.edit_text_email_sign_in);
        mPasswordField = (EditText) findViewById(R.id.edit_text_password_sign_in);
        mSignInButton  = (Button)  findViewById(R.id.sign_in_button);
    }

    private void initializeObject() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void applyListener() {
        mSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        email = mEmailField.getText().toString();
        password = mPasswordField.getText().toString();

        if (email != null || password != null){
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, this);
        }else {
            Toast.makeText(this, "Please fill all the field", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()){
            Intent intent = new Intent(this, IntroVideoActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "please check your password or your email", Toast.LENGTH_SHORT).show();
        }
    }
}
