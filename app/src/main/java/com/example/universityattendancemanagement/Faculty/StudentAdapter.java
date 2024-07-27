package com.example.universityattendancemanagement.Faculty;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universityattendancemanagement.Student.StudentModel;
import com.example.universityattendancemanagement.databinding.StudentsItemsLyBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    Context context;
    ArrayList<StudentModel> list = new ArrayList<>();
    StudentsItemsLyBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    int count = 0;
    int absents = 0;

    public StudentAdapter(Context context, ArrayList<StudentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = StudentsItemsLyBinding.inflate(LayoutInflater.from(context), parent, false);
        return new StudentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        binding.studentNameItem.setText(list.get(position).getName());
        binding.studentRollItem.setText(list.get(position).getRoleNumber());
        binding.studentAttendanceItems.setText(String.valueOf(list.get(position).getAttendance()));

        reference.child("Courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CourseModel model = snapshot1.getValue(CourseModel.class);
                        if (Objects.equals(model.getCourse(), list.get(position).getCourse()) && Objects.equals(model.getBranch(), list.get(position).getBranch()) && Objects.equals(model.getSession(), list.get(position).getSession())) {
                            count = model.getClassCount();
                            absents = count - list.get(position).getAttendance();
                            holder.binding.studentAbsentItems.setText(String.valueOf(absents));
//                            Log.d("CheckDate", String.valueOf(count) + "\n" + String.valueOf(absents) + "\n" + String.valueOf(list.get(position).getAttendance()));
//                            Log.d("TotalDays ", String.valueOf(model.getClassCount()));
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TapOnStudentItem.class);
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("roll_no", list.get(position).getRoleNumber());
                if (Objects.equals(list.get(position).getBranch(), "Not Available")) {
                    intent.putExtra("course", list.get(position).getCourse());
                } else {
                    intent.putExtra("course", list.get(position).getCourse() + " " + list.get(position).getBranch());
                }
                intent.putExtra("id", list.get(position).getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        StudentsItemsLyBinding binding;

        public StudentViewHolder(@NonNull StudentsItemsLyBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
