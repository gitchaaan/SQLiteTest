package jp.dip.gitchaaan.sqlitetest.app;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Owner on 2014/11/25.
 */
public class ReceiveLocation extends BroadcastReceiver{

    static final String CREATE_TABLE = "create table if not exists GPS_LIST ( _id integer primary key autoincrement, latitude real, longitude real, accuracy real );";
    static final String DROP_TABLE = "drop table GPS_LIST";
    static final int DB_VERSION = 1;
    static final String DB = "SQLiteTest";
    static SQLiteDatabase mydb;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("INFO", "onReceive");
        if(intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)){
            LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            String message = "Location\n"
                    + "Longitude：" + location.getLongitude()
                    + "\n"
                    + "Latitude：" + location.getLatitude();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(context);
            mydb = helper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("latitude", location.getLatitude());
            values.put("longitude", location.getLongitude());
            values.put("accuracy", location.getAccuracy());
            mydb.insert("GPS_LIST", null, values);
        }
    }

    class MySQLiteOpenHelper extends SQLiteOpenHelper {

        public MySQLiteOpenHelper(Context context) {
            super(context, DB, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }

}
