package com.bms.user.configurationsmartwatch;

import android.content.Context;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.bms.user.database.DbManager;
import com.bms.user.model.DataSetMaps;
import com.bms.user.model.ModelSettingSmartwatch;
import com.bms.user.model.SpinAdapter;

import java.util.ArrayList;

public class ChooseMapsActivity extends WearableActivity {
    private Spinner spinnerChooseMaps;
    private Button btnSave;
    private ArrayAdapter<ModelSettingSmartwatch> adapter;
    private SpinAdapter spinAdapter;
    private String chooseData = "cipatat topografi";
    ArrayList<ModelSettingSmartwatch> modelChooseMapsArrayList=new ArrayList<ModelSettingSmartwatch>();
    DbManager dbManager;
    ModelSettingSmartwatch modelChooseMaps;
    private int choosedIdx = 0;
    private ModelSettingSmartwatch modelSettingSmartwatch;
    private String TAG = "ChooseMapsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_choose_maps);
        setAmbientEnabled();

        Context ctx = getApplicationContext();
        spinnerChooseMaps=(Spinner) findViewById(R.id.spinnerChooseMaps);
        btnSave=(Button) findViewById(R.id.btnSave);

        //ADAPTER
        DataSetMaps dataSetMaps = new DataSetMaps();
        modelChooseMapsArrayList = dataSetMaps.getDataSettingMaps();
        //adapter=new ArrayAdapter<ModelChooseMaps>(this, android.R.layout.simple_list_item_1,modelChooseMapsArrayList);
        //spinnerChooseMaps.setAdapter(adapter);

        spinAdapter = new SpinAdapter(this, android.R.layout.simple_list_item_1, modelChooseMapsArrayList);
        spinnerChooseMaps.setAdapter(spinAdapter);

        dbManager = new DbManager(ctx);
        modelSettingSmartwatch = new ModelSettingSmartwatch();
        modelSettingSmartwatch=dbManager.getSettingSmartwatch();
        String mapsName = modelSettingSmartwatch.getMapsName();
        for (int i = 0; i <modelChooseMapsArrayList.size() ; i++) {
            Log.d(TAG, modelChooseMapsArrayList.get(i).getMapsName() + "==" + mapsName);
            if (modelChooseMapsArrayList.get(i).getMapsName().equalsIgnoreCase(mapsName)){
                spinnerChooseMaps.setSelection(modelChooseMapsArrayList.get(i).getIdx());
                break;
            }
        }

        spinnerChooseMaps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                Log.d("ChooseMaps", "pos:" + pos + "-id:" + id + "-chooseData:" + parent.getItemAtPosition(pos));
                chooseData = parent.getItemAtPosition(pos).toString();
                Log.d("ChooseMaps", "chooseData:" + chooseData);
                choosedIdx = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                ModelSettingSmartwatch resModelChooseMaps = new ModelSettingSmartwatch();

                resModelChooseMaps.setSendTo(modelChooseMapsArrayList.get(choosedIdx).getSendTo());
                resModelChooseMaps.setMapsName(modelChooseMapsArrayList.get(choosedIdx).getMapsName());
                resModelChooseMaps.setMapsPath(modelChooseMapsArrayList.get(choosedIdx).getMapsPath());
                //resModelChooseMaps.setGroup(modelChooseMapsArrayList.get(choosedIdx).getGroup());
                resModelChooseMaps.setIpCCU(modelChooseMapsArrayList.get(choosedIdx).getIpCCU());
                resModelChooseMaps.setIpDriverView(modelChooseMapsArrayList.get(choosedIdx).getIpDriverView());
                resModelChooseMaps.setImageFileNameEnding(modelChooseMapsArrayList.get(choosedIdx).getImageFileNameEnding());
                resModelChooseMaps.setMinZoomLvl(modelChooseMapsArrayList.get(choosedIdx).getMinZoomLvl());
                resModelChooseMaps.setMaxZoomLvl(modelChooseMapsArrayList.get(choosedIdx).getMaxZoomLvl());
                resModelChooseMaps.setFirstZoomLvl(modelChooseMapsArrayList.get(choosedIdx).getFirstZoomLvl());
                resModelChooseMaps.setTileSizePixel(modelChooseMapsArrayList.get(choosedIdx).getTileSizePixel());
                resModelChooseMaps.setSdrLat(modelChooseMapsArrayList.get(choosedIdx).getSdrLat());
                resModelChooseMaps.setSdrLon(modelChooseMapsArrayList.get(choosedIdx).getSdrLon());

                int result = dbManager.updateSettingSmartwatch(resModelChooseMaps);

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
