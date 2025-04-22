package com.app.weightloss.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.weightloss.R;
import com.app.weightloss.Utils.Support;
import com.app.weightloss.db.DBHelper;
import com.app.weightloss.fragments.WeightLossExercisesFragment;
import com.app.weightloss.model.Exercise;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private int userId;
    private Fragment currentFragment;
    private MaterialToolbar topAppBar;

    private TextInputEditText currentImageUrlField;

    private ImageView ivNavAdd;
    private ImageView ivNavSMS;
    private static final int IMAGE_PICK_REQUEST = 1001;
    private WeightLossExercisesFragment exercisesFragment;

    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivNavAdd = findViewById(R.id.ivNavAdd);
        ivNavSMS = findViewById(R.id.ivNavSMS);
        ivNavAdd.setOnClickListener(v -> showAddPopup());
        ivNavSMS.setOnClickListener(v -> showSMSActivity());
        dbHelper = new DBHelper(this);

        // userId from login or onboarding
        userId = getIntent().getIntExtra("USER_ID", -1);

        // 1) Find the topAppBar & set as SupportActionBar
        topAppBar = findViewById(R.id.topAppBar);


        // Default fragment
        exercisesFragment = new WeightLossExercisesFragment();

        switchToFragment(exercisesFragment);

    }
    private void showAddPopup() {
        PopupMenu popup = new PopupMenu(this, ivNavAdd);

        popup.getMenu().add("Add Weightloss Exercise");

        popup.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

                showAddExerciseDialog(null);

            return true;
        });
        popup.show();
    }

    private void showSMSActivity(){
        Intent intent = new Intent(MainActivity.this, SMSActivity.class);
        startActivity(intent);
    }

    private void switchToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void showAddExerciseDialog(Exercise existingExercise) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);

        TextInputEditText etExName = dialogView.findViewById(R.id.etExName);
        Spinner spExType = dialogView.findViewById(R.id.spExType);
        Spinner spExDifficulty = dialogView.findViewById(R.id.spExDifficulty);
        TextInputEditText etExDuration = dialogView.findViewById(R.id.etExDuration);
        TextInputEditText etExReps = dialogView.findViewById(R.id.etExReps);
        TextInputEditText etDesc = dialogView.findViewById(R.id.etExDesc);
        TextInputEditText etExImageUrl = dialogView.findViewById(R.id.etExImageUrl);
        ImageView btnUploadImage = dialogView.findViewById(R.id.btnUploadImage);
        MaterialButton btnSave = dialogView.findViewById(R.id.btnSaveExercise);

        // Setup spinners for exercise type and difficulty
        String[] exTypes = {"Cardio", "Strength", "Core", "Other"};
        ArrayAdapter<String> exTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exTypes);
        exTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExType.setAdapter(exTypeAdapter);

        String[] difficulties = {"Easy", "Medium", "Hard"};
        ArrayAdapter<String> diffAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficulties);
        diffAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExDifficulty.setAdapter(diffAdapter);

        if (existingExercise != null) {
            etExName.setText(existingExercise.getExerciseName());
            spExType.setSelection(Arrays.asList(exTypes).indexOf(existingExercise.getType()));
            spExDifficulty.setSelection(Arrays.asList(difficulties).indexOf(existingExercise.getDifficulty()));
            etExDuration.setText(String.valueOf(existingExercise.getDuration()));
            etExReps.setText(String.valueOf(existingExercise.getRepetitions()));
            etDesc.setText(existingExercise.getDescription());
            etExImageUrl.setText(existingExercise.getImageUrl());
        }


        // Upload image button: allow gallery selection
        btnUploadImage.setOnClickListener(v -> {
            currentImageUrlField = etExImageUrl;
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, IMAGE_PICK_REQUEST);
        });

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String name = etExName.getText().toString().trim();
            String type = spExType.getSelectedItem().toString();
            String diff = spExDifficulty.getSelectedItem().toString();
            String durStr = etExDuration.getText().toString().trim();
            String repStr = etExReps.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            String imageUrl = etExImageUrl.getText().toString().trim();

            if (name.isEmpty() || durStr.isEmpty() || repStr.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int duration = Integer.parseInt(durStr);
            int reps = Integer.parseInt(repStr);
                    if (existingExercise == null) {
                        Exercise ex = new Exercise();
                        ex.setExerciseName(name);
                        ex.setType(type);
                        ex.setDifficulty(diff);
                        ex.setDuration(duration);
                        ex.setRepetitions(reps);
                        ex.setDescription(desc);
                        ex.setImageUrl(imageUrl);
                        ex.setDateAdded(Support.getFormattedCurrentDate());

                        long result = dbHelper.addExercise(ex);
                        if (result != -1) {
                            Toast.makeText(this, "Record Saved!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            if (exercisesFragment.isVisible()) {
                                exercisesFragment.refreshList();
                            }
                        } else {
                            Toast.makeText(this, "Error saving record", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        existingExercise.setExerciseName(name);
                        existingExercise.setType(type);
                        existingExercise.setDifficulty(diff);
                        existingExercise.setDuration(duration);
                        existingExercise.setRepetitions(reps);
                        existingExercise.setDescription(desc);
                        existingExercise.setImageUrl(imageUrl);
                        existingExercise.setDateAdded(Support.getFormattedCurrentDate());

                        int success = dbHelper.updateExercise(existingExercise); // make sure this method exists
                        if (success==1) {
                            Toast.makeText(this, "Record Updated!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            if (exercisesFragment.isVisible()) {
                                exercisesFragment.refreshList();
                            }
                        } else {
                            Toast.makeText(this, "Error updating record", Toast.LENGTH_SHORT).show();
                        }
                    }
        });

        dialog.show();
    }
    // 3) Inflate the plan menu so "Generate Plan" is always in the top bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_plan, menu);
        return true;
    }


}
