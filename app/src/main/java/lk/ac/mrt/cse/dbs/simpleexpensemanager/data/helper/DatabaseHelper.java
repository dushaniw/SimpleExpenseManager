package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Uer on 12/5/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "130645J";

    // Table Names
    public  static final String TABLE_ACCOUNT ="account";
    public  static final String TABLE_TRANSACTION ="_transaction";

    //account Column Names
    public static final String COLUMN_ACCOUNTNO = "accountNo";
    public static final String COLUMN_BANKNAME = "bankName";
    public static final String COLUMN_ACCHOLDER = "accountHolderName";
    public static final String COLUMN_BALANCE = "balance";

    //transaction Column Names
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ACCNO = "accountNo";
    public static final String COLUMN_EXPENSETYPE ="expenseType";
    public static final String COLUMN_AMOUNT = "amount";

    //table create statements
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + TABLE_ACCOUNT
            + "(" + COLUMN_ACCOUNTNO + " TEXT PRIMARY KEY," + COLUMN_BANKNAME+ " TEXT NOT NULL,"+ COLUMN_ACCHOLDER + " TEXT NOT NULL,"
            + COLUMN_BALANCE + " REAL" + ")";

    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTION
            + "(" + COLUMN_DATE + " TEXT NOT NULL," + COLUMN_ACCNO+ " TEXT NOT NULL,"+ COLUMN_EXPENSETYPE + " TEXT NOT NULL,"
            + COLUMN_AMOUNT + " REAL" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop other tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);

        //create new tables
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating required tables
        db.execSQL(CREATE_TABLE_ACCOUNT);
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }
}
