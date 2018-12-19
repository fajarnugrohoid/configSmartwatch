package com.bms.user.configurationsmartwatch;

import android.content.Context;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bms.user.database.DbManager;
import com.bms.user.model.ModelSettingSmartwatch;

import java.util.ArrayList;

public class SetCompassActivity extends WearableActivity {

    private Button btnSave;
    private ArrayAdapter<String> adapter;
    private Switch switchIsCompassExternal;
    DbManager dbManager;
    private String setCompass = "0";
    private String TAG = "setCompassActivity";
    Integer statusCompassExternal = null;
    ModelSettingSmartwatch modelSettingSmartwatch, resModelSettingSmartwatch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_set_compass);
        setAmbientEnabled();

        Context ctx = getApplicationContext();

        switchIsCompassExternal=(Switch)findViewById(R.id.switchIsCompassExternal);
        btnSave=(Button) findViewById(R.id.btnSaveCompass);

        dbManager = new DbManager(ctx);
        modelSettingSmartwatch = new ModelSettingSmartwatch();
        modelSettingSmartwatch = dbManager.getSettingSmartwatch();
        Integer resCompassExternal =modelSettingSmartwatch.getIsCompassExternal();
        if (resCompassExternal==1){
            switchIsCompassExternal.setChecked(true);
        }else{
            switchIsCompassExternal.setChecked(false);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                resModelSettingSmartwatch = new ModelSettingSmartwatch();

                // check current state of a Switch (true or false).
                Boolean switchState = switchIsCompassExternal.isChecked();
                if (switchState==true){
                    statusCompassExternal = 1;
                }else{
                    statusCompassExternal = 0;
                }
                resModelSettingSmartwatch.setIsCompassExternal(statusCompassExternal);
                Log.d(TAG, "switchIsCompassExternal:" + switchState);
                int result = dbManager.updateSettingSmartwatch(resModelSettingSmartwatch);

                if( result != 0)
                {
                    Toast.makeText(getApplicationContext(), "Data Berhasil diupdate", Toast.LENGTH_SHORT).show();

                }else
                {
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                }

                //CLOSE

            }
        });
    }
}
