package id.or.ppfi.sqlite.listviewgroup;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManagerListViewGroup {

    private DatabaseHelperListViewGroup dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManagerListViewGroup(Context c) {
        context = c;
    }

    public DBManagerListViewGroup open() throws SQLException {
        dbHelper = new DatabaseHelperListViewGroup(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }



    public void insert(String groupCode, String groupName,String category,String gambar,String totalPutra,String totalPutri) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelperListViewGroup.GROUPCODE, groupCode);
        contentValue.put(DatabaseHelperListViewGroup.GROUPNAME, groupName);
        contentValue.put(DatabaseHelperListViewGroup.CATEGORY, category);
        contentValue.put(DatabaseHelperListViewGroup.GAMBAR, gambar);
        contentValue.put(DatabaseHelperListViewGroup.TOTAL_ATLET_PUTRA, totalPutra);
        contentValue.put(DatabaseHelperListViewGroup.TOTAL_ATLET_PUTRI, totalPutri);
        database.insert(DatabaseHelperListViewGroup.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelperListViewGroup._ID, DatabaseHelperListViewGroup.GROUPCODE, DatabaseHelperListViewGroup.GROUPNAME ,
                DatabaseHelperListViewGroup.CATEGORY , DatabaseHelperListViewGroup.GAMBAR , DatabaseHelperListViewGroup.TOTAL_ATLET_PUTRA ,
                DatabaseHelperListViewGroup.TOTAL_ATLET_PUTRI };
        Cursor cursor = database.query(DatabaseHelperListViewGroup.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public long getProfilesCount() {
        long cnt  = DatabaseUtils.queryNumEntries(database, DatabaseHelperListViewGroup.TABLE_NAME);
        return cnt;
    }



    public int update(long _id, String groupCode, String groupName,String category,String gambar,String totalPutra,String totalPutri) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelperListViewGroup.GROUPCODE, groupCode);
        contentValues.put(DatabaseHelperListViewGroup.GROUPNAME, groupName);
        contentValues.put(DatabaseHelperListViewGroup.CATEGORY, category);
        contentValues.put(DatabaseHelperListViewGroup.GAMBAR, gambar);
        contentValues.put(DatabaseHelperListViewGroup.TOTAL_ATLET_PUTRA, totalPutra);
        contentValues.put(DatabaseHelperListViewGroup.TOTAL_ATLET_PUTRI, totalPutri);
        int i = database.update(DatabaseHelperListViewGroup.TABLE_NAME, contentValues, DatabaseHelperListViewGroup._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelperListViewGroup.TABLE_NAME, DatabaseHelperListViewGroup._ID + "=" + _id, null);
    }

    public void deleteAll() {
        database.delete(DatabaseHelperListViewGroup.TABLE_NAME, DatabaseHelperListViewGroup._ID , null);
    }


}
