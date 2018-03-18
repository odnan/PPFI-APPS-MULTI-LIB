package id.or.ppfi.sqlite.group;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import id.or.ppfi.entities.M_GroupPrima;

public class DBManagerGroup {

    M_GroupPrima mData;

    private DatabaseHelperGroup dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManagerGroup(Context c) {
        context = c;
    }

    public DBManagerGroup open() throws SQLException {
        dbHelper = new DatabaseHelperGroup(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String groupCode, String groupName, String qtyAtlet) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelperGroup.GROUPCODE, groupCode);
        contentValue.put(DatabaseHelperGroup.GROUPNAME, groupName);
        contentValue.put(DatabaseHelperGroup.QTYATLET, qtyAtlet);
        database.insert(DatabaseHelperGroup.TABLE_NAME, null, contentValue);


    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelperGroup._ID, DatabaseHelperGroup.GROUPCODE, DatabaseHelperGroup.GROUPNAME };
        Cursor cursor = database.query(DatabaseHelperGroup.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String groupCode, String groupName, String filename) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelperGroup.GROUPCODE, groupCode);
        contentValues.put(DatabaseHelperGroup.GROUPNAME, groupName);
        int i = database.update(DatabaseHelperGroup.TABLE_NAME, contentValues, DatabaseHelperGroup._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelperGroup.TABLE_NAME, DatabaseHelperGroup._ID + "=" + _id, null);
    }

    public void deleteAll() {
        database.delete(DatabaseHelperGroup.TABLE_NAME, DatabaseHelperGroup._ID , null);
    }

    public String[] getAllSpinnerContent(){

        String[] columns = new String[] { DatabaseHelperGroup._ID, DatabaseHelperGroup.GROUPCODE , DatabaseHelperGroup.GROUPNAME , DatabaseHelperGroup.QTYATLET };
        Cursor cursor = database.query(DatabaseHelperGroup.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<String> spinnerContent = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                String groupCode = cursor.getString(cursor.getColumnIndexOrThrow("groupcode"));
                String groupName = cursor.getString(cursor.getColumnIndexOrThrow("groupname"));
                String qtyAtlet = cursor.getString(cursor.getColumnIndexOrThrow("qty_atlet"));
                spinnerContent.add(groupCode+groupName+" ("+qtyAtlet+")");
            }while(cursor.moveToNext());
        }
        cursor.close();

        String[] allSpinner = new String[spinnerContent.size()];
        allSpinner = spinnerContent.toArray(allSpinner);

        return allSpinner;
    }



}
