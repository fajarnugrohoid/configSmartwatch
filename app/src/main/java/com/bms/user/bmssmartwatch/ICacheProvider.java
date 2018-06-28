package com.bms.user.bmssmartwatch;

/**
 * Created by FAJAR-NB on 08/06/2018.
 */

public interface ICacheProvider {
    public byte[] getTile(final String aURLstring, final int aX, final int aY, final int aZ);
    public void putTile(final String aURLstring, final int aX, final int aY, final int aZ, final byte[] aData) throws RException;
    public void Free();
}
