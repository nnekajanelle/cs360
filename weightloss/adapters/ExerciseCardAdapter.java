package com.app.weightloss.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.weightloss.R;
import com.app.weightloss.model.Exercise;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

/**
 * Shows exercises in a horizontal RecyclerView with stylish card
 */
public class ExerciseCardAdapter extends RecyclerView.Adapter<ExerciseCardAdapter.ExViewHolder> {

    private List<Exercise> exerciseList;

    public ExerciseCardAdapter(List<Exercise> list){
        this.exerciseList = list;
    }

    @NonNull
    @Override
    public ExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise_card_stylish, parent, false);
        return new ExViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExViewHolder holder, int position) {
        Exercise ex = exerciseList.get(position);
        holder.tvName.setText(ex.getExerciseName());
        holder.tvType.setText("Type: " + ex.getType());
        holder.tvDifficulty.setText("Difficulty: " + ex.getDifficulty());
        holder.tvDuration.setText("Duration: " + ex.getDuration() + " min");
        holder.tvReps.setText("Reps: " + ex.getRepetitions());
        holder.tvReps.setText("Reps: " + ex.getRepetitions());
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    static class ExViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tvName, tvType, tvDifficulty, tvDuration, tvReps;
        public ExViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvExNameCard);
            tvType = itemView.findViewById(R.id.tvExTypeCard);
            tvDifficulty = itemView.findViewById(R.id.tvExDiffCard);
            tvDuration = itemView.findViewById(R.id.tvExDurationCard);
            tvReps = itemView.findViewById(R.id.tvExRepsCard);
        }
    }
}
