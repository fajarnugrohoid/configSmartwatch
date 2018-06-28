package shape;

import android.graphics.PointF;

/**
 * Created by user on 25/04/2018.
 */

public interface OnShapeTapListener {
    public boolean onClick(ShapeOverlay layer, MapShape shape, PointF posTap);
    boolean onClick(TShapeOverlay layer, TMapShape shape, PointF posTap, String ID);
}
