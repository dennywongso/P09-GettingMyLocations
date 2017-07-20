package com.example.a15017096.p09_gettingmylocations;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {
    String folderLocation;
    Button btnRefresh;
    ListView lvCoordinate;
    ArrayAdapter<String> aa;
    ArrayList<String> al;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        btnRefresh = (Button)findViewById(R.id.btnRefresh);
        lvCoordinate = (ListView)findViewById(R.id.lvCoordinate);
        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Test";
        al = new ArrayList<>();
        aa = new ArrayAdapter<String>(DisplayActivity.this, android.R.layout.simple_list_item_1, al);
        lvCoordinate.setAdapter(aa);
        File targetFile = new File(folderLocation, "location.txt");
        if (targetFile.exists() == true){
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                while (line != null){
                    if(al.size()>=15) {
                        al.remove(15);
                        al.add(0,line);
                        line = br.readLine();
                    } else {
                        al.add(0,line);
                        line = br.readLine();
                    }

                }
                br.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        aa.notifyDataSetChanged();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al.clear();
                File targetFile = new File(folderLocation, "location.txt");
                if (targetFile.exists() == true){
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null){
                            if(al.size()>=15) {
                                al.remove(15);
                                al.add(0,line);
                                line = br.readLine();
                            } else {
                                al.add(0,line);
                                line = br.readLine();
                            }
                        }
                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                aa.notifyDataSetChanged();
            }
        });

    }
}
