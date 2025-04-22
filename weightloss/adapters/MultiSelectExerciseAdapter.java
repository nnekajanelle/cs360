package com.app.weightloss.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.weightloss.R;
import com.app.weightloss.model.Exercise;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * For selecting multiple exercises with checkboxes
 */
public class MultiSelectExerciseAdapter extends RecyclerView.Adapter<MultiSelectExerciseAdapter.MultiExViewHolder> {

    private List<Exercise> exerciseList;
    // track which items are selected
    private List<Long> selectedIds = new ArrayList<>();

    public MultiSelectExerciseAdapter(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public MultiExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkbox_exercise, parent, false);
        return new MultiExViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiExViewHolder holder, int position) {
        Exercise ex = exerciseList.get(position);
        holder.tvName.setText(ex.getExerciseName());
        holder.tvType.setText("Type: " + ex.getType());
        holder.checkBox.setOnCheckedChangeListener(null); // unbind previous

        // Pre-check if it's in selectedIds
        boolean isSelected = selectedIds.contains((long)ex.getExerciseId());
        holder.checkBox.setChecked(isSelected);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                if(!selectedIds.contains((long)ex.getExerciseId())){
                    selectedIds.add((long)ex.getExerciseId());
                }
            } else {
                selectedIds.remove((Long)(long)ex.getExerciseId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public List<Long> getSelectedExerciseIds() {
        return selectedIds;
    }

    static class MultiExViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tvName, tvType;
        CheckBox checkBox;

        public MultiExViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvExNameCheck);
            tvType = itemView.findViewById(R.id.tvExTypeCheck);
            checkBox = itemView.findViewById(R.id.chkSelectExercise);
        }
    }
}
