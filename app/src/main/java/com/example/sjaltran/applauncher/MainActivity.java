package com.example.sjaltran.applauncher;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    GridView gridView;
    CustomGrid adapter;
    PackageManager packManager;
    private EditText filterText;
    private TextView msg;
    private ImageButton searchBtn;
    List<ApplicationInfo> app_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getInstalledApplication List
        loadApps();
        //initialization of the UI elements
        filterText = (EditText)findViewById(R.id.editText);
        msg = (TextView)findViewById(R.id.list_empty);
        msg.setText(R.string.emptyMsg);
        searchBtn = (ImageButton) findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getFilter().filter(filterText.getText());
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                
            }
        });
//        filterText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                adapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        // Show the app icons and titles in a GridView
        gridView = (GridView) findViewById(R.id.simpleGridView);
        adapter = new CustomGrid(MainActivity.this,app_list);
        gridView.setAdapter(adapter);
        gridView.setTextFilterEnabled(false);
        gridView.setEmptyView(msg);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at " + ((ApplicationInfo)parent.getItemAtPosition(position)).loadLabel(packManager), Toast.LENGTH_SHORT).show();
                //Launch the selected application
                ApplicationInfo app = (ApplicationInfo) parent.getItemAtPosition(position);
                try {
                    Intent intent = packManager
                            .getLaunchIntentForPackage(app.packageName);

                    if (null != intent) {
                        startActivity(intent);
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void loadApps() {
        // Create Intent object for ACTION_MAIN
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        // We wish to query the installed apps so let specify CATEGORY_LAUNCHER to the intent's category
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // Query the installed apps
        packManager= getPackageManager();
        app_list = checkForLaunchIntent(packManager.getInstalledApplications(PackageManager.GET_META_DATA));
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : list) {
            try {
                if (null != packManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applist;
    }
}

