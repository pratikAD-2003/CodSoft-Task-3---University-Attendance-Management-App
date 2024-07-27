package com.example.universityattendancemanagement.Faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.universityattendancemanagement.R;
import com.example.universityattendancemanagement.Student.StudentModel;
import com.example.universityattendancemanagement.databinding.ActivityMainBinding;
import com.example.universityattendancemanagement.databinding.ActivityMaintainClassesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MaintainClasses extends AppCompatActivity {
    ActivityMaintainClassesBinding binding;
    String session = "", course = "", branch = "", pushId = "";
    int count = 0;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMaintainClassesBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        session = getIntent().getStringExtra("session");
        course = getIntent().getStringExtra("course");
        branch = getIntent().getStringExtra("branch");

        binding.courseMC.setText(course);
        binding.sessionMC.setText("Session : " + session);

        if (Objects.equals(branch, "Not Available")) {
            binding.branchMC.setVisibility(View.GONE);
        } else {
            binding.branchMC.setText("Branch : " + branch);
        }
        getMyData();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        binding.markTodayClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Are you sure ?");
                builder.setMessage("After clicking 'Allow' Button Students can mark their attendance!");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        count++;
                        binding.maintainClassProgressbarMC.setVisibility(View.VISIBLE);
                        reference.child("Courses").child(pushId).child("classCount").setValue(count).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    String dateFormat = "yyyy-MM-dd";
                                    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                                    Date currentDate = new Date();
                                    String dateString = sdf.format(currentDate);
                                    reference.child("Courses").child(pushId).child("date").setValue(dateString).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                reference.child("Courses").child(pushId).child("isTodayHoliday").setValue("false").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            binding.maintainClassProgressbarMC.setVisibility(View.GONE);
//                                                            recreate();
                                                        } else {
                                                            binding.maintainClassProgressbarMC.setVisibility(View.GONE);
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        binding.maintainClassProgressbarMC.setVisibility(View.GONE);
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    binding.maintainClassProgressbarMC.setVisibility(View.GONE);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                binding.maintainClassProgressbarMC.setVisibility(View.GONE);
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
    }

    private void getMyData() {
        binding.maintainClassProgressbarMC.setVisibility(View.VISIBLE);
        reference.child("Courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CourseModel model = snapshot1.getValue(CourseModel.class);
                        if (Objects.equals(model.getCourse(), course) && Objects.equals(model.getBranch(), branch) && Objects.equals(model.getSession(), session)) {
                            pushId = model.getPushId();
                            count = model.getClassCount();
                            checkWithDate(model.getDate(), pushId);
                            binding.maintainClassProgressbarMC.setVisibility(View.GONE);
                            binding.classCountMC.setText(String.valueOf(model.getClassCount()));
                        } else {
                            binding.maintainClassProgressbarMC.setVisibility(View.GONE);
                        }
                    }
                } else {
                    binding.maintainClassProgressbarMC.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.maintainClassProgressbarMC.setVisibility(View.GONE);
            }
        });
    }

    private void checkWithDate(String date, String pushId) {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        Date retrievedDate = null;
        try {
            retrievedDate = sdf.parse(date);
            System.out.println("Retrieved Date: " + retrievedDate);
        } catch (ParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
        }

        if (retrievedDate != null) {
            Date currentDateForComparison = new Date();
            String currentDateString = sdf.format(currentDateForComparison);
            Date parsedCurrentDate = null;
            try {
                parsedCurrentDate = sdf.parse(currentDateString);
            } catch (ParseException e) {
                System.out.println("Error parsing current date: " + e.getMessage());
            }

            if (parsedCurrentDate != null) {
                // Compare the dates using before() and after()
                if (retrievedDate.before(parsedCurrentDate)) {
                    System.out.println("Retrieved date is before the current date (using before()).");
                    binding.markTodayClasses.setText("Allow Today's Students Attendance");
                    binding.markTodayClasses.setClickable(true);
                    reference.child("Courses").child(pushId).child("isTodayHoliday").setValue("true");
                } else if (retrievedDate.after(parsedCurrentDate)) {
                    System.out.println("Retrieved date is after the current date (using after()).");
                } else {
                    System.out.println("Retrieved date is equal to the current date (using equals()).");
                    binding.markTodayClasses.setText("This class is Live!");
                    binding.markTodayClasses.setClickable(false);
                }
            }
        }
    }
}