package com.app.weightloss.fragments.onboarding;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.weightloss.R;
import com.app.weightloss.activities.OnboardingCallback;
import com.app.weightloss.model.User;

public class DietaryFragment extends Fragment {

    private static final String ARG_USER = "user_arg";
    private static final String ARG_STEP = "step_arg";

    private User user;
    private int stepIndex;

    private Spinner spDietary;
    private Button btnFinish;

    public DietaryFragment() {}

    public static DietaryFragment newInstance(User user, int stepIndex){
        DietaryFragment fragment = new DietaryFragment();
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
        View root = inflater.inflate(R.layout.fragment_onboarding_dietary, container, false);

        // Replace the previous TextInputEditText with a Spinner (ensure your XML uses spDietaryOnboarding)
        spDietary = root.findViewById(R.id.spDietaryOnboarding);
        btnFinish = root.findViewById(R.id.btnFinishOnboarding);

        // Set up dietary options
        String[] dietaryOptions = {"None", "Vegan", "Vegetarian", "Keto", "Paleo", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, dietaryOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDietary.setAdapter(adapter);

        // If the user already has a dietary restriction, select it in the spinner
        if(user != null && user.getDietaryRestrictions() != null) {
            int pos = adapter.getPosition(user.getDietaryRestrictions());
            if(pos >= 0){
                spDietary.setSelection(pos);
            }
        }

        btnFinish.setOnClickListener(v -> {
            String selectedDietary = spDietary.getSelectedItem().toString();
            user.setDietaryRestrictions(selectedDietary);

            if(getActivity() instanceof OnboardingCallback){
                ((OnboardingCallback)getActivity()).onFinishOnboarding();
            } else {
                Toast.makeText(getContext(), "Onboarding finished.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
