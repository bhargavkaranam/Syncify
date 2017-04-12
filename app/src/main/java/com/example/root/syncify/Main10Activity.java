package com.example.root.syncify;

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

public class Main10Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main10);
        final ListView lv = (ListView) findViewById(R.id.clipboardList);


        //FIREBASE PART
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("clipboard");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                final ArrayList<String> myPostList = new ArrayList<String>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    myPostList.add(post.text);

                    Toast.makeText(Main10Activity.this, post.title, Toast.LENGTH_LONG);
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main10Activity.this, android.R.layout.simple_list_item_1, android.R.id.text1, myPostList);
                lv.setAdapter(adapter);

            }

            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(Main10Activity.this, "Error", Toast.LENGTH_LONG).show();
                // ...
            }
        };
        myRef.addValueEventListener(postListener);
    }
}