package jp.dip.gitchaaan.sqlitetest.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Button;

/**
 * Created by Owner on 2014/11/26.
 */
public class WifiActivity extends Activity implements OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        Button btn_gps = (Button)findViewById(R.id.btn_gps);
        btn_gps.setOnClickListener(this);

        Button btn_start2 = (Button)findViewById(R.id.btn_start2);
        btn_start2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_gps:
                Intent intent = new Intent(getApplicationContext(), SQLiteActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_start2:
                startService(new Intent(WifiActivity.this, WifiService.class));
                break;
            case R.id.btn_stop2:
                stopService(new Intent(WifiActivity.this, WifiService.class));
                break;
        }
    }
}
