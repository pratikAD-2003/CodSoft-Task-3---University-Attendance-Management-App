package com.example.universityattendancemanagement.Faculty;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.universityattendancemanagement.databinding.ActivityTapOnStudentItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TapOnStudentItem extends AppCompatActivity {
    ActivityTapOnStudentItemBinding binding;
    String uid = "";
    String course = "";
    String roll = "";
    String name = "";
    StudentDateAdapter adapter;
    ArrayList<AttendanceModel> list;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityTapOnStudentItemBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        list = new ArrayList<>();
        uid = getIntent().getStringExtra("id");
        course = getIntent().getStringExtra("course");
        roll = getIntent().getStringExtra("roll_no");
        name = getIntent().getStringExtra("name");

        binding.nameTOSI.setText(name);
        binding.rollTOSI.setText(roll);
        binding.courseTOSI.setText(course);

        binding.studentAttendanceRecyclerviewTOSI.setLayoutManager(new LinearLayoutManager(this));
        binding.studentAttendanceRecyclerviewTOSI.setHasFixedSize(true);
        adapter = new StudentDateAdapter(this, list);
        binding.studentAttendanceRecyclerviewTOSI.setAdapter(adapter);

        getData(uid);
    }

    private void getData(String id) {
        reference.child("AttendanceDates").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        AttendanceModel model = snapshot1.getValue(AttendanceModel.class);
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}