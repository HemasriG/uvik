package com.example.uvik.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uvik.R;
import com.example.uvik.adapter.MainAdapter;
import com.example.uvik.model.GetResponse;
import com.example.uvik.model.MainModelData;
import com.example.uvik.model.NormalizedInput;
import com.example.uvik.service.ApiModule;
import com.example.uvik.service.Service;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView locationText;
    GetResponse getResponse;
    MainAdapter mainAdapter;
    LinearLayout noDataL;
    Location location;
    Geocoder geocoder;
    List<Address> addresses;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        locationText = (TextView) findViewById(R.id.location_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        noDataL = (LinearLayout)findViewById(R.id.no_data_l);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        getLocation();

    }

    private void getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        geocoder= new Geocoder(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                        Address address = addresses.get(0);
                        Log.d("postalCode",address.getPostalCode());
                        String postalCode = address.getPostalCode();
                        getData(postalCode);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getData(String postalCode) {
        Service service = ApiModule.getInstance().getService();
        service.getRepresentativesResponse("AIzaSyDnddIYAHiLmG4jrGp44KkFt6cLZhpizn4","10002").enqueue(new Callback<GetResponse>() {
            @Override
            public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                if (response.isSuccessful()){
                    if (response.code()==200) {
                        if (response.body() != null) {
                            getResponse = response.body();
                            updateView(getResponse.getNormalizedInput());
                            setAdapter(getResponse);
                        }
                    }else if (response.code()==400||response.code()==403){
                        Toast.makeText(MainActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                        noDataL.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed to connect server",Toast.LENGTH_SHORT).show();
                noDataL.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setAdapter(GetResponse response) {

        List<MainModelData> modelDataList = new ArrayList<>();
        for (int j=0;j<response.getOffices().size();j++){
            if (response.getOffices().get(j).getOfficialIndices()!=null&&response.getOffices().get(j).getOfficialIndices().size()>0){
                for (int i=0;i<response.getOffices().get(j).getOfficialIndices().size();i++){
                    if (response.getOfficials()!=null&&response.getOfficials().size()>0){
                        MainModelData mainModelData = new MainModelData();
                        Log.d("datanames",response.getOffices().get(j).getName());
                        Log.d("data", String.valueOf(response.getOffices().get(j).getOfficialIndices().get(i)));
                        Log.d("dataName",response.getOfficials().get(response.getOffices().get(j).getOfficialIndices().get(i)).getName());
                        mainModelData.setOfficeName(response.getOffices().get(j).getName());
                        mainModelData.setOfficialName(response.getOfficials().get(response.getOffices().get(j).getOfficialIndices().get(i)).getName());
                        mainModelData.setParty(response.getOfficials().get(response.getOffices().get(j).getOfficialIndices().get(i)).getParty());
                        modelDataList.add(mainModelData);
                        Log.d("dataList",new Gson().toJson(modelDataList));
                    }
                }
            }
        }
        if (modelDataList!=null&&modelDataList.size()>0) {
            recyclerView.setVisibility(View.VISIBLE);
            noDataL.setVisibility(View.GONE);
            mainAdapter = new MainAdapter(this, modelDataList,response);
            recyclerView.setAdapter(mainAdapter);
        }else{
            noDataL.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void updateView(NormalizedInput normalizedInput) {
        if (normalizedInput!=null) {
            locationText.setText((normalizedInput.getCity()) + ", " + (normalizedInput.getState()) + " " +
                    (normalizedInput.getZip()));
        }else{
            locationText.setText("No Data for Location");
        }
    }

    public void goToOfficialActivity(GetResponse response, MainModelData data) {
        Intent intent = new Intent(this,OfficialActivity.class);
        intent.putExtra("data",response);
        intent.putExtra("modelData",data);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.info:
                Intent intent = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.location:
                openAlertDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAlertDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog);
        EditText editText = (EditText) findViewById(R.id.zip);
        TextView cancel = (TextView) findViewById(R.id.btn_cancel);
        TextView ok = (TextView) findViewById(R.id.btn_ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = editText.getText().toString();
                if (!code.equals(""))
                    getData(code);
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}