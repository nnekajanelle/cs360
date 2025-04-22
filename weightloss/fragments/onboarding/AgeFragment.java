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

public class AgeFragment extends Fragment {

    private static final String ARG_USER = "user_arg";
    private static final String ARG_STEP = "step_arg";

    private User user;
    private int stepIndex;

    private TextInputEditText etAge;
    private Button btnNext;

    public AgeFragment() {}

    public static AgeFragment newInstance(User user, int stepIndex) {
        AgeFragment fragment = new AgeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putInt(ARG_STEP, stepIndex);
        fragment.setArguments(args);
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
        View root = inflater.inflate(R.layout.fragment_onboarding_age, container, false);

        etAge = root.findViewById(R.id.etAgeOnboarding);
        btnNext = root.findViewById(R.id.btnNextAge);

        // Prefill if user already has an age
        if(user.getAge() > 0){
            etAge.setText(String.valueOf(user.getAge()));
        }

        btnNext.setOnClickListener(v -> {
            String ageStr = etAge.getText().toString().trim();
            if(TextUtils.isEmpty(ageStr)){
                etAge.setError("Please enter your age");
                return;
            }
            int age = Integer.parseInt(ageStr);
            user.setAge(age);

            if(getActivity() instanceof OnboardingCallback){
                ((OnboardingCallback)getActivity()).onNextStep(stepIndex);
            }
        });

        return root;
    }
}
