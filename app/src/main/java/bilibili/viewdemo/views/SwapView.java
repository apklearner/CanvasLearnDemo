package bilibili.viewdemo.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bilibili.viewdemo.R;

/**
 * Created by ly on 2017/2/15.
 */

public class SwapView extends View {
    public SwapView(Context context) {
        this(context,null);
    }

    public SwapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwapView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }



    private String bgText ="谢谢惠顾";
    private Paint mTextPaint;
    private Paint forePaint;
    private Paint pathPaint;
    private Path path;
    private int mWidth,mHeight;
    private Canvas mCanvas;
    private volatile Bitmap desBitmap;
    private Bitmap srcBitmap;
    private Rect mRect = new Rect();
    private Rect textRectF = new Rect();
    private ExecutorService excutorService;
    private volatile boolean isCompleted = false;


    private void initPaint(){

        excutorService  = Executors.newSingleThreadExecutor();
        mRect = new Rect(0,0,mWidth,mHeight);


        desBitmap = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a01);
//        Drawable drawable = new BitmapDrawable(srcBitmap);
//        drawable.setBounds(0,0,mWidth,mHeight);


        mCanvas = new Canvas(desBitmap);

        forePaint = new Paint();

        pathPaint =new Paint();
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        pathPaint.setStrokeWidth(20);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(30);
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setStyle(Paint.Style.FILL);

        path = new Path();



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(srcBitmap,null,mRect,forePaint);
//        drawMText(canvas);
        if(isCompleted) return;
        drawContent();
        canvas.drawBitmap(desBitmap,null,mRect,new Paint());

    }


    private int lastX;
    private int lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                calcultePercent();
                break;
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                path.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                lastX = x;
                lastY = y;
                path.lineTo(x,y);
                invalidate();


                break;
        }

        return true;
    }



    private void drawMText(Canvas canvas){
        canvas.drawColor(Color.GRAY);
        mTextPaint.getTextBounds(bgText,0,bgText.length(),textRectF);
        float textWidth = textRectF.width();
        float textHeight = textRectF.height();
//        Log.i("123",textWidth +" " +textHeight);
        canvas.drawText(bgText,0,bgText.length(), (mWidth - textWidth)/2,(mHeight + textHeight )/2,mTextPaint);

    }

    private void drawContent(){
        mCanvas.save();
//        mCanvas.drawBitmap(srcBitmap,null,mRect,forePaint);
        mCanvas.drawColor(Color.parseColor("#c3c3c3"));
        mCanvas.drawPath(path,pathPaint);
        mCanvas.restore();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth =getMeasuredWidth();
        mHeight =  getMeasuredHeight();
        initPaint();


    }


    private void calcultePercent(){
//        int pixels[] = new int[bitmap.getWidth()*bitmap.getHeight()];

        excutorService.execute(mRunnable);

    }

    private Runnable mRunnable =new Runnable() {

        @Override
        public void run() {
            int mpixels = 0;
            for(int i =0 ;i< desBitmap.getWidth();i++){
                for(int j = 0;j<desBitmap.getHeight() ;j++){
                    if( desBitmap.getPixel(i,j) == 0) mpixels ++;
                }
            }

            Log.i("123", mpixels*1.0f/(desBitmap.getWidth()*desBitmap.getHeight()) +"%");
            if(mpixels*1.0f/(desBitmap.getWidth()*desBitmap.getHeight()) > 0.6){
                isCompleted = true;
                postInvalidate();
            }
        }
    };
    public void shutDowmExecutor(){
        if(excutorService!=null) excutorService.shutdown();

    }

}
