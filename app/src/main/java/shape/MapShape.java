package shape;

import android.graphics.Canvas;
import android.graphics.Point;

import org.osmdroid.views.MapView;


/**
 * Created by user on 25/04/2018.
 */

public abstract class MapShape {
    protected Object tag;
    public void setTag(Object val){tag = val;}
    public Object getTag(){return tag;}

    protected int ID = 0;
    public  void setID(int val){ID = val;}
    public  int getID(){return ID;}

    protected boolean isVisible=true;
    public  void setVisible(boolean val){isVisible=val;}
    public boolean getVisible(){return isVisible;}

    protected ShapeType shapeType = ShapeType.NONE;
    public ShapeType getShapeType(){return shapeType;}

    abstract  public void DrawShape(Canvas c, MapView view);
    abstract public boolean HitTest(Point pt, MapView view);
}
