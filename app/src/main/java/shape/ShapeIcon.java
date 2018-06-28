package shape;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;

import com.bms.user.bmssmartwatch.AssetsTileSource;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by user on 25/04/2018.
 */

public class ShapeIcon extends MapShape {
    private static final Logger logger = LoggerFactory.getLogger(AssetsTileSource.class);

    protected Bitmap img = null;
    public void setImage(Bitmap val){img = val;}
    public Bitmap getImage(){return  img;}

    protected Location loc= new Location("test");
    public void setLocation(Location val){loc=val;}
    public Location getLocation(){return loc;}

    public String lblLeft="";
    public void setLabelLeft(String val){lblLeft =val;}
    public String getLabelLeft(){return  lblLeft;}

    public String lblRight="";
    public void setLabelRight(String val){lblRight =val;}
    public String getLabelRight(){return  lblRight;}

    public  boolean isLblLeftVisible = true;
    public  void setLabelLeftVisible(boolean val){isLblLeftVisible=val;}
    public  boolean getLabelLeftVisible(){return isLblLeftVisible;}

    public  boolean isLblRightVisible = true;
    public  void setLabelRightVisible(boolean val){isLblRightVisible=val;}
    public  boolean getLabelRightVisible(){return isLblRightVisible;}
    private Paint mPaint = new Paint();

    public ShapeIcon(Bitmap bmp, Location pos)
    {
        img = bmp;
        loc = pos;
        shapeType =ShapeType.FLAG;
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
    }

    @Override
    public void DrawShape(Canvas c, MapView view) {
        if (!isVisible) return;
        if (img==null) return;
        //draw icon
        Point posIcon = new Point();
        view.getProjection().toPixels(new GeoPoint(loc.getLatitude(),loc.getLongitude()),posIcon);
        int dx = img.getWidth()/2;
        int dy = img.getHeight()/2;
        c.save();
        c.drawBitmap(img, posIcon.x - dx, posIcon.y - dy, mPaint);
        logger.error("Location  " + String.valueOf(loc.getLatitude()) + ":" + String.valueOf(loc.getLongitude()));
        logger.error("pos X " + String.valueOf(posIcon.x) + ":" + String.valueOf(dx));
        logger.error("pos Y "+ String.valueOf(posIcon.y)+ ":"+ String.valueOf(dy));
        c.restore();
        //draw label

    }

    @Override
    public boolean HitTest(Point pt, MapView view)
    {
        boolean res = false;
        Point posIcon = new Point();
        view.getProjection().toPixels(new GeoPoint(loc.getLatitude(), loc.getLongitude()), posIcon);

        int dx = img.getWidth()/2;
        int dy = img.getHeight()/2;
        Rect r = new Rect(posIcon.x-dx,posIcon.y-dy,posIcon.x+dx,posIcon.y+dy);
        return r.contains(pt.x,pt.y);
    }
}
