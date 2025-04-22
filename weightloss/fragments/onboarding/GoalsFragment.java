package com.app.weightloss.fragments.onboarding;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.app.weightloss.R;
import com.app.weightloss.activities.OnboardingCallback;
import com.app.weightloss.model.User;
import java.util.ArrayList;
import java.util.List;

public class GoalsFragment extends Fragment {

    private static final String ARG_USER = "user_arg";
    private static final String ARG_STEP = "step_arg";

    private User user;
    private int stepIndex;

    private CheckBox cbGoalBuildMuscle;
    private CheckBox cbGoalLoseWeight;
    private CheckBox cbGoalEndurance;
    private CheckBox cbGoalOther;
    private TextView tvSelectedGoals;
    private Button btnNext;

    public GoalsFragment() {}

    public static GoalsFragment newInstance(User user, int stepIndex) {
        GoalsFragment fragment = new GoalsFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_onboarding_goals, container, false);

        // Initialize views
        cbGoalBuildMuscle = root.findViewById(R.id.cbGoalBuildMuscle);
        cbGoalLoseWeight  = root.findViewById(R.id.cbGoalLoseWeight);
        cbGoalEndurance   = root.findViewById(R.id.cbGoalEndurance);
        cbGoalOther       = root.findViewById(R.id.cbGoalOther);
        tvSelectedGoals   = root.findViewById(R.id.tvSelectedGoals);
        btnNext           = root.findViewById(R.id.btnNextGoals);

        // Set mutual exclusivity: if "Build Muscle" is checked, uncheck "Lose Weight" and vice versa.
        cbGoalBuildMuscle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                cbGoalLoseWeight.setChecked(false);
            }
            updateSelectedGoals();
        });
        cbGoalLoseWeight.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                cbGoalBuildMuscle.setChecked(false);
            }
            updateSelectedGoals();
        });
        cbGoalEndurance.setOnCheckedChangeListener((buttonView, isChecked) -> updateSelectedGoals());
        cbGoalOther.setOnCheckedChangeListener((buttonView, isChecked) -> updateSelectedGoals());

        // Set initial text
        tvSelectedGoals.setText("Selected: None");

        btnNext.setOnClickListener(v -> {
            List<String> selectedGoals = new ArrayList<>();
            if(cbGoalBuildMuscle.isChecked()) selectedGoals.add("Build Muscle");
            if(cbGoalLoseWeight.isChecked())  selectedGoals.add("Lose Weight");
            if(cbGoalEndurance.isChecked())   selectedGoals.add("Improve Endurance");
            if(cbGoalOther.isChecked())       selectedGoals.add("General Fitness");

            if(selectedGoals.isEmpty()){
                Toast.makeText(getContext(), "Please select at least one goal.", Toast.LENGTH_SHORT).show();
                return;
            }

            // For onboarding, store all selected goals as a comma-separated string.
            String goalsStr = android.text.TextUtils.join(", ", selectedGoals);
            user.setGoals(goalsStr);

            // Proceed to the next onboarding step
            if(getActivity() instanceof OnboardingCallback){
                ((OnboardingCallback) getActivity()).onNextStep(stepIndex);
            }
        });

        return root;
    }

    private void updateSelectedGoals() {
        List<String> goals = new ArrayList<>();
        if(cbGoalBuildMuscle.isChecked()) goals.add("Build Muscle");
        if(cbGoalLoseWeight.isChecked())  goals.add("Lose Weight");
        if(cbGoalEndurance.isChecked())   goals.add("Improve Endurance");
        if(cbGoalOther.isChecked())       goals.add("General Fitness");

        if(goals.isEmpty()){
            tvSelectedGoals.setText("Selected: None");
        } else {
            tvSelectedGoals.setText("Selected: " + android.text.TextUtils.join(", ", goals));
        }
    }
}
