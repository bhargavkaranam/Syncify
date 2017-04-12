package com.example.root.syncify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main9Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {

                handleSendText(intent);
            }
        }
    }
    public void handleSendText(Intent intent)
    {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String channel = (sharedpreferences.getString("email",""));
            String title = "Clipboard";
            String text = sharedText;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("clipboard");


            String postId = myRef.push().getKey();
            Post post = new Post(channel,title,text);
            myRef.child(postId).setValue(post);
            Toast.makeText(this,"Copied and saved",Toast.LENGTH_LONG).show();
            Intent i = new Intent(this,Main3Activity.class);
            startActivity(i);

        }
    }
}
