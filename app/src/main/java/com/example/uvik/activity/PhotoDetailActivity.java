package com.example.uvik.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uvik.R;
import com.example.uvik.model.GetResponse;
import com.example.uvik.model.MainModelData;
import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {

    TextView locationTxt,officeName,name;
    ImageView imageView;
    GetResponse data;
    MainModelData modelData;
    String photoUrl;
    LinearLayout mainL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentOrientation = this.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.photo_detail_landscape);
        }else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_photo_detail);
        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        officeName = (TextView) findViewById(R.id.office_name);
        name = (TextView) findViewById(R.id.name);
        locationTxt = (TextView) findViewById(R.id.location_text);
        imageView = (ImageView) findViewById(R.id.image_view);
        mainL = (LinearLayout) findViewById(R.id.main_l);
        if (getIntent()!=null){
            data = (GetResponse) getIntent().getSerializableExtra("data");
            modelData = (MainModelData) getIntent().getSerializableExtra("modelData");
            photoUrl = getIntent().getStringExtra("pic");
        }
        if (savedInstanceState!=null){
            data = (GetResponse) savedInstanceState.getSerializable("data");
            modelData = (MainModelData) savedInstanceState.getSerializable("modelData");
            photoUrl = savedInstanceState.getString("pic");
        }
        updateView(data,modelData,photoUrl);
    }

    private void updateView(GetResponse data, MainModelData modelData, String photoUrl) {
        locationTxt.setText(data.getNormalizedInput().getCity()+", "+data.getNormalizedInput().getState()+" "+data.getNormalizedInput().getZip());
        officeName.setText(modelData.getOfficeName());
        name.setText(modelData.getOfficialName());
        if (modelData.getParty().contains("Democratic")){
            mainL.setBackgroundColor(getResources().getColor(R.color.purple_500));
        }else if (modelData.getParty().contains("Republican")){
            mainL.setBackgroundColor(getResources().getColor(R.color.red));
        }else{
            mainL.setBackgroundColor(getResources().getColor(R.color.black));
        }
        if (photoUrl!=null) {
            Picasso.get().load(photoUrl)
                    .error(R.drawable.brokenimg)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data",data);
        outState.putSerializable("modelData",modelData);
        outState.putSerializable("photoUrl",photoUrl);
        Toast.makeText(this, outState.get("photoUrl").toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        data = (GetResponse) savedInstanceState.getSerializable("data");
        modelData = (MainModelData) savedInstanceState.getSerializable("modelData");
        photoUrl = savedInstanceState.getString("pic");
        updateView(data,modelData,photoUrl);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.photo_detail_landscape);
        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_photo_detail);
        }
    }
}