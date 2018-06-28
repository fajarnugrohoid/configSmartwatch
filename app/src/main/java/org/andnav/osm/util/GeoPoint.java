// Created by plusminus on 21:28:12 - 25.09.2008
package org.andnav.osm.util;

import java.text.DecimalFormat;

import org.andnav.osm.util.constants.GeoConstants;
import org.andnav.osm.views.util.constants.MathConstants;

/**
 *
 * @author Nicolas Gramlich
 *
 */
public class GeoPoint implements MathConstants, GeoConstants{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mLongitudeE6;
	private int mLatitudeE6;

	// ===========================================================
	// Constructors
	// ===========================================================

	public GeoPoint(final int aLatitudeE6, final int aLongitudeE6) {
		this.mLatitudeE6 = aLatitudeE6;
		this.mLongitudeE6 = aLongitudeE6;
	}

	protected static GeoPoint fromDoubleString(final String s, final char spacer) {
		final int spacerPos = s.indexOf(spacer);
		return new GeoPoint((int) (Double.parseDouble(s.substring(0,
				spacerPos - 1)) * 1E6), (int) (Double.parseDouble(s.substring(
				spacerPos + 1, s.length())) * 1E6));
	}

	public static GeoPoint fromDoubleString(final String s){
		//final int commaPos = s.indexOf(',');
		final String[] f = s.split(",");
		return new GeoPoint((int)(Double.parseDouble(f[0])* 1E6),
				(int)(Double.parseDouble(f[1])* 1E6));
		//return new GeoPoint((int)(Double.parseDouble(s.substring(0,commaPos-1))* 1E6),
		//		(int)(Double.parseDouble(s.substring(commaPos+1,s.length()))* 1E6));
	}

	public static GeoPoint from2DoubleString(final String lat, final String lon) {
		try {
			return new GeoPoint((int) (Double.parseDouble(lat) * 1E6),
					(int) (Double.parseDouble(lon) * 1E6));
		} catch (NumberFormatException e) {
			return new GeoPoint(0,0);
		}
	}

	public static GeoPoint from2Double(final Double lat, final Double lon) {
		try {
			return new GeoPoint((int) (lat * 1E6),
					(int) (lon * 1E6));
		} catch (NumberFormatException e) {
			return new GeoPoint(0,0);
		}
	}

	public static GeoPoint fromIntString(final String s){
		final String word[] = s.split(",");
		int lat = 0, lon = 0;
		try {
			lat = Integer.parseInt(word[0]);
		} catch (Exception e) {
		}
		try {
			lon = Integer.parseInt(word[1]);
		} catch (Exception e) {
		}
		return new GeoPoint(lat, lon);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getLongitudeE6() {
		return this.mLongitudeE6;
	}

	public int getLatitudeE6() {
		return this.mLatitudeE6;
	}

	public double getLongitude() {
		return this.mLongitudeE6/1E6;
	}

	public double getLatitude() {
		return this.mLatitudeE6/1E6;
	}

	public void setLongitudeE6(final int aLongitudeE6) {
		this.mLongitudeE6 = aLongitudeE6;
	}

	public void setLatitudeE6(final int aLatitudeE6) {
		this.mLatitudeE6 = aLatitudeE6;
	}

	public void setCoordsE6(final int aLatitudeE6, final int aLongitudeE6) {
		this.mLatitudeE6 = aLatitudeE6;
		this.mLongitudeE6 = aLongitudeE6;
	}

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String toString(){
		return new StringBuilder().append(this.mLatitudeE6).append(",").append(this.mLongitudeE6).toString();
	}

	public String toDoubleString() {
		return new StringBuilder().append(this.mLatitudeE6 / 1E6).append(",").append(this.mLongitudeE6  / 1E6).toString();
	}
	
	public String toDoubleString4Digit() {
		String sLat = new DecimalFormat("#0.0000").format(this.mLatitudeE6 / 1E6);
		String sLon = new DecimalFormat("#0.0000").format(this.mLongitudeE6 / 1E6);
		return new StringBuilder().append(sLat).append(",").append(sLon).toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof GeoPoint))
			return false;
		GeoPoint g = (GeoPoint)obj;
		return g.mLatitudeE6 == this.mLatitudeE6 && g.mLongitudeE6 == this.mLongitudeE6;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @see Source@ http://www.geocities.com/DrChengalva/GPSDistance.html
	 * @param gpA
	 * @param gpB
	 * @return distance in meters
	 */
	public int distanceTo(final GeoPoint other) {

		final double a1 = DEG2RAD * (this.mLatitudeE6 / 1E6);
		final double a2 = DEG2RAD * (this.mLongitudeE6 / 1E6);
		final double b1 = DEG2RAD * (other.mLatitudeE6 / 1E6);
		final double b2 = DEG2RAD * (other.mLongitudeE6 / 1E6);

		final double cosa1 = Math.cos(a1);
		final double cosb1 = Math.cos(b1);

		final double t1 = cosa1*Math.cos(a2)*cosb1*Math.cos(b2);

		final double t2 = cosa1*Math.sin(a2)*cosb1*Math.sin(b2);

		final double t3 = Math.sin(a1)*Math.sin(b1);

		final double tt = Math.acos( t1 + t2 + t3 );

		return (int)(RADIUS_EARTH_METERS*tt);
	}
	
	public GeoPoint geoPointTo(final double mAngle, final double mDistanceMeter) {
		final double a1 = DEG2RAD * (this.mLatitudeE6 / 1E6);
		final double a2 = DEG2RAD * (this.mLongitudeE6 / 1E6);
		final double b1 = 0, b2 = 0;
		
		final double tt = mDistanceMeter/RADIUS_EARTH_METERS;
		final double t4 = Math.cos(tt);
//		final double t3 = 
		return null;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
