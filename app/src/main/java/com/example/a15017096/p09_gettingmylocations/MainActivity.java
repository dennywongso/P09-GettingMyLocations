package com.example.a15017096.p09_gettingmylocations;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnStart, btnStop, btnCheck;
    TextView tvLat, tvLong, tvCurLat, tvCurLong;
    String folderLocation;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Test";
        btnCheck = (Button)findViewById(R.id.btnCheck);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnStop = (Button)findViewById(R.id.btnStop);
        tvLat = (TextView) findViewById(R.id.tvLat);
        tvLong = (TextView)findViewById(R.id.tvLong);
        tvCurLat = (TextView) findViewById(R.id.tvCurLat);
        tvCurLong = (TextView)findViewById(R.id.tvCurLong);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck_Fine != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

            // stops the action from proceeding further as permission not
            //  granted yet
            return;
        }
        File targetFile = new File(folderLocation, "location.txt");
        if (targetFile.exists() == true) {
            String data1 = "";
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                while (line != null) {
                    data1 = line;
                    line = br.readLine();
                }
                br.close();
                reader.close();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Failed to read!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            String[] separated = data1.split(",");
            tvLat.setText("Latitude:" + separated[0]);
            tvLong.setText("Longitude:"+separated[1]);
        }
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyService.class);
                startService(i);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyService.class);
                stopService(i);

                    File targetFile = new File(folderLocation, "location.txt");
                    if (targetFile.exists() == true){
                        String data ="";
                        String newData = "";
                        try {
                            FileReader reader = new FileReader(targetFile);
                            BufferedReader br = new BufferedReader(reader);
                            String line = br.readLine();
                            while (line != null){
                                newData = line;
                                String[] separated = line.split(",");
                                LatLng east = new LatLng(Double.parseDouble(separated[0]), Double.parseDouble(separated[1]));
                                Marker e = map.addMarker(new
                                        MarkerOptions()
                                        .position(east)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                data += line + "\n";
                                line = br.readLine();
                            }
                            br.close();
                            reader.close();
                            String[] x = newData.split(",");
                            tvCurLat.setText("Latitude:" + x[0]);
                            tvCurLong.setText("Longitude:"+x[1]);

                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to read!",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this,data+"",Toast.LENGTH_SHORT).show();
                    }


            }
        });
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                startActivity(intent);

            }
        });
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)
                fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                // stops the action from proceeding further as permission not
                //  granted yet
                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);

                }


                UiSettings ui = map.getUiSettings();
                ui.setZoomControlsEnabled(true);

                LatLng sgp = new LatLng(1.290270, 103.851959);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(sgp,
                        10));


                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Toast.makeText(MainActivity.this, marker.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

            }
        });


    }
}
