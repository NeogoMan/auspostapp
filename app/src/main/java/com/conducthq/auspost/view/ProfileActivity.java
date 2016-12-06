package com.conducthq.auspost.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.data.DietaryRequirements;
import com.conducthq.auspost.model.data.Principal;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.model.response.PrincipalResponse;
import com.conducthq.auspost.task.AsyncContentResponse;
import com.conducthq.auspost.task.ContentTask;
import com.conducthq.auspost.view.EventRsvp.EventRsvpActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements AsyncContentResponse {

    private PrincipalResponse principalResponse;
    private Button mSubmitButton;
    private boolean mFirstTimeOpened = true;

    private ImageView picProfile;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private TextView titleRegistration;
    private Spinner dietaryRequirements;

    private DietaryRequirements selectedDietary;
    public AVLoadingIndicatorView mProgress;

    private boolean isRegister;

    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isRegister = getIntent().getBooleanExtra(Constants.KEY_PROFILE_REGISTER, false);
        if(isRegister){
            setContentView(R.layout.activity_register);
            titleRegistration = (TextView) findViewById(R.id.txt_title_register_profile);
            setTopActionBar(R.string.profile_title_register, Constants.BTN_HELP, -1);
            mSubmitButton = (Button) findViewById(R.id.button);
            mSubmitButton.setText(R.string.profile_register);

        } else {
            setContentView(R.layout.activity_profile);
            setTopActionBar(R.string.profile_title, -1, Constants.BTN_X);
            mSubmitButton = (Button) findViewById(R.id.btn_save);
            mSubmitButton.setText(R.string.profile_button);
        }

        picProfile = (ImageView) findViewById(R.id.profile_pic);
        mProgress= (AVLoadingIndicatorView) findViewById(R.id.avi_loader);
        prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        firstName = (EditText) findViewById(R.id.txt_first_name);
        lastName = (EditText) findViewById(R.id.txt_last_name);
        email = (EditText) findViewById(R.id.txt_email);
        phone = (EditText) findViewById(R.id.txt_phone);
        dietaryRequirements = (Spinner) findViewById(R.id.sp_dietary);

        if (actionHelp != null) {
            actionHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent(getApplication(), MoreContactActivity.class);
                    i.putExtra(Constants.CONTACT_HELP_TITLE, true);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            });
        }

        dietaryRequirements.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mFirstTimeOpened) {
                    mFirstTimeOpened = false;
                    return;
                }
                selectedDietary = (DietaryRequirements) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        principalResponse = EventBus.getDefault().getStickyEvent(PrincipalResponse.class);
        if (principalResponse != null) {
            EventBus.getDefault().removeStickyEvent(principalResponse);
            populateProfile(principalResponse);
        } else {
            Intent i=new Intent(getApplication(), PinActivity.class);
            overridePendingTransition(0, 0);
            startActivity(i);
            finish();
        }

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {

                arg0.setEnabled(false);
                Principal principal = updatePrincipalProfile(new Principal(principalResponse.getResponse().getPrincipal()));

                String token = prefs.getString(Constants.TOKEN, "");

                mProgress.smoothToShow();
                final Call<Principal> call = auspostAusPostApi.UpdatePrincipal(principal, principalResponse.getResponse().getPrincipal().getId(), token);

                call.enqueue(new Callback<Principal>() {
                    @Override
                    public void onResponse(Call<Principal> call, Response<Principal> response) {
                        mProgress.smoothToHide();
                        final Principal principal = response.body();
                        if (response.isSuccessful()) {
                            if(isRegister) {
                                mProgress.smoothToShow();
                                final ContentTask asyncTask = new ContentTask(getApplicationContext());
                                asyncTask.delegate = ProfileActivity.this;
                                asyncTask.execute();
                            } else {
                                onBackPressed();
                            }
                        } else if (response.code() == 401) {
                                arg0.setEnabled(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<Principal> call, Throwable t) {
                        mProgress.smoothToHide();
                        String Response = t.getMessage();
                    }
                });
            }
        });

        picProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplication(), ProfilePictureActivity.class), Constants.INTENT_UPLOAD_IMAGE);
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.INTENT_UPLOAD_IMAGE) {
            if (resultCode == RESULT_OK) {
                Principal principal = principalResponse.getResponse().getPrincipal();
                principal.setImageUrl(data.getStringExtra(ProfilePictureActivity.DATA_IMAGE_URL));
                Glide.with(this).load(principal.getImageUrl()).into(picProfile);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void populateProfile(PrincipalResponse principalResponse) {

        Principal principal = principalResponse.getResponse().getPrincipal();

        firstName.setText(principal.getFirstName());
        lastName.setText(principal.getLastName());
        email.setText(principal.getEmail());
        phone.setText(principal.getPhone());
        if(isRegister) {
            Resources res = getResources();
            titleRegistration.setText(String.format(res.getString(R.string.profile_main_title), principal.getFirstName()));
        }

//        Glide.with(this).load(principal.getImageUrl()).into(picProfile);

        ArrayAdapter<DietaryRequirements> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, principalResponse.getResponse().getDietaryRequirements());
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        dietaryRequirements.setAdapter(adapter);

        for (int i=0;i<dietaryRequirements.getCount();i++){
            DietaryRequirements currentPosition = (DietaryRequirements) dietaryRequirements.getItemAtPosition(i);
            if (currentPosition.getId().equals(principal.getDietaryRequirements())){
                dietaryRequirements.setSelection(i);
                selectedDietary = currentPosition;
            }
        }

    }

    public Principal updatePrincipalProfile(Principal principal) {
        principal.setFirstName(firstName.getText().toString());
        principal.setLastName(lastName.getText().toString());
        principal.setEmail(email.getText().toString());
        principal.setPhone(phone.getText().toString());
        principal.setDietaryRequirements(selectedDietary.getId());
        return principal;
    }

    @Override
    public void processFinish(ContentResponse contentResponse) {
        mProgress.smoothToHide();
        Intent i=new Intent(getApplication(), EventRsvpActivity.class);
        EventBus.getDefault().postSticky(contentResponse);
        overridePendingTransition(0, 0);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
