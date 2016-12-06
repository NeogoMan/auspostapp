package com.conducthq.auspost.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener{


    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mSignUpButton;
    private TextView mAlreadyAUser;
    
    private String mEmail;
    private String mPassword;

    private FirebaseAuth mAuth;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeViews();
        initializeObject();
        applyListener();
    }

    private void initializeViews() {
        mEmailField     = (EditText) findViewById(R.id.edit_text_email_sign_up);
        mPasswordField  = (EditText) findViewById(R.id.edit_text_password_sign_up);
        mSignUpButton   = (Button) findViewById(R.id.sign_up_button);
        mAlreadyAUser   = (TextView) findViewById(R.id.already_a_user);
    }

    private void initializeObject() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void applyListener() {
        mSignUpButton.setOnClickListener(this);
        mAlreadyAUser.setOnClickListener(this);
    }
    

    @Override
    public void onClick(View view) {

        if (view.getId()== R.id.already_a_user){
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }else
        {
            mEmail = mEmailField.getText().toString();
            mPassword = mPasswordField.getText().toString();
            if (mEmail != null || mPassword != null){
                mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(this, this);
            }else {
                Toast.makeText(this, "Please fill all the field", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()){

            Toast.makeText(SignUpActivity.this, "congratulation you have successfully ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();

        }else if (task.getException().getMessage().equals("The email address is already in use by another account.")){
            // if the user is already exist
            Toast.makeText(SignUpActivity.this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(SignUpActivity.this, "There is problem please check your connection", Toast.LENGTH_SHORT).show();
        }
    }
}
