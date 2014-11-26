package jp.dip.gitchaaan.sqlitetest.app;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

/**
 * Created by Owner on 2014/11/26.
 */
public class WifiService extends Service {
    WifiManager wm;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WifiManager)getSystemService(WIFI_SERVICE);
        wm.startScan();

        setReceiver();
    }

    //レシーバーをアンドロイドシステムに登録する処理
    private void setReceiver() {
        ReceiveWifi receiveWifi = new ReceiveWifi();

        // 受信する情報の種類を設定
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        //■別ファイルのpublic クラス
        registerReceiver(receiveWifi,  filter);

        //onPauseで解除
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("INFO", "WifiService is started.");
        return START_STICKY;
    }
}
