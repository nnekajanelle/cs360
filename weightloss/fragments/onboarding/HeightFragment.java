package com.app.weightloss.fragments.onboarding;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.weightloss.R;
import com.app.weightloss.activities.OnboardingCallback;
import com.app.weightloss.model.User;
import com.google.android.material.textfield.TextInputEditText;

public class HeightFragment extends Fragment {

    private static final String ARG_USER = "user_arg";
    private static final String ARG_STEP = "step_arg";

    private User user;
    private int stepIndex;

    private TextInputEditText etHeight;
    private Button btnNext;

    public HeightFragment() {}

    public static HeightFragment newInstance(User user, int stepIndex){
        HeightFragment fragment = new HeightFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_USER, user);
        b.putInt(ARG_STEP, stepIndex);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            user = (User) getArguments().getSerializable(ARG_USER);
            stepIndex = getArguments().getInt(ARG_STEP);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View root = inflater.inflate(R.layout.fragment_onboarding_height, container, false);

        etHeight = root.findViewById(R.id.etHeightOnboarding);
        btnNext = root.findViewById(R.id.btnNextHeight);

        if(user.getHeight() > 0){
            etHeight.setText(String.valueOf(user.getHeight()));
        }

        btnNext.setOnClickListener(v -> {
            String hStr = etHeight.getText().toString().trim();
            if(TextUtils.isEmpty(hStr)){
                etHeight.setError("Please enter your height");
                return;
            }
            float heightVal = Float.parseFloat(hStr);
            user.setHeight(heightVal);

            if(getActivity() instanceof OnboardingCallback){
                ((OnboardingCallback)getActivity()).onNextStep(stepIndex);
            }
        });

        return root;
    }
}
