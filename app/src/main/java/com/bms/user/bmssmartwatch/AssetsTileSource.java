package com.bms.user.bmssmartwatch;

/**
 * Created by user on 17/04/2018.
 */

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import org.osmdroid.tileprovider.util.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;

import bitmap.CustomBitmapTileSourceBase;


public class AssetsTileSource extends CustomBitmapTileSourceBase {

    private final AssetManager mAssetManager;

    public AssetsTileSource(final AssetManager assetManager, final String aName,
                            final int aZoomMinLevel, final int aZoomMaxLevel, final int aTileSizePixels,
                            final String aImageFilenameEnding) {
        super(aName, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels, aImageFilenameEnding);
        mAssetManager = assetManager;
    }

    private static final Logger logger = LoggerFactory.getLogger(AssetsTileSource.class);

    @Override
    public Drawable getDrawable(final String aFilePath) {
        InputStream inputStream = null;
        try {
            logger.error(aFilePath);
            //inputStream = mAssetManager.open(aFilePath);
            inputStream = new FileInputStream(aFilePath);
            if (inputStream != null) {
                final Drawable drawable = getDrawable(inputStream);
                return drawable;
            }
        } catch (final Throwable e) {
            // Tile does not exist in assets folder.
            // Ignore silently
        } finally {
            if (inputStream != null) {
                StreamUtils.closeStream(inputStream);
            }
        }

        return null;
    }

}
