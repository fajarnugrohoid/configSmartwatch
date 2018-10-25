package com.bms.user.configurationsmartwatch;

import android.content.Context;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bms.user.database.DbManager;
import com.bms.user.model.ModelSetIp;
import com.bms.user.model.ModelSettingSmartwatch;

public class SetIpActivity extends WearableActivity {
    private Button btnSave;
    private ArrayAdapter<ModelSettingSmartwatch> adapter;
    DbManager dbManager;
    private int choosedIdx = 0;
    private EditText editTextSSIDName;
    private EditText editTextSSIDPassword;
    private EditText editTextMyIp;
    private EditText editTextMyGateway;

    private String txtSSIDName="";
    private String txtSSIDPassword="";
    private String txtMyIp="";
    private String txtMyGateway="";

    private String TAG = "SetIpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_set_ip);
        setAmbientEnabled();

        Context ctx = getApplicationContext();

        editTextSSIDName = (EditText)findViewById(R.id.editTextSSIDName);
        editTextSSIDPassword = (EditText)findViewById(R.id.editTextSSIDPassword);
        editTextMyIp = (EditText)findViewById(R.id.editTextMyIp);
        editTextMyGateway = (EditText)findViewById(R.id.editTextMyGateway);
        btnSave=(Button) findViewById(R.id.btnSave);


        dbManager = new DbManager(ctx);
        ModelSetIp modelSetIp = new ModelSetIp();
        modelSetIp = dbManager.getWifiConfig();
        editTextSSIDName.setText(modelSetIp.getSSIDName());
        editTextSSIDPassword.setText(modelSetIp.getSSIDPassword());
        editTextMyIp.setText(modelSetIp.getMyIp());
        editTextMyGateway.setText(modelSetIp.getMyGateway());

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                txtSSIDName = editTextSSIDName.getText().toString();
                txtSSIDPassword = editTextSSIDPassword.getText().toString();
                txtMyIp = editTextMyIp.getText().toString();
                txtMyGateway = editTextMyGateway.getText().toString();
                Log.d(TAG, "1." + txtSSIDName + "-" + txtSSIDPassword + "-" + txtMyIp + "-" + txtMyGateway);

                int result = dbManager.updateSetIp(txtSSIDName, txtSSIDPassword, txtMyIp, txtMyGateway);

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
