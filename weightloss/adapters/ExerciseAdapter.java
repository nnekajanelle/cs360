package com.app.weightloss.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.app.weightloss.R;
import com.app.weightloss.Utils.Support;
import com.app.weightloss.activities.MainActivity;
import com.app.weightloss.db.DBHelper;
import com.app.weightloss.model.Exercise;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> dataList;
    private DBHelper dbHelper;
    private Context context;

    public ExerciseAdapter(List<Exercise> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise_card, parent, false);
        return new ExerciseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise ex = dataList.get(position);
        holder.tvName.setText(""+ex.getExerciseName());
        holder.tvType.setText("Type: "+ex.getType());
        holder.tvDifficulty.setText("Difficulty: "+ex.getDifficulty());
        holder.tvDuration.setText("Duration: "+String.valueOf(ex.getDuration()));
        holder.tvReps.setText("Reps: "+String.valueOf(ex.getRepetitions()));
        holder.tvDate.setText("Date: "+ex.getDateAdded());
        holder.btnDelete.setOnClickListener(v -> {
            String[] options = {"Remove"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(ex.getExerciseName());
            builder.setItems(options, (dialog, which) -> {

                    // Remove exercise
                    dbHelper.removeExercise(ex.getExerciseId());
                    dataList.remove(position);
                    notifyItemRemoved(position);

            });
            builder.show();
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).showAddExerciseDialog(ex);
            }
        });

    }
    private void showExerciseOptionsDialog(Exercise ex, int position){
        String[] options = {"Edit", "Remove"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(ex.getExerciseName());
        builder.setItems(options, (dialog, which) -> {
            if(which == 0){
                // Edit (similar approach, open a dialog, etc.)
                // ...
            } else {
                // Remove
                dbHelper.removeExercise(ex.getExerciseId());
                dataList.remove(position);
                notifyItemRemoved(position);
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tvName, tvType, tvDifficulty, tvDuration, tvReps,tvDate;
        Button btnDelete,btnEdit;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvExName);
            tvType = itemView.findViewById(R.id.tvExType);
            tvDifficulty = itemView.findViewById(R.id.tvExDifficulty);
            tvDuration = itemView.findViewById(R.id.tvExDuration);
            tvReps = itemView.findViewById(R.id.tvExReps);
            tvDate = itemView.findViewById(R.id.tvExDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }


}
