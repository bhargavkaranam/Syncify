package com.example.root.syncify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Main8Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        final ListView lv = (ListView)findViewById(R.id.alarmList);
        final Calendar c = Calendar.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("alarms");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                final ArrayList<String> myPostList = new ArrayList<String>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Alarm post = postSnapshot.getValue(Alarm.class);
                    myPostList.add("Alarm at " + post.hour + ":" + post.minute);
                    c.set(Calendar.MINUTE,post.minute);
                    c.set(Calendar.HOUR_OF_DAY,post.hour);
                    if(System.currentTimeMillis() < c.getTimeInMillis()) {

                        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

                        alarmManager.set(AlarmManager.RTC, c.getTimeInMillis(), pendingIntent);
                    }

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main8Activity.this,android.R.layout.simple_list_item_1,android.R.id.text1,myPostList);
                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(Main8Activity.this,"Error",Toast.LENGTH_LONG).show();
                // ...
            }
        };
        myRef.addValueEventListener(postListener);
    }
}
