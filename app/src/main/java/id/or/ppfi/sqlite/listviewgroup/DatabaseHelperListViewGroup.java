package id.or.ppfi.sqlite.listviewgroup;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperListViewGroup extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "MASTER_GROUP_ALL";
    // Table columns
    public static final String _ID = "_id";
    public static final String GROUPCODE = "master_group_id";
    public static final String GROUPNAME = "master_group_name";
    public static final String CATEGORY = "master_group_category";
    public static final String GAMBAR = "master_group_logo";
    public static final String TOTAL_ATLET_PUTRA = "total_atlet_putra";
    public static final String TOTAL_ATLET_PUTRI = "total_atlet_putri";

    // Database Information
    static final String DB_NAME = "IAMPRIMA_MASTER_GROUP.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GROUPCODE + " TEXT NOT NULL, " + GROUPNAME + " TEXT , " + CATEGORY +
            " TEXT , "  + GAMBAR + " TEXT , " + " TEXT , "  + TOTAL_ATLET_PUTRA + " TEXT , " + TOTAL_ATLET_PUTRI + " TEXT);";

    public DatabaseHelperListViewGroup(Context context) {
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


}
