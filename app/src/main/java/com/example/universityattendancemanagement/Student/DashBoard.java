package com.example.universityattendancemanagement.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.universityattendancemanagement.Faculty.AttendanceModel;
import com.example.universityattendancemanagement.Faculty.CourseModel;
import com.example.universityattendancemanagement.R;
import com.example.universityattendancemanagement.databinding.ActivityDashBoardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DashBoard extends AppCompatActivity {
    ActivityDashBoardBinding binding;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    DatabaseReference reference;
    int attendance = 0;
    Dialog dialog;
    EditText pinEditText;
    Button verifyBtn;

    String course = "", branch = "", session = "", pushId = "";
    int securityPin = 0;

    boolean isAllow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        reference = FirebaseDatabase.getInstance().getReference();

        getMyData();

        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();

        dialog = new Dialog(DashBoard.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.security_pin_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.corner_with_10));

        pinEditText = dialog.findViewById(R.id.enterPinSPD);
        verifyBtn = dialog.findViewById(R.id.verifyBtnSPD);

        // Set the minimum and maximum dates to current date
        binding.currentDatePickerDB.setMinDate(currentTimeInMillis);
        binding.currentDatePickerDB.setMaxDate(currentTimeInMillis);

        // Set the DatePicker to current date
        binding.currentDatePickerDB.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);

        binding.currentDatePickerDB.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        });

        binding.markTodayAttendanceDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllow) {
                    dialog.show();
                } else {
                    Toast.makeText(DashBoard.this, "Need faculty permission!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValid(pinEditText.getText().toString())) {
                    Date currentDate = new Date();
                    attendance++;
                    binding.dashBoardProgressbarDB.setVisibility(View.VISIBLE);
                    reference.child("Students").child(uid).child("attendance").setValue(attendance).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                String dateFormat = "yyyy-MM-dd";
                                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                                Date currentDate = new Date();
                                String dateString = sdf.format(currentDate);

                                String tempId = reference.push().getKey();
                                AttendanceModel model = new AttendanceModel(dateString, String.valueOf(attendance));
                                reference.child("AttendanceDates").child(uid).child(tempId).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            reference.child("Students").child(uid).child("date").setValue(dateString).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        binding.dashBoardProgressbarDB.setVisibility(View.GONE);
                                                        dialog.dismiss();
                                                        DashBoard.this.recreate();
                                                    } else {
                                                        binding.dashBoardProgressbarDB.setVisibility(View.GONE);
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    binding.dashBoardProgressbarDB.setVisibility(View.GONE);
                                                }
                                            });
                                        } else {
                                            binding.dashBoardProgressbarDB.setVisibility(View.GONE);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        binding.dashBoardProgressbarDB.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                binding.dashBoardProgressbarDB.setVisibility(View.GONE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            binding.dashBoardProgressbarDB.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private boolean checkValid(String pin) {
        if (pin.isEmpty()) {
            Toast.makeText(this, "Please enter your pin.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Objects.equals(Integer.parseInt(pin), securityPin)) {
            Toast.makeText(this, "Invalid security pin.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void getMyData() {
        binding.dashBoardProgressbarDB.setVisibility(View.VISIBLE);
        reference.child("Students").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.dashBoardProgressbarDB.setVisibility(View.GONE);
                    StudentModel model = snapshot.getValue(StudentModel.class);
                    binding.studentNameDB.setText(model.getName());
                    binding.studentRollNoDB.setText(model.getRoleNumber());
//                binding.studentAbsentDB.setText(String.valueOf(model.getAbsents()));
                    binding.studentPresentDB.setText(String.valueOf(model.getAttendance()));
                    attendance = model.getAttendance();
                    securityPin = Integer.parseInt(model.getSecurityPin());
                    course = model.getCourse();
                    branch = model.getBranch();
                    session = model.getSession();
                    getAbsents();

                    String dateFormat = "yyyy-MM-dd";
                    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

                    Date retrievedDate = null;
                    try {
                        retrievedDate = sdf.parse(model.getDate());
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
                                binding.markTodayAttendanceDB.setVisibility(View.VISIBLE);
                            } else if (retrievedDate.after(parsedCurrentDate)) {
                                System.out.println("Retrieved date is after the current date (using after()).");
                            } else {
                                System.out.println("Retrieved date is equal to the current date (using equals()).");
                                binding.markTodayAttendanceDB.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    binding.dashBoardProgressbarDB.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.dashBoardProgressbarDB.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getAbsents() {
        reference.child("Courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CourseModel model = snapshot1.getValue(CourseModel.class);
                        if (Objects.equals(model.getCourse(), course) && Objects.equals(model.getBranch(), branch) && Objects.equals(model.getSession(), session)) {
                            pushId = model.getPushId();
                            int absent = model.getClassCount() - attendance;
                            binding.studentAbsentDB.setText(String.valueOf(absent));
                            if (Objects.equals(model.getIsTodayHoliday(), "true")) {
                                isAllow = false;
                            } else {
                                isAllow = true;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}