package com.example.root.syncify;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.UUID;

public class Main3Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main3);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String channel = (sharedpreferences.getString("email",""));

        TextView tv = (TextView)headerView.findViewById(R.id.emailView);
        tv.setText(channel);

        final ListView lv = (ListView)findViewById(R.id.postList);


        //FIREBASE PART
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("posts");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                final ArrayList<String> myPostList = new ArrayList<String>();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    myPostList.add(post.text);

                    Toast.makeText(Main3Activity.this,post.title,Toast.LENGTH_LONG);
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main3Activity.this,android.R.layout.simple_list_item_1,android.R.id.text1,myPostList);
                lv.setAdapter(adapter);
                
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String)lv.getItemAtPosition(position);
                        Intent intent = new Intent(Main3Activity.this,Main4Activity.class);
                        intent.putExtra("text",item);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(Main3Activity.this,"Error",Toast.LENGTH_LONG).show();
                // ...
            }
        };
        myRef.addValueEventListener(postListener);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toast.makeText(this,"called",Toast.LENGTH_LONG).show();
        if (id == R.id.nav_notes) {
            // Handle the camera action
        } else if (id == R.id.nav_photos) {
            Intent intent = new Intent(this,Main7Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_alarms) {
            Intent intent = new Intent(this,Main8Activity.class);
            startActivity(intent);
        } else if(id == R.id.nav_clipboard) {
            Intent intent = new Intent(this,Main10Activity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void expandTextView(View v)
    {
        Intent intent = new Intent(this,Main4Activity.class);
        startActivity(intent);
    }


    public void launchAlarm(View v)
    {
        int mHour,mMinute;

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        //TRIGGER ALARM HERE
                        Toast.makeText(Main3Activity.this,"Alarm set.",Toast.LENGTH_LONG).show();
                        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        String channel = (sharedpreferences.getString("email",""));
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("alarms");
                        Alarm arm = new Alarm(channel,hourOfDay,minute);
                        String postId = myRef.push().getKey();
                        myRef.child(postId).setValue(arm);

                        c.set(Calendar.MINUTE,minute);
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
                        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

                        alarmManager.set(AlarmManager.RTC, c.getTimeInMillis(), pendingIntent);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;

    public void launchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);

        startActivityForResult(intent, TAKE_PICTURE);
    }
    private StorageReference mStorageRef;
    static int RESULT = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT);
                    } else {
                        //your code

                        Uri selectedImage = imageUri;
                        getContentResolver().notifyChange(selectedImage, null);
                        Uri file = Uri.fromFile(new File(selectedImage.getPath()));
                        StorageReference riversRef = mStorageRef.child(UUID.randomUUID().toString());

                        riversRef.putFile(file)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Get a URL to the uploaded content
                                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                        String channel = (sharedpreferences.getString("email",""));
                                        String title = "Photo";
                                        String text = downloadUrl.toString();
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("images");


                                        String postId = myRef.push().getKey();
                                        Post post = new Post(channel,title,text);
                                        myRef.child(postId).setValue(post);

                                        Toast.makeText(Main3Activity.this, downloadUrl.toString(), Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        // ...
                                    }
                                });

                    }
                }
        }
    }

}
