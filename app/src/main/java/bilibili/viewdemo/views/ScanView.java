package bilibili.viewdemo.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import bilibili.viewdemo.R;

/**
 * Created by ly on 2017/2/15.
 */

public class ScanView extends View {
    public ScanView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScanView(Context context) {
        this(context,null);
    }

    private Bitmap destBitmap;
    private Bitmap srcBitmap;
    private int mWidth;
    private int mHeight;
    private Paint destPaint;
    private Paint srcPaint;
    private Paint forePaint;
    private Paint outPaint;
    private Paint innerPaint;
    private Paint circlePaint;

    private RectF cornerRect = new
            RectF();
    private Rect imageRect = new Rect();

    private int outStrtoke =10;
    private int innerStroke = 4;
    private int circleRadius = 8;
    private int mStroke;
    private volatile  int percent =0;
    private volatile  int circlePercent = 0;
    private volatile  boolean downMode = true;

    private ScheduledExecutorService executorService;
    private ScheduledExecutorService cicleExecutorService;

    private LinearGradient innerGradient;
    private LinearGradient outerGradient;
    private int innerColors[] = new int[]{Color.WHITE,Color.parseColor("#00ffffff")};
    private int outerColors[] = new int[]{Color.BLUE,Color.GREEN};

    private void initRes(){

        executorService = Executors.newScheduledThreadPool(1);
        cicleExecutorService = Executors.newScheduledThreadPool(1);

        mStroke = Math.max(outStrtoke/2,circleRadius);

        cornerRect = new RectF(mStroke,mStroke,mWidth-mStroke,mHeight - mStroke);
        imageRect = new Rect(circleRadius*2 ,circleRadius*2,mWidth - circleRadius*2,mHeight -circleRadius*2);

        innerGradient = new LinearGradient(mWidth/2 ,mStroke,mStroke+mWidth/4,mStroke,innerColors,null, Shader.TileMode.CLAMP);
        outerGradient = new LinearGradient(0,0,mWidth,mHeight,outerColors,null, Shader.TileMode.MIRROR);

        destBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a01);
        srcBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.a02);

        destPaint = new Paint();
        destPaint.setAntiAlias(true);


        srcPaint = new Paint();
        srcPaint.setAntiAlias(true);

        forePaint = new Paint();
        forePaint.setAntiAlias(true);
        forePaint.setStyle(Paint.Style.FILL);
        forePaint.setColor(Color.BLUE);

        outPaint = new Paint();
        outPaint.setAntiAlias(true);
        outPaint.setStyle(Paint.Style.STROKE);
//        outPaint.setColor(Color.BLUE);
        outPaint.setStrokeWidth(outStrtoke);
        outPaint.setShader(outerGradient);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.RED);

        innerPaint = new Paint();
//        innerPaint.setColor(Color.YELLOW);
        innerPaint.setStrokeCap(Paint.Cap.ROUND);
        innerPaint.setStyle(Paint.Style.STROKE);
        innerPaint.setStrokeWidth(innerStroke);
        innerPaint.setStrokeJoin(Paint.Join.ROUND);
        innerPaint.setShader(innerGradient);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.GRAY);
//        getRoundBitmap(destBitmap);
        canvas.drawCircle(mWidth/2,mHeight/2,mHeight/2 - circleRadius,outPaint);
        canvas.save();

        canvas.rotate(circlePercent,mWidth/2,mHeight/2);
        canvas.drawArc(cornerRect,-90,-60,false,innerPaint);
        canvas.drawCircle(mWidth/2,circleRadius,circleRadius,circlePaint);

        canvas.restore();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);




//
        canvas.drawBitmap( getRoundBitmap(destBitmap),null,imageRect,paint);
//        canvas.drawBitmap( getClipRect(srcBitmap,getClipRectArea(percent)),null,imageRect,paint);
        canvas.drawBitmap(getDrawGradientBitmap(getRoundBitmap(srcBitmap),percent),null,imageRect,paint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        initRes();
        executorService.scheduleAtFixedRate(taskRunnable,1000,50, TimeUnit.MILLISECONDS);
//        cicleExecutorService.scheduleAtFixedRate(taskRunable2,1000,10,TimeUnit.MILLISECONDS);

    }


    /**
     *  画圆图
     * @param bitmap
     * @return
     */
    private Bitmap  getRoundBitmap(Bitmap bitmap){
        Rect rect = new Rect(0,0,imageRect.width(), imageRect.height());
        Bitmap orgBitmap = Bitmap.createBitmap(imageRect.width(),imageRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(orgBitmap);

        Paint paint = new Paint();
        canvas.save();
        canvas.drawCircle(imageRect.width()/2,imageRect.height()/2,imageRect.width()/2,forePaint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,null,rect,paint);
        canvas.restore();

        return orgBitmap;
    }

    private Bitmap getClipRect(Bitmap bitmap,Rect rect){
        Paint paint = new Paint();
        Bitmap dstBitmap = getRoundBitmap(bitmap);
        Canvas canvas = new Canvas(dstBitmap);
        canvas.clipRect(rect);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawRect(rect,paint);
        paint.setXfermode(null);
        return dstBitmap;
    }



    private Bitmap getDrawGradientBitmap(Bitmap passBitmap,int percent){

//        Bitmap bitmap = Bitmap.createBitmap(imageRect.width(),imageRect.height(), Bitmap.Config.ARGB_8888);
        Rect oriRect = new Rect(0,0,imageRect.width(),imageRect.height());
        Rect clipRect = getClipRectArea(percent);
        Canvas canvas = new Canvas(passBitmap);
//        canvas.clipRect(clipRect);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawRect(clipRect,paint);

//      画上半的渐变区域 和 横截线
//       int flag =  canvas.saveLayer(0,0,imageRect.width(),imageRect.height(),paint,Canvas.ALL_SAVE_FLAG);
        canvas.save();
        //重要
        canvas.clipRect(clipRect);
//        int color = paint.getColor();
//        paint.setColor(Color.LTGRAY);
        paint.setXfermode(null);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawCircle(oriRect.width()/2,oriRect.height()/2,oriRect.width()/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        paint.setColor(color);

        int startY = Math.min(75,clipRect.height());

        LinearGradient linearGradient = new LinearGradient(0,clipRect.height() - startY,0,clipRect.height(),Color.parseColor("#00ffffff"),Color.parseColor("#99ffffff"), Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        canvas.drawRect(clipRect,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        paint.setStrokeWidth(8);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setShader(null);
        canvas.drawLine(0,clipRect.height(),clipRect.width(),clipRect.height(),paint);
//        canvas.restoreToCount(flag);

        canvas.restore();

        return passBitmap;
    }

    /**
     *  获取百分比的高度的区域
     * @param percent
     * @return
     */
    private Rect getClipRectArea( int percent){
        int endY = (int) (imageRect.height()*percent/100);
        Rect rect = new Rect(0,0,imageRect.width(),endY);
        return rect;
    }


//    private int

    private Runnable taskRunnable = new Runnable() {
        @Override
        public void run() {
            if(downMode){
                if(percent <100){
                    percent+=1;
                }else {
                    percent-=1;
                    downMode = false;
                }
            }else{
                if(percent >0){
                    percent -=1;
                }else {
                    percent+=1;
                    downMode = true;
                }
            }

            if(circlePercent <360){
                circlePercent +=1;
            }else {
                circlePercent = 0;
            }

            postInvalidate();
        }
    };

    private Runnable taskRunable2 = new Runnable() {
        @Override
        public void run() {
            if(circlePercent <360){
                circlePercent +=1;
            }else {
                circlePercent = 0;
            }
            postInvalidate();
        }
    };


    public void shutDownExcurors(){
        if(executorService!=null){
            executorService.shutdown();
        }

    }

}
