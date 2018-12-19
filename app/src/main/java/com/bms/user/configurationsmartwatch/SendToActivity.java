package com.bms.user.configurationsmartwatch;

import android.content.Context;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
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

public class SendToActivity extends WearableActivity {

    private Spinner spinnerSendTo;
    private Button btnSave;
    private ArrayAdapter<String> adapter;
    private String chooseSendTo = "ccu", ipAddressCCU="192.168.0.1", ipAddressDv="192.168.0.2";
    private long smartwatchId;
    private EditText editTextIpCCU, editTextIpDv, editTextSmartwatchID;
    ArrayList<String> names=new ArrayList<String>();
    ModelSettingSmartwatch modelSettingSmartwatch, resModelSettingSmartwatch;
    DbManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_send_to);
        setAmbientEnabled();

        Context ctx = getApplicationContext();
        spinnerSendTo=(Spinner) findViewById(R.id.spinnerSendTo);
        editTextIpCCU = (EditText)findViewById(R.id.editTextIpCCU);
        editTextIpDv = (EditText)findViewById(R.id.editTextIpDriverView);
        editTextSmartwatchID = (EditText)findViewById(R.id.editTextSmartwatchID);
        btnSave=(Button) findViewById(R.id.btnSaveSendTo);

        //ADAPTER
        names.add("choose");
        names.add("ccu");
        names.add("driverview");
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,names);
        spinnerSendTo.setAdapter(adapter);

        dbManager = new DbManager(ctx);
        modelSettingSmartwatch = new ModelSettingSmartwatch();
        modelSettingSmartwatch = dbManager.getSettingSmartwatch();
        editTextIpCCU.setText(modelSettingSmartwatch.getIpCCU());
        editTextIpDv.setText(modelSettingSmartwatch.getIpDriverView());
        editTextSmartwatchID.setText(Long.toString(modelSettingSmartwatch.getSmartwatchId()));
        String selectedSendToData = modelSettingSmartwatch.getSendTo();

        if (selectedSendToData.equalsIgnoreCase("ccu")){
            spinnerSendTo.setSelection(1);
        }else if (selectedSendToData.equalsIgnoreCase("driverview")){
            spinnerSendTo.setSelection(2);
        }else{
            spinnerSendTo.setSelection(0);
        }

        spinnerSendTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                chooseSendTo = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        //EVENTS
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                resModelSettingSmartwatch = new ModelSettingSmartwatch();
                ipAddressCCU = editTextIpCCU.getText().toString();
                ipAddressDv = editTextIpDv.getText().toString();
                smartwatchId = Long.valueOf(editTextSmartwatchID.getText().toString());


                resModelSettingSmartwatch.setSendTo(chooseSendTo);
                resModelSettingSmartwatch.setIpCCU(ipAddressCCU);
                resModelSettingSmartwatch.setIpDriverView(ipAddressDv);
                resModelSettingSmartwatch.setSmartwatchId(smartwatchId);

                int result = dbManager.updateSettingSmartwatch(resModelSettingSmartwatch);

                if( result != 0)
                {
                    Toast.makeText(getApplicationContext(), "Data Berhasil diupdate", Toast.LENGTH_SHORT).show();

                }else
                {
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
