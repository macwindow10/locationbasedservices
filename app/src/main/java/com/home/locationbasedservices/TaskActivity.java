package com.home.locationbasedservices;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.home.locationbasedservices.model.Task;

import java.util.Calendar;

public class TaskActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceTasks;
    private Calendar calendarReminderDateTime = Calendar.getInstance();
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private RadioButton radioButtonNotification;
    private RadioButton radioButtonRingerAlarm;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button buttonSelectLocationFromMap;
    private Button buttonSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task);

        firebaseDatabase = FirebaseDatabase.getInstance("https://androidlocationbasedservices-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("UserTasks");

        editTextTitle = findViewById(R.id.edittext_title);
        editTextDescription = findViewById(R.id.edittext_description);
        buttonSelectLocationFromMap = findViewById(R.id.button_select_location);
        editTextLatitude = findViewById(R.id.edittext_latitude);
        editTextLongitude = findViewById(R.id.edittext_longitude);
        radioButtonNotification = findViewById(R.id.radio_button_notification);
        radioButtonRingerAlarm = findViewById(R.id.radio_button_ringer_alarm);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);
        buttonSave = findViewById(R.id.button_save);

        datePicker.init(calendarReminderDateTime.get(Calendar.YEAR), calendarReminderDateTime.get(Calendar.MONTH), calendarReminderDateTime.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                calendarReminderDateTime.set(Calendar.YEAR, year);
                calendarReminderDateTime.set(Calendar.MONTH, month);
                calendarReminderDateTime.set(Calendar.DAY_OF_MONTH, day);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                calendarReminderDateTime.set(Calendar.HOUR, hour);
                calendarReminderDateTime.set(Calendar.MINUTE, minute);
            }
        });

        buttonSelectLocationFromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                String latitude = editTextLatitude.getText().toString();
                String longitude = editTextLongitude.getText().toString();
                boolean notification = radioButtonNotification.isChecked();
                boolean ringerAlarm = radioButtonRingerAlarm.isChecked();

                if (TextUtils.isEmpty(title)) {
                    return;
                }
                if (TextUtils.isEmpty(latitude)) {
                    return;
                }
                if (TextUtils.isEmpty(longitude)) {
                    return;
                }
                addDataToFirebase(title, description, latitude, longitude, notification, ringerAlarm);
            }
        });
    }

    private void addDataToFirebase(String title, String description, String latitude, String longitude, boolean notification, boolean ringerAlarm) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isLoggedIn = sharedPreferences.getBoolean(Common.PREFERENCE_IS_LOGGED_IN, false);
        String userEmail = sharedPreferences.getString(Common.PREFERENCE_USER_EMAIL, "");
        if (!isLoggedIn) {
            return;
        }
        if (TextUtils.isEmpty(userEmail)) {
            return;
        }

        Task task = new Task();
        task.setUserEmail(userEmail);
        task.setTitle(title);
        task.setDescription(description);
        task.setLatitude(Double.parseDouble(latitude));
        task.setLongitude(Double.parseDouble(longitude));
        task.setNotificationReminder(notification);
        task.setAlarmRingerReminder(ringerAlarm);
        task.setCreationDate(Calendar.getInstance().getTime());
        task.setReminderDate(calendarReminderDateTime.getTime());

        databaseReferenceTasks = databaseReference.push();
        databaseReferenceTasks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReferenceTasks.setValue(task);

                Toast.makeText(TaskActivity.this, "Task saved", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TaskActivity.this, "Failed to save Task " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
