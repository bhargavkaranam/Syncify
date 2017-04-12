package com.example.root.syncify;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Main7Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        final LinearLayout rl = (LinearLayout) findViewById(R.id.activity_main7);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("images");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    LinearLayout.LayoutParams imParams =
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ImageView image = new ImageView(Main7Activity.this);

                    Post post = postSnapshot.getValue(Post.class);
                    Picasso.with(Main7Activity.this).load(post.text).resize(500,500).into(image);
                    image.setScaleType(ImageView.ScaleType.CENTER);

                    rl.addView(image,imParams);
//                    new ImageLoadTask(post.text,image).execute();
//                    image.setImageBitmap(getBitmapFromURL(post.text));
//                    myPostList.add(post.text);

                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main7Activity.this,android.R.layout.simple_list_item_1,android.R.id.text1,myPostList);
//                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(Main7Activity.this,"Error",Toast.LENGTH_LONG).show();
                // ...
            }
        };
        myRef.addValueEventListener(postListener);

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}
