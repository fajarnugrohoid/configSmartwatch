package com.bms.user.bmssmartwatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.osmdroid.util.GeoPoint;

import java.util.List;

/**
 * Created by FAJAR-NB on 25/06/2018.
 */

public class ShowPopUpAddObjectMethod {
    public static View promptView;


    public static void showPopUpMenuAction(Context activity, final GeoPoint p){
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(activity);
        LayoutInflater mLayoutInflater = LayoutInflater.from(activity);
        promptView = mLayoutInflater.inflate(R.layout.popup_menu_action, null);
        LinearLayout menulyt = (LinearLayout)promptView.findViewById(R.id.menuLYT);
        Button addObjectSituasiBtn = (Button) promptView.findViewById(R.id.menuAddObjectSituation);
        Button addKoreksiBtn = (Button) promptView.findViewById(R.id.menuAddCorrection);
        Button addNewBtn = (Button) promptView.findViewById(R.id.menuAddNew);
        Button addCancel = (Button) promptView.findViewById(R.id.menuCancel);

        final DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        menulyt.getLayoutParams().width = dm.widthPixels/2;
        final AlertDialog d = dialogbuilder.create();
        d.setView(promptView);
        d.setCanceledOnTouchOutside(false);
        d.show();
        addObjectSituasiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(activity, ObjectSituationAddActivity.class);
                //activity.startActivity(i);
                d.dismiss();
            }
        });


        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showHitungModePureAuto();
                d.dismiss();
            }
        });

        addCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        WindowManager.LayoutParams lp = d.getWindow().getAttributes();
        lp.dimAmount = 0.0F;
        d.getWindow().setAttributes(lp);
        //d.getWindow().setBackgroundDrawableResource(R.color.transparance);
    }


}
