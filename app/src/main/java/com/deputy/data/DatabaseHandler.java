package com.deputy.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deputy.activity.R;
import com.deputy.api.model.Shift;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "DmcDb";

    private Context _ctxt;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this._ctxt = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SHIFTS_TABLE = "CREATE TABLE "
                + this._ctxt.getResources().getString(R.string.db_shifts_table_name) + "("
                + this._ctxt.getResources().getString(R.string.shift_col_id) + " INTEGER PRIMARY KEY,"
                + this._ctxt.getResources().getString(R.string.shift_col_start) + " TEXT NOT NULL,"
                + this._ctxt.getResources().getString(R.string.shift_col_end) + " TEXT DEFAULT '',"
                + this._ctxt.getResources().getString(R.string.shift_col_start_lat) + " TEXT NOT NULL,"
                + this._ctxt.getResources().getString(R.string.shift_col_start_lon) + " TEXT NOT NULL,"
                + this._ctxt.getResources().getString(R.string.shift_col_end_lat) + " TEXT DEFAULT '',"
                + this._ctxt.getResources().getString(R.string.shift_col_end_lon) + " TEXT DEFAULT '',"
                + this._ctxt.getResources().getString(R.string.shift_col_image) + " TEXT DEFAULT '',"
                + this._ctxt.getResources().getString(R.string.shift_col_image_bytes) + " BLOB DEFAULT ''" + ");";
        db.execSQL(CREATE_SHIFTS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + this._ctxt.getResources().getString(R.string.db_shifts_table_name));

        onCreate(db);
    }

    /**
     * addShiftDetails will add the shift details to SQLite Table
     */
    public void addShiftDetails(Shift shiftObj) {

        SQLiteDatabase db = this.getWritableDatabase();

        String insertQuery = "INSERT or REPLACE into " + this._ctxt.getResources().getString(R.string.db_shifts_table_name) +
                "(" + this._ctxt.getResources().getString(R.string.shift_col_id) + ","
                + this._ctxt.getResources().getString(R.string.shift_col_start) + ","
                + this._ctxt.getResources().getString(R.string.shift_col_end) + ","
                + this._ctxt.getResources().getString(R.string.shift_col_start_lat) + ","
                + this._ctxt.getResources().getString(R.string.shift_col_start_lon) + ","
                + this._ctxt.getResources().getString(R.string.shift_col_end_lat) + ","
                + this._ctxt.getResources().getString(R.string.shift_col_end_lon) + ","
                + this._ctxt.getResources().getString(R.string.shift_col_image) + ","
                + this._ctxt.getResources().getString(R.string.shift_col_image_bytes) + ") values("
                + "'" + shiftObj.getId() + "',"
                + "'" + shiftObj.getStart() + "',"
                + "'" + shiftObj.getEnd() + "',"
                + "'" + shiftObj.getStartLatitude() + "',"
                + "'" + shiftObj.getStartLongitude() + "',"
                + "'" + shiftObj.getEndLatitude() + "',"
                + "'" + shiftObj.getEndLongitude() + "',"
                + "'" + shiftObj.getImage() + "',"
                + "'" + shiftObj.getImageInBytes() + "')";

        // Inserting Row
        Cursor cursor = db.rawQuery(insertQuery, null);
        cursor.getCount();

        db.close(); // Closing database connection
    }

    /**
     * getAllShiftDetails will fetch the shift details from SQLite Table
     *
     * @return
     */
    public List<Shift> getAllShiftDetails() {
        List<Shift> shiftDetailsList = new ArrayList<Shift>();

        // Select Query to get the Shift Details
        String selectQuery = "SELECT  * FROM " + this._ctxt.getResources().getString(R.string.db_shifts_table_name);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        while (cursor.moveToNext()) {
            Shift shiftObj = new Shift();
            shiftObj.setId(cursor.getInt(cursor.getColumnIndex(this._ctxt.getResources().getString(R.string.shift_col_id))));

            shiftObj.setStart(cursor.getString(
                    cursor.getColumnIndex(this._ctxt.getResources().getString(R.string.shift_col_start))));
            shiftObj.setEnd(cursor.getString(cursor.getColumnIndex(this._ctxt.getResources().getString(R.string.shift_col_end))));
            shiftObj.setStartLatitude(
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(this._ctxt.getResources().getString(R.string.shift_col_start_lat)))));
            shiftObj.setStartLongitude(
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(this._ctxt.getResources().getString(R.string.shift_col_start_lon)))));
            shiftObj.setEndLatitude(
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(this._ctxt.getResources().getString(R.string.shift_col_end_lat)))));
            shiftObj.setEndLongitude(
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(this._ctxt.getResources().getString(R.string.shift_col_end_lon)))));
            shiftObj.setImage(cursor.getString(cursor.getColumnIndex(this._ctxt.getResources().getString(R.string.shift_col_image))));
            shiftObj.setImageInBytes(cursor.getBlob(cursor.getColumnIndex(this._ctxt.getResources().getString(R.string.shift_col_image_bytes))));
            // Adding Shift details to list
            shiftDetailsList.add(shiftObj);
        }

        db.close();

        return shiftDetailsList;
    }


}
