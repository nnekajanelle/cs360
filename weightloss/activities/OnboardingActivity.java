package com.app.weightloss.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.app.weightloss.R;
import com.app.weightloss.db.DBHelper;
import com.app.weightloss.model.User;

/**
 * Hosts a ViewPager2 with 5 fragments: Age, Weight, Height, Goals, Dietary
 */
public class OnboardingActivity extends AppCompatActivity implements OnboardingCallback {

    private ViewPager2 viewPager;
    private com.app.weightloss.activities.OnboardingAdapter adapter;

    private DBHelper dbHelper;
    private int userId;

    // We'll hold the user data in memory while onboarding
    private User onboardingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_pager);

        dbHelper = new DBHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Load existing user data from DB
        User existing = dbHelper.getUserById(userId);
        if (existing != null) {
            onboardingUser = existing;
        } else {
            // If brand new user, create a blank user with userId
            onboardingUser = new User();
            onboardingUser.setUserId(userId);
        }

        viewPager = findViewById(R.id.viewPagerOnboarding);
        adapter = new com.app.weightloss.activities.OnboardingAdapter(this, onboardingUser);
        viewPager.setAdapter(adapter);

        // If you want to disable swiping:
        // viewPager.setUserInputEnabled(false);
    }

    @Override
    public void onNextStep(int currentStep) {
        // Move to the next fragment
        viewPager.setCurrentItem(currentStep + 1, true);
    }

    @Override
    public void onFinishOnboarding() {
        // Save the user data to DB
        dbHelper.updateUserProfile(onboardingUser);

        // Navigate to MainActivity
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("USER_ID", userId);
        startActivity(i);
        finish();
    }
}
