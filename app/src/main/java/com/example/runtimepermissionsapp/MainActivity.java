package com.example.runtimepermissionsapp;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private File file;
    private List<String> myList;
    private ListView fileListView;

    private final static int REQUEST_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileListView = findViewById(R.id.tv);
        myList = new ArrayList<>();
        requestPermissions(new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            String root_sd = Environment.getExternalStorageDirectory().toString();
            file = new File(root_sd);
            File[] list = file.listFiles();

            for (File value : Objects.requireNonNull(list)) {
                myList.add(value.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, myList);
            fileListView.setAdapter(adapter);
            fileListView.setOnItemClickListener(this);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        File currentFile = new File(file, myList.get(position));
        if (currentFile.isDirectory()) {
            if (currentFile.length() == 0) {
                return;
            }
            file = new File(file, myList.get(position));
            File[] list = file.listFiles();

            myList.clear();

            for (File value : Objects.requireNonNull(list)) {
                myList.add(value.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, myList);
            fileListView.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        String parent = file.getParent();
        file = new File(Objects.requireNonNull(parent)) ;
        File[] list = file.listFiles();

        myList.clear();

        for (File value : Objects.requireNonNull(list)) {
            myList.add(value.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, myList);
        fileListView.setAdapter(adapter);
    }
}
