package shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.bms.user.bmssmartwatch.TLineSegment2;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

/**
 * Created by FAJAR-NB on 03/07/2018.
 */

public class TShapePolyline extends TMapShape {
    protected ArrayList<GeoPoint> locations = new ArrayList<GeoPoint>();
    public ArrayList<GeoPoint> getLocations(){return locations;}
    public void setLocations(ArrayList<GeoPoint> locations){
        this.locations = locations;
    }

    protected Paint linePaint = new Paint();
    public void setColor(int val){linePaint.setColor(val);}
    public int getColor(){return linePaint.getColor();}

    public void setLineWidth(float val){linePaint.setStrokeWidth(val);}
    public float getLineWidth(){return linePaint.getStrokeWidth();}

    protected boolean isDashed = false;
    public void setDashed(boolean val){isDashed = val;}
    public boolean getDashed(){return isDashed;}

    protected boolean isRute = true;
    public void setIsRute(boolean val){isRute = val;}
    public boolean getIsRute(){return isRute;}

    float headWidth =25;
    public  float getHeadWidth(){return  headWidth;}
    public  void setHeadWidth(float val){headWidth = val;}

    float headHeight =30;
    public  float getHeadHeight(){return  headHeight;}
    public  void setHeadHeight(float val){headHeight = val;}

    float _dxArrowHead = 8.0f;

    private TShapeType internalType = TShapeType.POLYLINE;
    public  TShapeType getShapeType()
    {
        return  internalType;
    }

    TShapeLabel labels[] = new TShapeLabel[]{};

    public TShapeLabel[] getLabels() {
        return labels;
    }

    public void setLabels(TShapeLabel[] labels) {
        this.labels = labels;
    }

    private Paint mPaint = new Paint();
    private Paint textPaint = new Paint();
    public TShapePolyline()
    {
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(3);
        linePaint.setStyle(Paint.Style.STROKE);
        this.isVisible = true;
        textPaint.setTextSize(24);
        textPaint.setColor(Color.BLACK);
    }

    Point posIcon = new Point();
    Point posIconDrawPath = new Point();
    Point posIcon2 = new Point();
    PointF posIconF = new PointF();

    ArrayList<Point> screenLocation = new ArrayList<Point>();
    ArrayList<PointF> screenLocationF = new ArrayList<PointF>();

    public void CalcScreenCoord(MapView view)
    {
        screenLocation.clear();
        if (locations.size()>0)
            for (int i = 0; i < locations.size(); i++) {
                screenLocation.add(view.getProjection().toPixels(new GeoPoint(locations.get(i).getLatitude(), locations.get(i).getLongitude()), posIcon));
            }
    }

    BoundingBox bounds = null;
    public void CalcBounds()
    {
        if (locations.size() == 0) bounds = null;
        ArrayList<GeoPoint> lstGeo = new ArrayList<>();
        for (int i = 0; i < locations.size() ; i++) {
            lstGeo.add(new GeoPoint(locations.get(i).getLatitude(),locations.get(i).getLongitude()));
        }
        bounds = BoundingBox.fromGeoPoints(lstGeo);
    }

    public BoundingBox GetWorldBounds()
    {
        return bounds;
    }

    public void GetScreenBounds()
    {

    }

    @Override
    public void DrawShape(Canvas c, MapView view) {
        if (locations.size()>0) {
            for (int i = 0; i < locations.size(); i++) {
                screenLocation.add(view.getProjection().toPixels(new GeoPoint(locations.get(i).getLatitude(), locations.get(i).getLongitude()), posIcon));
                //Log.d("OLEAD BOUNDS", "screenLocation : " + screenLocation.get(i).x + " , " + screenLocation.get(i).y);
            }
        }

        //screenLocationF.clear();
        /*if (locations.size()>0) {
            for (int i = 0; i < locations.size(); i++) {

                //pj.toPixels(locations.get(i), posIcon);
                screenLocationF.add(new PointF(posIcon.x, posIcon.y));

                //posIconF = view.getProjection().toPixels(new GeoPoint(locations.get(i).getLatitude(), locations.get(i).getLongitude()), posIconF);

                //screenLocationF.add(view.getProjection().toPixels(new GeoPoint(locations.get(i).getLatitude(), locations.get(i).getLongitude()), posIconF));
                //screenLocationF.add(new PointF((float)locations.get(i).getLatitude(), (float)locations.get(i).getLongitude()));
                Log.d("OLEAD BOUNDS", "screenLocationF : " + screenLocationF.get(i).x + " , " + screenLocationF.get(i).y);
            }
        }*/


        if (!isVisible) return;
        if (locations.size() < 2) return;
        //load from calcscreencoord

        c.save();
        Path mPath = new Path();
        Path mPathDrawLine = new Path();

        posIcon = view.getProjection().toPixels(new GeoPoint(locations.get(0).getLatitude(), locations.get(0).getLongitude()), posIcon);
        mPath.moveTo(posIcon.x,posIcon.y);
        //mPath2.moveTo(0,0);
        //mPath2.lineTo(100,200);
        //mPath2.lineTo(300,300);


        if (labels != null)
        {
            if (labels.length > 0)
            {
                Rect txtBounds = new Rect();
                for (int i = 0;i<labels.length;i++)
                {
                    switch (labels[i].AlignType)
                    {
                        case LeftCenter:
                        {
                            textPaint.setTextAlign(Paint.Align.LEFT);
                            textPaint.getTextBounds(labels[i].Text,0,labels[i].Text.length(),txtBounds);
                            c.drawText(labels[i].Text,posIcon.x+labels[i].OffsetPoint.x,posIcon.y+labels[i].OffsetPoint.y-txtBounds.height(),textPaint);
                            break;
                        }
                        case RightCenter:
                        {
                            textPaint.setTextAlign(Paint.Align.RIGHT);
                            textPaint.getTextBounds(labels[i].Text,0,labels[i].Text.length(),txtBounds);
                            c.drawText(labels[i].Text,posIcon.x+labels[i].OffsetPoint.x-txtBounds.width(),posIcon.y+labels[i].OffsetPoint.y-txtBounds.height(),textPaint);
                            break;
                        }
                        case TopCenter:
                        {
                            textPaint.setTextAlign(Paint.Align.CENTER);
                            textPaint.getTextBounds(labels[i].Text,0,labels[i].Text.length(),txtBounds);
                            c.drawText(labels[i].Text,posIcon.x+labels[i].OffsetPoint.x-txtBounds.width()/2,posIcon.y+labels[i].OffsetPoint.y,textPaint);
                            break;
                        }
                    }
                }
            }
        }
        //if not rute


        for (int i =1;i<locations.size();i++)
        {
            posIcon = view.getProjection().toPixels(new GeoPoint(locations.get(i).getLatitude(), locations.get(i).getLongitude()), posIcon);
            //Log.i("slopDegree loc:", String.valueOf(locations.get(i).getLatitude()) + "," + locations.get(i).getLongitude());
            ////mPath.lineTo(posIcon.x,posIcon.y);
            //Log.i("slopDegree lineTo:", String.valueOf(posIcon.x) + "," + posIcon.y);


            //Log.i("Tshape Polyline", "tshape Polyline locations:" + locations.size());
            /*if (i == locations.size()-1)
            {
                Log.i("Tshape Polyline", "tshape Polyline isRoute:" + isRute);
                if (isRute)
                {
                    Log.i("Tshape Polyline" , "=========================> Is Route ....");

                    //draw arrow
                    Point prevIcon = view.getProjection().toPixels(new GeoPoint(locations.get(i-1).getLatitude(), locations.get(i-1).getLongitude()), posIcon);
                    PointF d = new PointF(posIcon.x-prevIcon.x, posIcon.y-prevIcon.y);
                    // normalize
                    d.set(d.x / d.length(), d.y / d.length());
                    // stretch to, say, 10 units
                    d.set(10.0f * d.x, 10.0f * d.y);

                    float angle = 0.75f * (float) Math.PI;
                    PointF dLeft = new PointF(+d.x*(long) Math.cos(-angle)-d.y*(long) Math.sin(-angle),+d.x*(long) Math.sin(-angle)+d.y*(long) Math.cos(-angle));
                    PointF dRight = new PointF(+d.x*(long) Math.cos(+angle)-d.y*(long) Math.sin(+angle),+d.x*(long) Math.sin(+angle)+d.y*(long) Math.cos(+angle));
                    float pxleft = +d.x*(float) Math.cos(-angle)-d.y*(float) Math.sin(-angle);
                    float pyleft = +d.x*(float) Math.sin(-angle)+d.y*(float) Math.cos(-angle);
                    float kxright = +d.x*(float) Math.cos(+angle)-d.y*(float) Math.sin(+angle);
                    float kyright = +d.x*(float) Math.sin(+angle)+d.y*(float) Math.cos(+angle);


                    mPath.moveTo(posIcon.x,posIcon.y);


                    //mPath.lineTo(posIcon.x+dLeft.x,posIcon.y+dLeft.y);
                    //Log.i("Tshape Polyline" , "=========================> Is Route .... Left Sign X : "+posIcon.x+pxleft+"sign Y : "+posIcon.y+pyleft);
                    //mPath.moveTo(posIcon.x,posIcon.y);
                    //mPath.lineTo(posIcon.x+dLeft.x,posIcon.y+dLeft.y);
                    //Log.i("Tshape Polyline" , "=========================> Is Route .... Right Sign X : "+posIcon.x+dRight.x+"sign Y : "+posIcon.y+dRight.y);

                    //draw label
                }
            } */
            //Log.i("Tshape Polyline" , "=========================> Point x" + posIcon.x +"Point Y : "+posIcon.y);
            ////c.restore();
        }

        //Log.i("Tshape Polyline" , "=========================> Point x" + posIcon.x +"Point Y : "+posIcon.y);
        linePaint.setPathEffect(null);
        if (isDashed) linePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));

        //Log.i("slopDegree sLF:", screenLocationF.toString());
        //Log.i("slopDegree sL:", screenLocation.toString());

        //for (int i =1;i<screenLocation.size();i++){
            //Log.d("slopDegree", "screenLocation uSL:" + i + ":" + screenLocation.get(i).x + " , " + screenLocation.get(i).y);
        //}

        int n1 = 0;
        int n2 = 0;
        screenLocation.clear();
        ArrayList<Point> scr = new ArrayList<Point>();
        ArrayList<String> x = new ArrayList<String>();
        scr.clear();
        if (locations.size()>0) {
            for (int i = 0; i < locations.size(); i++) {
                screenLocation.add(i, view.getProjection().toPixels(new GeoPoint(locations.get(i).getLatitude(), locations.get(i).getLongitude()), posIcon));
                //Log.i("slopDegree L loc:", String.valueOf(locations.get(i).getLatitude()) + "," + String.valueOf(locations.get(i).getLongitude()));
                //Log.i("slopDegree sL loc:", String.valueOf(screenLocation.get(i).x) + "," + String.valueOf(screenLocation.get(i).y));
                ////scr.add(screenLocation.get(i));
                x.add(String.valueOf(screenLocation.get(i).x) + "," + String.valueOf(screenLocation.get(i).y));
            }

            //Log.i("slopDegree loc:", screenLocation.toString());
            //Log.i("slopDegree sL.get0", x.get(0).toString());
            //Log.i("slopDegree sL.get1", x.get(1).toString());
            //Log.i("slopDegree sL.get2", x.get(2).toString());
            //Log.i("slopDegree sL.get3", x.get(3).toString());

            n1 = screenLocation.size() - 2;
            n2 = screenLocation.size() - 1;
            //Log.i("slopDegree n1:", String.valueOf(n1));
            //Log.i("slopDegree n2:", String.valueOf(n2));

            String[] arrx2 = x.get(n2).split(",",-1);
            String[] arrx1 = x.get(n1).split(",",-1);

            Point p1 = screenLocation.get(n1);
            Point p2 = screenLocation.get(n2);
            p1 = new Point(Integer.parseInt(arrx1[0]), Integer.parseInt(arrx1[1]));
            p2 = new Point(Integer.parseInt(arrx2[0]), Integer.parseInt(arrx2[1]));

            ////p1 = new Point(240 , 184);
            ////p2 = new Point(270 , 198);
            //Log.i("slopDegree PointF p1 x:", String.valueOf(p1.x) + ",y:" + String.valueOf(p1.y));
            //Log.i("slopDegree PointF p2 x:", String.valueOf(p2.x) + ",y:" + String.valueOf(p2.y));

            float slope = TLineSegment2.slopeDegree(p1, p2);
            //
            Point ps = new Point((int)(p2.x - headWidth), (int)(p2.y - headHeight));
            Point pe = new Point((int)(p2.x - headWidth), (int)(p2.y + headHeight));

            //Log.d("MAIN" , "head : "+ ps +" , " +pe );

            mPath.moveTo(ps.x,ps.y);
            mPath.lineTo(p2.x,p2.y);
            mPath.lineTo(pe.x,pe.y);
            Matrix rfm = new Matrix();
            rfm.postRotate(slope,p2.x,p2.y);
            mPath.transform(rfm);
        }

        //dc.drawPath(head,s);
        //c.save();
        c.drawPath(mPath,linePaint);

        posIconDrawPath = view.getProjection().toPixels(new GeoPoint(locations.get(0).getLatitude(), locations.get(0).getLongitude()), posIconDrawPath);
        mPathDrawLine.moveTo(posIconDrawPath.x,posIconDrawPath.y);

        for (int i =1;i<locations.size();i++)
        {
            posIconDrawPath = view.getProjection().toPixels(new GeoPoint(locations.get(i).getLatitude(), locations.get(i).getLongitude()), posIconDrawPath);
            //Log.i("slopDegree loc:", String.valueOf(locations.get(i).getLatitude()) + "," + locations.get(i).getLongitude());
            mPathDrawLine.lineTo(posIconDrawPath.x,posIconDrawPath.y);
        }

        c.drawPath(mPathDrawLine,linePaint);
        c.restore();
    }

    @Override
    public boolean HitTest(Point pt, MapView view) {
        return false;
    }
}
