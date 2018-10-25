package com.bms.user.configurationsmartwatch;

import android.content.Context;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bms.user.database.DbManager;
import com.bms.user.model.ModelSettingSmartwatch;

import java.util.ArrayList;

public class SetGroupActivity extends WearableActivity {

    private Spinner spinnerSendTo;
    private EditText editTextGroup;
    private Button btnSave;
    private ArrayAdapter<String> adapter;
    private String chooseSendTo = "ccu";
    ArrayList<String> names=new ArrayList<String>();
    DbManager dbManager;
    private String setGroup = "0";
    private String TAG = "setGroupActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_set_group);
        setAmbientEnabled();

        Context ctx = getApplicationContext();
        editTextGroup=(EditText)findViewById(R.id.editTextGroup);
        btnSave=(Button) findViewById(R.id.btnSaveGroup);

        dbManager = new DbManager(ctx);
        ModelSettingSmartwatch modelSettingSmartwatch = new ModelSettingSmartwatch();
        modelSettingSmartwatch = dbManager.getSettingSmartwatch();
        editTextGroup.setText(modelSettingSmartwatch.getGroup());

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                setGroup = editTextGroup.getText().toString();
                Log.d(TAG, "getTextData:" + editTextGroup.getText().toString() + "-setGroup:" + setGroup);
                int result = dbManager.updateSettingSmartwatch("", "", "", setGroup, "", "");

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
