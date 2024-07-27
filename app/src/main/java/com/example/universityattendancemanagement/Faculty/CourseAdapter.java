package com.example.universityattendancemanagement.Faculty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universityattendancemanagement.databinding.CourseItemLyBinding;

import java.util.ArrayList;
import java.util.Objects;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {
    Context context;
    ArrayList<CourseModel> list = new ArrayList<>();
    CourseItemLyBinding binding;

    public CourseAdapter(Context context, ArrayList<CourseModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CourseItemLyBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CourseHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
        binding.courseNameItem.setText("Course : " + list.get(position).getCourse());
        if (Objects.equals(list.get(position).getBranch(), "Not Available")) {
            holder.binding.courseBranchItem.setVisibility(View.GONE);
        } else {
            holder.binding.courseBranchItem.setVisibility(View.VISIBLE);
            holder.binding.courseBranchItem.setText("Branch : " + list.get(position).getBranch());
        }
        binding.courseSessionItem.setText("Session : " + list.get(position).getSession());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MaintainClasses.class);
                intent.putExtra("session", list.get(position).getSession());
                intent.putExtra("course", list.get(position).getCourse());
                intent.putExtra("branch", list.get(position).getBranch());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CourseHolder extends RecyclerView.ViewHolder {
        CourseItemLyBinding binding;

        public CourseHolder(@NonNull CourseItemLyBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
