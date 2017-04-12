package com.example.root.syncify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main4Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Intent intent = getIntent();
        if(intent.getStringExtra("text") != null)
        {
            EditText et = (EditText)findViewById(R.id.editText5);
            et.setText(intent.getStringExtra("text"));
        }
    }

    public void saveToDatabase(View v)
    {
        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String channel = (sharedpreferences.getString("email",""));
        String title = ((EditText)findViewById(R.id.editText3)).getText().toString();
        String text = ((EditText)findViewById(R.id.editText5)).getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("posts");


        String postId = myRef.push().getKey();
        Post post = new Post(channel,title,text);
        myRef.child(postId).setValue(post);
        Toast.makeText(this,"Note saved.",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,Main3Activity.class);
        startActivity(intent);

    }
}


