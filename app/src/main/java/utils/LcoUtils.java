package utils;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.location.Location;
import android.util.Log;

public class LcoUtils {
	private static String TAG = "LcoUtils";
	
	class CSystemCoordTable {
        public int[] degree ;
        public int[] minute;
        public double[] Latitude;
        public double[] valueY;
        public double[] diffY;
        public boolean hasBuilt = false;
        public CSystemCoordTable()
        {
        }
    }
	
	static class CConstConv {
        static public double _1f = 299.15281;
        static public double k0 = 0.9997;
        static public int FE = 550000;
        static public int FN = 400000;
        static public double a = 6377397.155;
        static public double b;
        static public double latOrgDeg = -8, latOrg;
        static public double lonOrgDeg = 110, lonOrg;
        static public double sinLatOrg;
        static public double e2, e12;  //e2=e kwadrat; e12=akar(e);
        static public double R0, N0;
        static public double r0;
        static public double A, B, C, D;
        static public double A1, B1, C1, D1, E1;
        static public double s0;
        static boolean hasCalculated = false;
        static public void calcConst()
        {
            if (hasCalculated) return;
            //calculate constant
            double n, n2, n3, n4, n5;
            latOrg = (latOrgDeg / 180) * Math.PI;//-0.13962634	
            lonOrg = (lonOrgDeg / 180) * Math.PI;//1.919862177
            sinLatOrg = Math.sin(latOrg);
            b = a * (1 - 1 / _1f);//b=6356078.963	
            n = (a - b) / (a + b); //n=0.001674185
            e2 = 2 / _1f - Math.pow((1 / _1f), 2);  //0.006674372
            R0 = (a * (1 - e2)) / Math.pow((1 - e2 * Math.pow((Math.sin(latOrg)), 2)), 1.5); //6336060.652
            N0 = (a) / Math.pow((1 - e2 * Math.pow((Math.sin(latOrg)), 2)), 0.5); //6377809.42
            r0 = k0 * N0 / Math.tan(Math.abs(latOrg)); //45366857.9
            double tanP = Math.tan(Math.abs(latOrg));
            A = 1 / (6 * R0 * N0);
            B = (tanP / (24 * R0 * Math.pow(N0, 2))) * (1 - (4 * e2 * Math.pow(Math.cos(latOrg), 2)) / (1 - e2));
            C = (5 + 3 * Math.pow(tanP, 2)) / (120 * R0 * Math.pow(N0, 3));       //2.56491E-29	3.9542E-37
            D = tanP * (7 + 4 * Math.pow(tanP, 2)) / (240 * R0 * Math.pow(N0, 4));//	3.9542E-37
            n2 = Math.pow(n, 2);
            n3 = Math.pow(n, 3);
            n4 = Math.pow(n, 4);
            n5 = Math.pow(n, 5);
            A1 = a * (1.0 - n + ((5.0 / 4.0) * (n2 - n3)) + ((81.0 / 64.0) * (n4 - n5)));       //6366742.52  
            B1 = (3.0 * a / 2.0) * (n - n2 + (7.0 / 8.0) * (n3 - n4) + (55.0 / 64.0) * n5); //15988.63868	
            C1 = (15.0 / 16.0) * a * (n2 - n3 + (3.0 / 4.0) * (n4 - n5));     //16.72995422
            D1 = (35.0 / 48.0) * a * (n3 - n4 + (11.0 / 16.0) * n5);        //	0.021784802
            E1 = (315.0 / 512.0) * a * (n4 - n5);     //	3.0773E-05	
            double sin2P = Math.sin(2 * latOrg);
            double sin4P = Math.sin(4 * latOrg);
            double sin6P = Math.sin(6 * latOrg);
            double sin8P = Math.sin(8 * latOrg);
            s0 = A1 * latOrg - B1 * sin2P + C1 * sin4P - D1 * sin6P + E1 * sin8P;       //-884566.7725
            e12 = 1;//........test
            
            hasCalculated = true;
        }
    }
	
	static public boolean IsValid = false;
//    static public Extent thisExtent = new Extent();
	static LcoUtils lu = new LcoUtils();
    static CSystemCoordTable tableY = lu.new CSystemCoordTable();
    /*
    */
    static public List<Integer> lstLCOBlockX = new ArrayList<Integer>();
    static public List<Integer> lstLCOBlockY = new ArrayList<Integer>();
    static public List<Double> lstGeoX = new ArrayList<Double>();
    static public List<Double> lstGeoY = new ArrayList<Double>();
    static public Point ptLocalOrg = new Point();
    static public Location coordLocalOrg = new Location("lco");
    static public Location positionLocalOrg;

    static public void initialize()
    {
        CConstConv.calcConst();
        generateTable();
    }

    static public void generateTable()
    {
        if (tableY.hasBuilt) return;
        CConstConv.calcConst();
        int latStart = 5;
        int latEnd = 12;
        double latIncr  =  Math.PI / (180*60);
        double secRad = Math.PI / (180 * 60*60);
        double dLat;
        double lastVal = 0;
        List<Integer> degree = new ArrayList<Integer>();
        List<Integer> minute = new ArrayList<Integer>();
        List<Integer> Latitude = new ArrayList<Integer>();
        List<Integer>valueY=new ArrayList<Integer>();
        List<Integer> diffY = new ArrayList<Integer>();

        for (int nlat = latStart; nlat <= latEnd; nlat++)
        {
            dLat = (double)nlat;
            dLat = dLat * Math.PI / 180;
            for (int min = 0; min < 60; min++)
            {
                // valY=fn(-dLat); dY=value-lastVal; lastVal=valY;
                double valY = 0;
                double dY = 0;
                //valY=fn(-dLat);
                double lat = -dLat;
                double sin2P = Math.sin(2 * lat);
                double sin4P = Math.sin(4 * lat);
                double sin6P = Math.sin(6 * lat);
                double sin8P = Math.sin(8 * lat);
                double m = CConstConv.A1 * lat - CConstConv.B1 * sin2P + CConstConv.C1 * sin4P - CConstConv.D1 * sin6P + CConstConv.E1 * sin8P;
                m = m - CConstConv.s0;
                double m3 = Math.pow(m, 3);
                double m4 = Math.pow(m, 4);
                double m5 = Math.pow(m, 5);
                double m6 = Math.pow(m, 6);
                double M = CConstConv.k0 * (m + CConstConv.A * m3 + CConstConv.B * m4 + CConstConv.C * m5 + CConstConv.D * m6);
                valY = CConstConv.FN + M;

                dY = (valY - lastVal) / latIncr;
                lastVal = valY;
                //save(dLat,value,lastVal)
                degree.add(nlat);
                minute.add(min);
                Latitude.add((int) -dLat);
                valueY.add((int) valY);
                diffY.add((int) dY);

                dLat += latIncr;
            }
        }
        tableY.degree = toIntArray(degree);
        tableY.minute = toIntArray(minute);
        tableY.Latitude = toDoubleArray(Latitude);
        tableY.valueY = toDoubleArray(valueY);
        tableY.diffY = toDoubleArray(diffY);
        tableY.hasBuilt = true;
    }
    
    private static int[] toIntArray(List<Integer> list)  {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list)  
            ret[i++] = e.intValue();
        return ret;
    }
    
    private static double[] toDoubleArray(List<Integer> latitude) {
    	double[] ret = new double[latitude.size()];
        int i = 0;
        for (Integer e : latitude)  
            ret[i++] = e.intValue();
        return ret;
    }
    
    static public double findLatFromValY(double val)
    {
        int Sa = 0;
        int Sb = tableY.valueY.length - 1;
        if (val >= tableY.valueY[0]) return tableY.Latitude[0];
        if (val <= tableY.valueY[Sb]) return tableY.Latitude[Sb];
        double valRes=0;
        while (true)
        {
            int Sc = (Sa +Sb)/2;
            if (val == tableY.valueY[Sc]) return tableY.Latitude[Sc];
            if (Sa == Sc)
            {
                double dy = val - tableY.valueY[Sa];
                dy = dy / tableY.diffY[Sa + 1];
                valRes = tableY.Latitude[Sa] + dy;
                break;
            }
            if (val > tableY.valueY[Sc])
            {
                Sb = Sc;
            }
            else
            {
                Sa = Sc;
            }
        }
        return valRes;
    }
    
    public static Point geoToLco(Location loc) {
		CConstConv.calcConst();
        double lat = (loc.getLatitude() / 180.0) * Math.PI;
        double lon = (loc.getLongitude() / 180.0) * Math.PI;
        double deltaLon = lon - CConstConv.lonOrg;
        double deltaLat = lat - CConstConv.latOrg;
        double theta = deltaLon * Math.sin(CConstConv.latOrg);
        double sin2P = Math.sin(2 * lat);
        double sin4P = Math.sin(4 * lat);
        double sin6P = Math.sin(6 * lat);
        double sin8P = Math.sin(8 * lat);
        double m = CConstConv.A1 * lat - CConstConv.B1 * sin2P + CConstConv.C1 * sin4P - CConstConv.D1 * sin6P + CConstConv.E1 * sin8P;
        m = m - CConstConv.s0;
        double m3=Math.pow(m,3);
        double m4=Math.pow(m,4);
        double m5=Math.pow(m,5);
        double m6=Math.pow(m,6);
        double M = CConstConv.k0 * (m + CConstConv.A * m3 + CConstConv.B * m4 + CConstConv.C * m5 + CConstConv.D*m6);
        //r=r0+M
        double r = CConstConv.r0 + M;
        double sinTheta = Math.sin(theta);
        //x'=r*sin(theta)
        double x1 = r * sinTheta;
        x1 = Math.abs(x1);
        if (deltaLon < 0) x1 = -x1;
        double X = x1 + CConstConv.FE;
        //y2=x'*tan(1/2Theta)
        double y2 = x1 * Math.tan(0.5*theta);
        double Y = y2 + M + CConstConv.FN;
        Point pt = new Point((int)X, (int)Y);
        return pt;
	}
    
    static public Location lcoToGeo(Point pt) {
        double x1 = pt.x - CConstConv.FE;
        double theta = -Math.atan2(x1, (CConstConv.r0 - CConstConv.FN + pt.y));
        double deltaLmd = theta / CConstConv.sinLatOrg;
        double lmd = CConstConv.lonOrg + deltaLmd;
        double lonDeg = lmd * 180 / Math.PI;

        double y2 = x1 * Math.tan(0.5 * theta);
        double y1 = pt.y + y2;
        double lat = 0;
        lat=findLatFromValY(y1);
        double latDeg = lat * 180 / Math.PI;
        latDeg = latDeg * 1000;
        
        Location loc = new Location("loc");
        Log.d(TAG, "latDeg : " + latDeg);
        Log.d(TAG, "lonDeg : " + lonDeg);
        loc.setLatitude(latDeg);
        loc.setLongitude(lonDeg);
        return loc;
    }
}
