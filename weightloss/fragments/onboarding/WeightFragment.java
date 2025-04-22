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

public class WeightFragment extends Fragment {

    private static final String ARG_USER = "user_arg";
    private static final String ARG_STEP = "step_arg";

    private User user;
    private int stepIndex;

    private TextInputEditText etWeight;
    private Button btnNext;

    public WeightFragment() {}

    public static WeightFragment newInstance(User user, int stepIndex){
        WeightFragment fragment = new WeightFragment();
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
        View root = inflater.inflate(R.layout.fragment_onboarding_weight, container, false);

        etWeight = root.findViewById(R.id.etWeightOnboarding);
        btnNext = root.findViewById(R.id.btnNextWeight);

        // Prefill if user has weight
        if(user.getWeight() > 0){
            etWeight.setText(String.valueOf(user.getWeight()));
        }

        btnNext.setOnClickListener(v -> {
            String wStr = etWeight.getText().toString().trim();
            if(TextUtils.isEmpty(wStr)){
                etWeight.setError("Please enter your weight");
                return;
            }
            float weight = Float.parseFloat(wStr);
            user.setWeight(weight);

            if(getActivity() instanceof OnboardingCallback){
                ((OnboardingCallback)getActivity()).onNextStep(stepIndex);
            }
        });

        return root;
    }
}
