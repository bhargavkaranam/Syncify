package com.example.root.syncify;

import android.app.Activity;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Main6Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
    }

    public void setAlarm(View v)
    {
        String message = ((EditText)findViewById(R.id.editText6)).getText().toString();
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_MESSAGE,message);
        startActivity(i);
        this.finish();
    }

    public void close(View v)
    {
        this.finish();
    }
}
