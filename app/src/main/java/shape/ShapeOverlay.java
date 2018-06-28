package shape;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import java.util.ArrayList;

/**
 * Created by user on 25/04/2018.
 */

public class ShapeOverlay extends Overlay {
    protected String layerName = "";
    protected ArrayList<MapShape> Shapes = new ArrayList<MapShape>();
    public ArrayList<MapShape> getShapes(){return Shapes;}

    protected boolean isHitTest = true;
    public void  setHitTest(boolean val){isHitTest=val;}
    public boolean getHitTest(){return isHitTest;}

    protected OnShapeTapListener mOnShapeTapListener = null;

    public void setLayerName(String val)
    {
        layerName = val;
    }

    public String getLayerName()
    {
        return  layerName;
    }

    public ShapeOverlay(String name)
    {
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
            MapShape shp = null;
            Point pt = new Point((int)e.getX(),(int)e.getY());
            for (int i =0;i<getShapes().size();i++)
            {
                if (getShapes().get(i).HitTest(pt,mapView))
                {
                    if (shp == null) shp =getShapes().get(i);
                }
            }
            if (shp!=null && mOnShapeTapListener!=null)
            {
                return mOnShapeTapListener.onClick(this,shp, new PointF(e.getX(),e.getY()));
            }
        }
        return false;
    }
}
