package bilibili.viewdemo.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ly on 2017/2/14.
 */

public class CirlceProgressView extends View {
    public CirlceProgressView(Context context) {
        this(context,null);
    }

    public CirlceProgressView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CirlceProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Paint outerPaint ;
    private Paint innerPaint;
    private Paint radialPaint;
    private Paint mainPaint;
    private SweepGradient outerSweepGradient ;
    private SweepGradient innerSwaeepGradient;
    private LinearGradient outLinearGradient;
    private LinearGradient innerLinearGradient;
    private RadialGradient radialGradeient;
    private int outerColors[] = new int[]{Color.BLUE,Color.GREEN};
    private int innerColors[] = new int[]{Color.RED,Color.YELLOW};
    private int radialColors[] = new int[]{Color.RED,Color.WHITE,Color.WHITE};
    private float radialPos[] = new float[]{0.4f,0.9f,1f};
    private int mWidth;
    private int mHeight;
    private Bitmap orgBitmap;
    private Canvas mCanvas;
    private RectF mRect;
    private RectF outRect;
    private RectF innerRectF;
    private int outStrokeWidth = 30;
    private int innerStrokeWidth = 20;


    private void initPaint(){

        mRect = new RectF(0,0,mWidth,mHeight+outStrokeWidth/2);
        outRect = new RectF(outStrokeWidth/2,outStrokeWidth/2,mWidth -outStrokeWidth/2,mWidth - outStrokeWidth/2);
        innerRectF = new RectF(outStrokeWidth/2,outStrokeWidth/2,mWidth -outStrokeWidth/2,mWidth-outStrokeWidth/2);;
        orgBitmap = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(orgBitmap);

        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0,Paint.ANTI_ALIAS_FLAG |Paint.FILTER_BITMAP_FLAG));

        outerSweepGradient = new SweepGradient(mWidth/2,mHeight/2,outerColors,null);
        innerSwaeepGradient = new SweepGradient(mWidth/2,mHeight/2,innerColors,null);
        outLinearGradient = new LinearGradient(outStrokeWidth/2,outStrokeWidth/2,mWidth - outStrokeWidth/2,mHeight,outerColors, null,Shader.TileMode.MIRROR);
        innerLinearGradient = new LinearGradient(outStrokeWidth/2,outStrokeWidth/2,mWidth - outStrokeWidth/2,mHeight,innerColors, null,Shader.TileMode.MIRROR);
        radialGradeient = new RadialGradient(mWidth/2,mHeight,80,radialColors,radialPos, Shader.TileMode.CLAMP);


        outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerPaint.setAntiAlias(true);
        outerPaint.setDither(true);
        outerPaint.setStrokeCap(Paint.Cap.ROUND);
        outerPaint.setStrokeJoin(Paint.Join.ROUND);
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setShader(outLinearGradient);
        outerPaint.setStrokeWidth(outStrokeWidth);
        outerPaint.setFilterBitmap(true);


        innerPaint = new Paint();
        innerPaint.setAntiAlias(true);
        innerPaint.setDither(true);
        innerPaint.setStrokeCap(Paint.Cap.ROUND);
        innerPaint.setStyle(Paint.Style.STROKE);
        innerPaint.setShader(innerLinearGradient);
        innerPaint.setStrokeWidth(innerStrokeWidth);


        radialPaint = new Paint();
        radialPaint.setAntiAlias(true);
        radialPaint.setDither(true);
        radialPaint.setStyle(Paint.Style.FILL);
        radialPaint.setShader(radialGradeient);

        mainPaint = new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setDither(true);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOuterProgress();
        canvas.drawBitmap(orgBitmap,null,mRect,mainPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        initPaint();

    }

    private void drawOuterProgress(){

//        mCanvas.clipRect(0,0,mWidth,mWidth/2+outStrokeWidth);
        mCanvas.save();
        mCanvas.drawArc(outRect,180,180,false,outerPaint);
        mCanvas.drawArc(innerRectF,-150,90,false,innerPaint);
        mCanvas.drawCircle(mWidth/2,mHeight,80,radialPaint);


        mCanvas.restore();
//        mCanvas.clipRect(mRect);

    }

}
