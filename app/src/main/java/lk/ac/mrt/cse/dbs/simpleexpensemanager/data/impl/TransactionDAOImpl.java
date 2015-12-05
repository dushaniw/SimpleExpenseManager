package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.helper.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Uer on 12/5/2015.
 */
public class TransactionDAOImpl implements TransactionDAO {

    //Databasefields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { DatabaseHelper.COLUMN_DATE,DatabaseHelper.COLUMN_ACCNO,
            DatabaseHelper.COLUMN_EXPENSETYPE,DatabaseHelper.COLUMN_AMOUNT};


    public TransactionDAOImpl(SQLiteDatabase db,DatabaseHelper dbHelp){
        database=db;
        dbHelper=dbHelp;

    }


    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateString = df.format(date);
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DATE,dateString);
        values.put(DatabaseHelper.COLUMN_ACCNO,accountNo);
        values.put(DatabaseHelper.COLUMN_EXPENSETYPE,expenseType.name());
        values.put(DatabaseHelper.COLUMN_AMOUNT, String.valueOf(amount));
        try {
            database.insert(DatabaseHelper.TABLE_ACCOUNT, null, values);
        }
        catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
        }
    }

    public List<Transaction> getAllTransactionLogs(){
        List<Transaction> transactions = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_TRANSACTION,
                allColumns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                transactions.add(cursorToTransaction(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return transactions;
    }

    private Transaction cursorToTransaction(Cursor cursor) {
        String dateString = cursor.getString(0);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = df.parse(dateString);
        }
        catch (ParseException ex){
            Log.w("DateFormatError",ex.getMessage());
        }
        String accountno = cursor.getString(1);
        String expenseType=cursor.getString(2);
        double amount=cursor.getDouble(3);
        Transaction transaction = new Transaction(date,accountno,ExpenseType.valueOf(expenseType),amount);

        return transaction;
    }


    public List<Transaction> getPaginatedTransactionLogs(int limit){
        List<Transaction> transactions = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_TRANSACTION,
                allColumns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                transactions.add(cursorToTransaction(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);

    }



}
