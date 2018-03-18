package id.or.ppfi.sqlite.group;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperGroup extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "M_GROUP";

    // Table columns
    public static final String _ID = "_id";

    public static final String GROUPCODE = "groupcode";
    public static final String GROUPNAME = "groupname";
    public static final String QTYATLET = "qty_atlet";

    // Database Information
    static final String DB_NAME = "AWD_IAM_PRIMA_.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GROUPCODE + " TEXT NOT NULL, " + GROUPNAME + " TEXT " +
            ", " + QTYATLET + " TEXT);";

    public DatabaseHelperGroup(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public List<String> getAllLabels(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return list;
    }
}
