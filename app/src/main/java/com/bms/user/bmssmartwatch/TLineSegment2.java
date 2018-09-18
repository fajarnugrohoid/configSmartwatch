package com.bms.user.bmssmartwatch;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

/**
 * Created by FAJAR-NB on 03/07/2018.
 */

public class TLineSegment2 {
    public float X1, Y1, X2, Y2;
    public float a, b, c;
    public float length = 0;
    public PointF vector = new PointF();
    public PointF normal = new PointF();
    public PointF vectorNormalize = new PointF();
    public PointF pointA, pointB;

    public TLineSegment2(float x1, float y1, float x2, float y2)
    {
        X1 = x1; Y1 = y1; X2 = x2; Y2 = y2;
        a = b = c = 0;
        pointA = new PointF(x1, y1);
        pointB = new PointF(x2, y2);
        CalcCoef();
    }

    public TLineSegment2(PointF pt1, PointF pt2)
    {
        X1 = pt1.x; Y1 = pt1.y;
        X2 = pt2.x; Y2 = pt2.y;
        a = b = c = 0;
        pointA = new PointF(pt1.x,pt1.y);
        pointB = new PointF(pt2.x, pt2.y);
        CalcCoef();
    }

    protected PointF Normalize(PointF a, PointF b)
    {
        float x = b.x - a.x;
        float y = b.y - a.y;
        float mag = (float) Math.sqrt((x * x) + (y * y));
        return new PointF(x / mag, y / mag);
    }

    void CalcCoef()
    {
        a = Y2 - Y1;
        b = X1 - X2;
        c = X2 * Y1 - X1 * Y2;
        //
        vector.x = X2 - X1; vector.y = Y2 - Y1;
        vectorNormalize = Normalize(pointA, pointB);
        //
        normal = new PointF(vectorNormalize.y, -vectorNormalize.x);
        //
        length = (float) Math.sqrt(a * a + b * b);
    }

    public static float slopeDegree(Point p1, Point p2)
    {
        double deg = Math.atan2((p2.y - p1.y), (p2.x - p1.x));
        /*Log.i("slopDegree p2.y:", String.valueOf(p2.y));
        Log.i("slopDegree p1.y:", String.valueOf(p1.y));
        Log.i("slopDegree p2.x:", String.valueOf(p2.x));
        Log.i("slopDegree p1.x:", String.valueOf(p1.x));
        Log.i("slopDegree deg:", String.valueOf(deg));*/
        deg = (deg * 180) / Math.PI;
        return (float) deg;
    }

    //public static double slope(double x1, double y1, double x2, double y2)
    public static float slope(Point p1, Point p2)
    {
        //double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        //double angle = Math.toDegrees(Math.atan2(p2.x - p1.x, p2.y - p1.y));
        double angle = Math.atan2((p2.y - p1.y), (p2.x - p1.x));
        // Keep angle between 0 and 360
        Log.i("slopDegree angle:", String.valueOf(angle));

        angle = angle + Math.ceil( -angle / 360 ) * 360;
        Log.i("slopDegree angle2:", String.valueOf(angle));
        return (float)angle;
    }

    /*
    public static float slopeDegree(PointF p1, PointF p2)
    {
        double deg = Math.atan2((p2.y - p1.y), (p2.x - p1.x));
        Log.i("slopDegree p2.y:", String.valueOf(p2.y));
        Log.i("slopDegree p1.y:", String.valueOf(p1.y));
        Log.i("slopDegree p2.x:", String.valueOf(p2.x));
        Log.i("slopDegree p1.x:", String.valueOf(p1.x));
        Log.i("slopDegree deg:", String.valueOf(deg));
        deg = deg * 180 / Math.PI;
        Log.i("slopDegree deg2:", String.valueOf(deg));
        return (float) deg;
    } */


    public float slopeDegree()
    {
        double deg = Math.atan2((Y2 - Y1), (X2 - X1));
        deg = deg * 180 / Math.PI;
        return (float)deg;
    }
    public float slope()
    {
        double dx = X2 - X1;
        if (dx <= Double.MIN_VALUE) return Float.NaN;
        return (float)((Y2 - Y1) / dx);
    }
}
