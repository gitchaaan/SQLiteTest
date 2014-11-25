package jp.dip.gitchaaan.sqlitetest.app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


public class SQLiteActivity extends Activity implements OnClickListener {

    static final String CREATE_TABLE = "create table if not exists GPS_LIST ( _id integer primary key autoincrement, latitude real, longitude real, accuracy integer );";
    static final String DROP_TABLE = "drop table GPS_LIST";
    static final int DB_VERSION = 1;
    static final String DB = "SQLiteTest";
    static SQLiteDatabase mydb;

    private EditText lat;
    private EditText lng;
    private EditText acc;

    private SimpleCursorAdapter myadapter;
    private ListView listview;
    /**
     * OnCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getApplicationContext());
        mydb = helper.getWritableDatabase();

        lat = (EditText)findViewById(R.id.lat);
        lng = (EditText)findViewById(R.id.lng);
        acc = (EditText)findViewById(R.id.acc);

        Button btn_insert = (Button)findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(this);
        Button btn_select = (Button)findViewById(R.id.btn_select);
        btn_select.setOnClickListener(this);
        Button btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);

        Cursor cursor = mydb.query("GPS_LIST", new String[] {"_id", "latitude", "longitude", "accuracy"}, null, null, null, null, null, null);
        String[] from = {"_id", "longitude", "latitude", "accuracy"};
        int[] to = {R.id.data_id, R.id.data_lng, R.id.data_lat, R.id.data_acc};
        myadapter = new SimpleCursorAdapter(this, R.layout.db_data, cursor, from, to);
        listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(myadapter);

        startService(new Intent(SQLiteActivity.this, GpsService.class));
    }

    /**
     * OnClickListener
     * @param view
     */
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_insert) {
            double lat_val = Double.parseDouble(lat.getText().toString());
            double lng_val = Double.parseDouble(lng.getText().toString());
            int acc_val = Integer.parseInt(acc.getText().toString());

            ContentValues values = new ContentValues();
            values.put("latitude", lat_val);
            values.put("longitude", lng_val);
            values.put("accuracy", acc_val);
            mydb.insert("GPS_LIST", null, values);

        } else if(view.getId() == R.id.btn_select) {
            Cursor cursor = mydb.query("GPS_LIST", new String[] {"_id", "latitude", "longitude", "accuracy"}, null, null, null, null, null, null);
            startManagingCursor(cursor);
            myadapter.changeCursor(cursor);
        } else if(view.getId() == R.id.btn_delete) {
            mydb.delete("GPS_LIST", null, null);
        }
    }

    /**
     * SQLiteOpenHelper class
     */
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