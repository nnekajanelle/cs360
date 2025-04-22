package com.app.weightloss.activities;


public interface OnboardingCallback {
    void onNextStep(int currentStep);
    void onFinishOnboarding();
}
