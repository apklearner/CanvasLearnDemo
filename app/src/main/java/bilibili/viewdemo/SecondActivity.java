package bilibili.viewdemo;

import android.app.Activity;
import android.os.Bundle;

import bilibili.viewdemo.views.ScanView;
import bilibili.viewdemo.views.SwapView;

/**
 * Created by ly on 2017/2/15.
 */

public class SecondActivity extends Activity {

    private int currentPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPosition = getIntent().getIntExtra("abc",-1);
        setLayoutRes(getIntent().getIntExtra("abc",-1));
    }

    private void setLayoutRes(int positon){
        switch (positon){
            case 0:
                setContentView(R.layout.view_1);
                break;
            case 1:
                setContentView(R.layout.view_2);
                break;
            case 2:
                setContentView(R.layout.view_3);
                break;
        }


    }

    @Override
    protected void onDestroy() {

        switch (currentPosition){
            case 1:
                ((SwapView)findViewById(R.id.swap)).shutDowmExecutor();
                break;
            case 2:
                ((ScanView)findViewById(R.id.scanview)).shutDownExcurors();
                break;
        }

        super.onDestroy();
    }
}
