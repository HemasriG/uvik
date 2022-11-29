package com.example.uvik.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uvik.R;
import com.example.uvik.model.Address;
import com.example.uvik.model.Channels;
import com.example.uvik.model.GetResponse;
import com.example.uvik.model.MainModelData;
import com.example.uvik.model.Officials;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OfficialActivity extends AppCompatActivity {

    GetResponse data;
    TextView officeName,name,party,address,phone,email,website,locationTxt;
    ImageView img,youtube,googlePlus,twitter,fb;
    LinearLayout websiteL,addressL,phoneL,emailL,socialL,mainL;
    MainModelData modelData;
    String fbId,googleId,youtubeId,twitterId,photoUrl,phoneString,websiteString,emailString,addressString;
    List<Channels> channelsList;
    Channels channel;
    boolean picAvailable=false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null)
            onRestoreInstanceState(savedInstanceState);
        int currentOrientation = this.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.official_landscape);
        }else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_official);
        }
        officeName = (TextView) findViewById(R.id.office_name);
        name = (TextView) findViewById(R.id.name);
        party = (TextView) findViewById(R.id.party);
        address = (TextView) findViewById(R.id.address);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        website = (TextView) findViewById(R.id.website);
        websiteL = (LinearLayout) findViewById(R.id.website_l);
        addressL = (LinearLayout) findViewById(R.id.address_l);
        phoneL = (LinearLayout) findViewById(R.id.phone_l);
        emailL = (LinearLayout) findViewById(R.id.email_l);
        img = (ImageView) findViewById(R.id.image_view);
        locationTxt = (TextView) findViewById(R.id.location_text);
        youtube = (ImageView) findViewById(R.id.youtube);
        googlePlus = (ImageView) findViewById(R.id.googlePlus);
        twitter = (ImageView) findViewById(R.id.twitter);
        fb = (ImageView) findViewById(R.id.fb);
        socialL = (LinearLayout) findViewById(R.id.social_media_l);
        mainL = (LinearLayout) findViewById(R.id.main_l);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent()!=null){
            data = (GetResponse) getIntent().getSerializableExtra("data");
            modelData = (MainModelData) getIntent().getSerializableExtra("modelData");
        }
        updateView(data,modelData);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFacebookClick(fbId);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTwitterClick(twitterId);
            }
        });
        googlePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGooglePlusClick(googleId);
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onYoutubeClick(youtubeId);
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (picAvailable)
                    onImageClick();
                else
                    Toast.makeText(OfficialActivity.this, "Image not available", Toast.LENGTH_SHORT).show();
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneString!=null)
                    onPhoneClick(phoneString);
                else
                    Toast.makeText(OfficialActivity.this, "Phone number not available", Toast.LENGTH_SHORT).show();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailString!=null)
                    onEmailClick(emailString);
                else
                    Toast.makeText(OfficialActivity.this, "Email not available", Toast.LENGTH_SHORT).show();
            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (websiteString!=null)
                    onWebSiteClick(websiteString);
                else
                    Toast.makeText(OfficialActivity.this, "Website not available", Toast.LENGTH_SHORT).show();
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressString!=null)
                    onAddressClick(addressString);
                else
                    Toast.makeText(OfficialActivity.this, "Address not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onAddressClick(String addressString) {
        Uri uri = Uri.parse("geo:0,0?q="+addressString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void onWebSiteClick(String websiteString) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteString));
        startActivity(browserIntent);
    }

    private void onEmailClick(String emailString) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailString});
            intent.setType("plain/text");
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "There are no email client installed on your device.",Toast.LENGTH_SHORT).show();
        }
    }

    private void onPhoneClick(String phoneString) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+phoneString));
        startActivity(callIntent);
    }

    private void onImageClick() {
        Intent intent = new Intent(this,PhotoDetailActivity.class);
        intent.putExtra("data",data);
        intent.putExtra("modelData",modelData);
        if (photoUrl!=null&&!photoUrl.equals(""))
            intent.putExtra("pic",photoUrl);
        Log.d("photoUrl",photoUrl);
        startActivity(intent);
    }

    private void onYoutubeClick(String youtubeId) {
        String name = youtubeId;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    private void onGooglePlusClick(String googleId) {
        String name = googleId;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
        }
    }

    private void onTwitterClick(String twitterId) {
        Intent intent = null;
        String name = twitterId;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    private void onFacebookClick(String fbId) {
        String FACEBOOK_URL = "https://www.facebook.com/" + fbId;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + fbId;
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    private void updateView(GetResponse data,MainModelData mainModelData) {
        locationTxt.setText(data.getNormalizedInput().getCity()+", "+data.getNormalizedInput().getState()+" "+data.getNormalizedInput().getZip());
        officeName.setText(mainModelData.getOfficeName());
        name.setText(mainModelData.getOfficialName());
        party.setText("("+mainModelData.getParty()+")");
        if (mainModelData.getParty().contains("Democratic")){
            mainL.setBackgroundColor(getResources().getColor(R.color.purple_500));
        }else if (mainModelData.getParty().contains("Republican")){
            mainL.setBackgroundColor(getResources().getColor(R.color.red));
        }else{
            mainL.setBackgroundColor(getResources().getColor(R.color.black));
        }
        if (data.getOfficials()!=null&&data.getOfficials().size()>0) {
            for (int i = 0; i < data.getOfficials().size(); i++) {
                if (mainModelData.getOfficialName().equals(data.getOfficials().get(i).getName())) {
                    Officials officials = data.getOfficials().get(i);
                    if (officials.getAddress() != null && officials.getAddress().size() > 0) {
                        Address addresses = officials.getAddress().get(0);
                        address.setText(addresses.getLine1() + ", " + (addresses.getLine2() != null ? addresses.getLine2() : "") + ", " + addresses.getCity() + ", " + addresses.getState() + " " + addresses.getZip());
                        addressString = address.getText().toString();
                    } else {
                        address.setText("No Data Provided");
                    }
                    if (officials.getPhones() != null && officials.getPhones().size() > 0) {
                        phone.setText(officials.getPhones().get(0));
                        phoneString=officials.getPhones().get(0);
                    } else
                        phone.setText("No Data Provided");
                    if (officials.getEmails() != null && officials.getEmails().size() > 0){
                        email.setText(officials.getEmails().get(0));
                        emailString=officials.getEmails().get(0);
                    }else
                        email.setText("No Data Provided");
                    if (officials.getUrls()!=null&&officials.getUrls().size()>0) {
                        website.setText(officials.getUrls().get(0));
                        websiteString=officials.getUrls().get(0);
                    }else
                        website.setText("No Data Provided");
                    if (officials.getPhotoUrl()!=null){
                        Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                // Here we try https if the http image attempt failed
                                final String changedUrl = officials.getPhotoUrl().replace("http:", "https:");
                                picasso.load(changedUrl)
                                        .error(R.drawable.brokenimg)
                                        .placeholder(R.drawable.placeholder)
                                        .into(img);
                                picAvailable=true;
                                photoUrl = changedUrl;
                            }
                        }).build();
                        picasso.load(officials.getPhotoUrl())
                                .error(R.drawable.brokenimg)
                                .placeholder(R.drawable.placeholder)
                                .into(img);
                        picAvailable=true;
                        photoUrl = officials.getPhotoUrl();
                    }else{
                        Picasso.get().load(officials.getPhotoUrl())
                                .error(R.drawable.brokenimg)
                                .placeholder(R.drawable.missing)
                                .into(img);
                    }
                    if (officials.getChannels()!=null&&officials.getChannels().size()>0){
                        channelsList = officials.getChannels();
                        socialL.setVisibility(View.VISIBLE);
                        for (int j=0;j<officials.getChannels().size();j++){
                            Channels channels = officials.getChannels().get(j);
                            switch (channels.getType()){
                                case "Facebook":
                                    fb.setVisibility(View.VISIBLE);
                                    fbId = channels.getId();
                                    channel = channels;
                                    break;
                                case "Twitter":
                                    twitter.setVisibility(View.VISIBLE);
                                    twitterId = channels.getId();
                                    break;
                                case "YouTube":
                                    youtube.setVisibility(View.VISIBLE);
                                    youtubeId = channels.getId();
                                    break;
                                case "GooglePlus":
                                    googlePlus.setVisibility(View.VISIBLE);
                                    googleId = channels.getId();
                                    break;
                            }
                        }
                    }else{
                        socialL.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.official_landscape);
            /*if (getIntent()!=null){
                data = (GetResponse) getIntent().getSerializableExtra("data");
                modelData = (MainModelData) getIntent().getSerializableExtra("modelData");
            }
            updateView(data,modelData);*/
        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_official);
        }
    }
}