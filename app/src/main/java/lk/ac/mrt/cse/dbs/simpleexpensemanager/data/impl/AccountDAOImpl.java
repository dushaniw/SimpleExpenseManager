package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.helper.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Uer on 12/5/2015.
 */
public class AccountDAOImpl implements AccountDAO {

    //Databasefields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { DatabaseHelper.COLUMN_ACCNO,DatabaseHelper.COLUMN_ACCHOLDER,
            DatabaseHelper.COLUMN_BANKNAME,DatabaseHelper.COLUMN_BALANCE};
    public AccountDAOImpl(SQLiteDatabase db,DatabaseHelper dbHelp){
        database=db;
        dbHelper=dbHelp;
    }


    @Override
    public List<String> getAccountNumbersList(){
        List<String> accountNos = new ArrayList<String>() ;
        Cursor cursor = database.query(DatabaseHelper.TABLE_ACCOUNT,
                new String[]{DatabaseHelper.COLUMN_ACCOUNTNO},null,null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                accountNos.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return accountNos;
    }

    @Override
    public List<Account> getAccountsList(){
        List<Account> accounts = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_ACCOUNT,
                allColumns,null,null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                accounts.add(cursorToAccount(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return accounts;
    }

    private Account cursorToAccount(Cursor cursor) {
        String accountNo = cursor.getString(0);
        String bankName = cursor.getString(1);
        String accountHolderName=cursor.getString(2);
        double balance=cursor.getDouble(3);
        Account account = new Account(accountNo,bankName,accountHolderName,balance);

        return account;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException{
        Cursor cursor = database.query(DatabaseHelper.TABLE_ACCOUNT, allColumns, DatabaseHelper.COLUMN_ACCOUNTNO + "=?",
                new String[]{accountNo}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Account account= cursorToAccount(cursor);
            cursor.close();
            return account;

        }
        cursor.close();
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ACCOUNTNO,account.getAccountNo());
        values.put(DatabaseHelper.COLUMN_BANKNAME,account.getBankName());
        values.put(DatabaseHelper.COLUMN_ACCHOLDER,account.getAccountHolderName());
        values.put(DatabaseHelper.COLUMN_BALANCE, account.getBalance());
        try {
            database.insert(DatabaseHelper.TABLE_ACCOUNT, null, values);
        }
        catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
        }


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException{
        try{
            database.delete(DatabaseHelper.TABLE_ACCOUNT, DatabaseHelper.COLUMN_ACCOUNTNO + "=" + accountNo, null);
        }
        catch (SQLiteConstraintException ex) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }



    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException{
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ACCOUNTNO,accountNo);
        Account account = getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        values.put(DatabaseHelper.COLUMN_BANKNAME,account.getBankName());
        values.put(DatabaseHelper.COLUMN_ACCHOLDER,account.getAccountHolderName());
        values.put(DatabaseHelper.COLUMN_BALANCE,String.valueOf(account.getBalance()));

        database.update(DatabaseHelper.TABLE_ACCOUNT,values,DatabaseHelper.COLUMN_ACCOUNTNO+"=?",new String[] {accountNo});
    }



}
