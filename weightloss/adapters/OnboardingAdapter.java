package com.app.weightloss.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.weightloss.fragments.onboarding.AgeFragment;
import com.app.weightloss.fragments.onboarding.WeightFragment;
import com.app.weightloss.fragments.onboarding.HeightFragment;
import com.app.weightloss.fragments.onboarding.GoalsFragment;
import com.app.weightloss.fragments.onboarding.DietaryFragment;
import com.app.weightloss.model.User;

/**
 * Provides 5 fragments for the onboarding steps.
 */
public class OnboardingAdapter extends FragmentStateAdapter {

    private User onboardingUser;

    public OnboardingAdapter(@NonNull FragmentActivity fragmentActivity, User user) {
        super(fragmentActivity);
        this.onboardingUser = user;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the relevant fragment
        switch (position) {
            case 0: return AgeFragment.newInstance(onboardingUser, 0);
            case 1: return WeightFragment.newInstance(onboardingUser, 1);
            case 2: return HeightFragment.newInstance(onboardingUser, 2);
            case 3: return GoalsFragment.newInstance(onboardingUser, 3);
            case 4: return DietaryFragment.newInstance(onboardingUser, 4);
        }
        // fallback
        return AgeFragment.newInstance(onboardingUser, 0);
    }

    @Override
    public int getItemCount() {
        // 5 total screens
        return 5;
    }
}
