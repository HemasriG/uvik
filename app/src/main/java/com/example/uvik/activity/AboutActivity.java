package com.example.uvik.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.uvik.BuildConfig;
import com.example.uvik.R;

public class AboutActivity extends AppCompatActivity {

    TextView version,author;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        version = (TextView) findViewById(R.id.version_txt);
        author = (TextView)findViewById(R.id.author);
        author.setText("2022 , Hemasri");
        version.setText("Version "+BuildConfig.VERSION_NAME);
    }
}