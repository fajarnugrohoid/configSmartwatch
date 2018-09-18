package shape;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.bms.user.bmssmartwatch.MainActivity;
import com.bms.user.bmssmartwatch.ShowPopUpAddObjectMethod;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;


import java.util.ArrayList;

/**
 * Created by user on 14/05/2018.
 */

public class TShapeOverlay extends Overlay {

    protected String layerName = "";
    public ArrayList<TMapShape> Shapes = new ArrayList<TMapShape>();
    public ArrayList<TMapShape> getShapes(){return Shapes;}

    protected boolean isHitTest = true;
    public void  setHitTest(boolean val){isHitTest=val;}
    public boolean getHitTest(){return isHitTest;}
    public Context ctx;


    protected OnShapeTapListener mOnShapeTapListener = null;


    public void setLayerName(String val)
    {
        layerName = val;
    }

    public String getLayerName()
    {
        return  layerName;
    }

    public TShapeOverlay(Context ctx,String name)
    {
        System.out.println("TShapeOverlay:" + name);
        this.ctx = ctx;
        layerName = name;
    }

    public void setOnShapeTapListener(OnShapeTapListener val){mOnShapeTapListener= val;}

    @Override
    protected void draw(final Canvas c, final MapView osmv, final boolean shadow) {
        if (!isEnabled()) return;
        if (Shapes.size() == 0) return;
        //draw each shape
        for (int i = 0;i<Shapes.size();i++)
        {
            Shapes.get(i).DrawShape(c,osmv);
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {

        if (isHitTest && getShapes().size()>0)
        {
            TMapShape shp = null;
            Point pt = new Point((int)e.getX(),(int)e.getY());
            for (int i =0;i<getShapes().size();i++)
            {
                if (getShapes().get(i).HitTest(pt,mapView))
                {
                    if (shp == null) shp =getShapes().get(i);
                }
            }
            if (shp!=null && mOnShapeTapListener!=null) {
                //"select * from musuh where ID = ID";
                String ID = "1";

                return mOnShapeTapListener.onClick(TShapeOverlay.this, shp, new PointF(e.getX(), e.getY()) , ID);

            }
        }
        return false;
    }

    public boolean onLongPress(MotionEvent e, MapView mapView) {
        Point pt = new Point((int)e.getX(),(int)e.getY());

        Log.d("Test",  pt.toString());
        ShowPopUpAddObjectMethod.showPopUpMenuAction(this.ctx, mapView);
        return true;
    }



}
