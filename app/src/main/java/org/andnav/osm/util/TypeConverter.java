// Created by plusminus on 00:47:05 - 02.10.2008
package org.andnav.osm.util;


import android.location.Location;

/**
 * Converts some usual types from one to another.
 * @author Nicolas Gramlich
 *
 */
public class TypeConverter {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public static GeoPoint locationToGeoPoint(final Location aLoc){
		return new GeoPoint((int)(aLoc.getLatitude() * 1E6), (int)(aLoc.getLongitude() * 1E6));
	}

	public static GeoPoint customLocationToGeoPoint(final double lat, final double lon){
		return new GeoPoint((int)(lat * 1E6), (int)(lon * 1E6));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
