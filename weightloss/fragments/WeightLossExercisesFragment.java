package com.app.weightloss.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.weightloss.R;
import com.app.weightloss.adapters.ExerciseAdapter;
import com.app.weightloss.db.DBHelper;
import com.app.weightloss.model.Exercise;

import java.util.List;

public class WeightLossExercisesFragment extends Fragment {

    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;

    public WeightLossExercisesFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getContext());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_exercises, container, false);
        recyclerView = root.findViewById(R.id.recyclerExercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshList();
        return root;
    }

    // Called by AdminDashboardActivity after adding a new Exercise
    public void refreshList(){
        List<Exercise> exercises = dbHelper.getAllExercises();
        if(exercises.isEmpty()){
            Toast.makeText(getContext(), "No Exercises found.", Toast.LENGTH_SHORT).show();
        }
        adapter = new ExerciseAdapter(exercises, getContext());
        recyclerView.setAdapter(adapter);
    }
}
