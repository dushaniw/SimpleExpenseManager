package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.helper.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.AccountDAOImpl;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.TransactionDAOImpl;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by Uer on 12/5/2015.
 */
public class PersistentExpenseManager extends ExpenseManager {

    //Databasefields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public PersistentExpenseManager(Context mcontext){
        context=mcontext;
        setup();
    }

    @Override
    public void setup(){
        dbHelper = new DatabaseHelper(context);
        //context.deleteDatabase("130645J");
        open();
        if (database!=null) {
            AccountDAO accountDAO = new AccountDAOImpl(database, dbHelper);
            setAccountsDAO(accountDAO);
            TransactionDAO transactionDAO = new TransactionDAOImpl(database, dbHelper);
            setTransactionsDAO(transactionDAO);

           // Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
           // Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
           // getAccountsDAO().addAccount(dummyAcct1);
           // getAccountsDAO().addAccount(dummyAcct2);

        }

    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

}
