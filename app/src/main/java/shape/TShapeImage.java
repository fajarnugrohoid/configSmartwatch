package shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.bms.user.bmssmartwatch.MainActivity;
import com.bms.user.bmssmartwatch.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import static shape.TLabelAlignType.LeftCenter;

/**
 * Created by user on 14/05/2018.
 */

public class TShapeImage extends TMapShape {
    protected Bitmap img = null;
    public void setImage(Bitmap val){img = val;}
    public Bitmap getImage(){return  img;}

    protected Bitmap backgroundImg = null;
    public void setBackground(Bitmap valBackground){
        backgroundImg = valBackground;
    }
    public Bitmap getBackgroundImg(){return  backgroundImg;}

    protected GeoPoint loc= new GeoPoint(0.0,0.0);
    public void setLocation(GeoPoint val){loc=val;}
    public GeoPoint getLocation(){return loc;}

    private TShapeType internalType = TShapeType.IMAGE;
    public  TShapeType getShapeType()
    {
        return  internalType;
    }

    private TOriginType originType = TOriginType.Center;
    public  TOriginType getOriginType()
    {
        return  originType;
    }
    public  void setOriginType(TOriginType value)
    {
        originType = value;
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
    private Paint backPaint = new Paint();
    public TShapeImage()
    {
        //draw Images
        //draw labels
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(false);
        mPaint.setDither(false);

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(18);
    }

    private Point posIcon = new Point();
    private Rect txtBounds;
    @Override
    public void DrawShape(Canvas c, MapView view) {
        if (!isVisible) return;
        if (img==null) return;
        //check if onscreen
        //draw icon

        //view.getProjection().toPixels(new GeoPoint(loc.getLatitude(),loc.getLongitude()),posIcon);
        posIcon = view.getProjection().toPixels(new GeoPoint(loc.getLatitude(),loc.getLongitude()), posIcon);
        c.save();
        switch (originType)
        {
            case Start:
            case Center:
            {
                int dx = img.getWidth()/2;
                int dy = img.getHeight()/2;
                c.drawBitmap(img, posIcon.x - dx, posIcon.y - dy, mPaint);
                break;
            }
            case BottomLeft:
            {
                c.drawBitmap(img, posIcon.x, posIcon.y - img.getHeight(), mPaint);
                break;
            }
        }
        //draw label
        if (labels != null)
        {
            if (labels.length > 0)
            {
                txtBounds = new Rect();
                for (int i = 0;i<labels.length;i++)
                {
                    switch (labels[i].AlignType)
                    {
                        case LeftCenter:
                        {
                            textPaint.setTextAlign(Paint.Align.LEFT);
                            textPaint.getTextBounds(labels[i].Text,0,labels[i].Text.length(),txtBounds);
                            textPaint.setColor(Color.WHITE);
                            //tmbhan image didieu jang background
                            if (backgroundImg!=null) {
                                c.drawBitmap(backgroundImg, posIcon.x - 25, (posIcon.y - img.getHeight()) - 25, backPaint);
                            }
                            c.drawText(labels[i].Text,posIcon.x+labels[i].OffsetPoint.x + 35 , posIcon.y+labels[i].OffsetPoint.y-txtBounds.height()-15,textPaint);
                            break;
                        }
                        case RightCenter:
                        {
                            textPaint.setTextAlign(Paint.Align.RIGHT);
                            textPaint.getTextBounds(labels[i].Text,0,labels[i].Text.length(),txtBounds);
                            textPaint.setColor(Color.WHITE);
                            if (backgroundImg!=null) {
                                c.drawBitmap(backgroundImg, posIcon.x-backgroundImg.getWidth() + 25,posIcon.y-backgroundImg.getHeight() + 45, backPaint);
                            }
                            c.drawText(labels[i].Text,posIcon.x+labels[i].OffsetPoint.x-txtBounds.width(),posIcon.y+labels[i].OffsetPoint.y-txtBounds.height()-15,textPaint);
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
        c.restore();
    }

    @Override
    public boolean HitTest(Point pt, MapView view) {
        Log.i("Test Hit", String.valueOf(pt.x)+":"+ String.valueOf(pt.y));
        if (img==null) return false;
        Log.i("Test Hit", String.valueOf(pt.x)+":"+ String.valueOf(pt.y)+" Not NULL");
        boolean res = false;
        Point posIcon = new Point();
        view.getProjection().toPixels(new GeoPoint(loc.getLatitude(), loc.getLongitude()), posIcon);
        int dx = img.getWidth()/2;
        int dy = img.getHeight()/2;
        Rect r = new Rect(posIcon.x-dx,posIcon.y-dy,posIcon.x+dx,posIcon.y+dy);

        return r.contains(pt.x,pt.y);
    }
}
