package com.example.universityattendancemanagement.Faculty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universityattendancemanagement.databinding.AttendanceDateLyBinding;

import java.util.ArrayList;

public class StudentDateAdapter extends RecyclerView.Adapter<StudentDateAdapter.MyViewHolder> {
    Context context;
    ArrayList<AttendanceModel> list = new ArrayList<>();
    AttendanceDateLyBinding binding;

    public StudentDateAdapter(Context context, ArrayList<AttendanceModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AttendanceDateLyBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        binding.attendanceItemDate.setText(list.get(position).getDate());
        binding.attendanceItemCount.setText(list.get(position).getAttendanceCount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AttendanceDateLyBinding binding;

        public MyViewHolder(@NonNull AttendanceDateLyBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
