package com.bms.user.bmssmartwatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.List;

import shape.TLabelAlignType;
import shape.TShapeImage;
import shape.TShapeLabel;
import shape.TShapeOverlay;


import static com.bms.user.bmssmartwatch.MainActivity.*;
import static com.bms.user.bmssmartwatch.MainActivity.map;

/**
 * Created by FAJAR-NB on 25/06/2018.
 */

public class ShowPopUpAddObjectMethod {
    public static View promptView;


    public static void showPopUpMenuAction(final Context activity,final MapView mapView){
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(activity);
        LayoutInflater mLayoutInflater = LayoutInflater.from(activity);
        promptView = mLayoutInflater.inflate(R.layout.popup_menu_action, null);
        LinearLayout menulyt = (LinearLayout)promptView.findViewById(R.id.menuLYT);
        Button addNew = (Button) promptView.findViewById(R.id.addNew);
        /*Button addKoreksiBtn = (Button) promptView.findViewById(R.id.menuAddCorrection);
        Button addObjectSituasiBtn = (Button) promptView.findViewById(R.id.menuAddObjectSituation);
        Button addNewBtn = (Button) promptView.findViewById(R.id.menuAddNew);
        Button addCancel = (Button) promptView.findViewById(R.id.menuCancel); */

        final DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        menulyt.getLayoutParams().width = dm.widthPixels/2;
        final AlertDialog d = dialogbuilder.create();
        d.setView(promptView);
        d.setCanceledOnTouchOutside(false);
        d.show();
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(activity, ObjectSituationAddActivity.class);
                //activity.startActivity(i);

                mapController = map.getController();
                mapController.setZoom(12);
                GeoPoint point = new GeoPoint(-6.823108, 107.388532);
                mapController.setCenter(point);

                TShapeLabel dataLabel = new TShapeLabel();
                dataLabel.Text = "sdr";
                dataLabel.AlignType = TLabelAlignType.RightCenter;
                TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};



                tImageObject = new TShapeImage();
                tImageObject.setImage(BitmapFactory.decodeResource(activity.getResources(),R.drawable.drawable_position_blue_small));
                tImageObject.setBackground(BitmapFactory.decodeResource(activity.getResources(),R.drawable.ic_baloontips_blue_normal_right));
                tImageObject.setLocation(point);
                tImageObject.setLabels(arrLabel);
                Log.d("Test", "onClick");

                lyrLocal.Shapes.add(tImageObject);
                map.getOverlays().add(lyrLocal);
                map.invalidate();
                d.dismiss();
            }
        });


        /*
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
        }); */

        WindowManager.LayoutParams lp = d.getWindow().getAttributes();
        lp.dimAmount = 0.0F;
        d.getWindow().setAttributes(lp);
        //d.getWindow().setBackgroundDrawableResource(R.color.transparance);
    }


}
