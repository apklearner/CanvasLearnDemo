package bilibili.viewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1,btn2,btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private  void initView(){
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn1:
                startMActivity(0);
                break;
            case R.id.btn2:
                startMActivity(1);
                break;
            case R.id.btn3:
                startMActivity(2);
                break;
        }
    }

    private void startMActivity(int position){

        Intent intent = new Intent(this,SecondActivity.class);
        intent.putExtra("abc",position);
        startActivity(intent);

    }
}
