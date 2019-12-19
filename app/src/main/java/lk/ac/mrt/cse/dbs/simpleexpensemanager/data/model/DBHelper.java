package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    //database name
    private static final String DB_NAME = "170138U";

    //table names
    private final String TABLE_ACCOUNT = "account";
    private final String TABLE_TRANSACTION = "transaction";
    //column names
    private final String COLUMN_ACCNO = "account_number";
    private final String COLUMN_BANKNAME = "account";
    private final String COLUMN_ACC_HOLDER_NAME = "account_holder_name";
    private final String COLUMN_BALANCE = "balance";

    private final String COLUMN_DATE = "date";
    private final String COLUMN_EXPENSE_TYPE = "expence_type";
    private final String COLUMN_AMOUNT = "amount";


    //initialize the database

    public DBHelper(Context context) {

        super(context, DB_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + TABLE_ACCOUNT +"("+ COLUMN_ACCNO +" varchar primary key,"+ COLUMN_BANKNAME+" varchar, "+COLUMN_ACC_HOLDER_NAME+" varchar, "+COLUMN_BALANCE+" float)";
        final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTION +"("+ COLUMN_ACCNO +" varchar ,"+ COLUMN_DATE+" date, "+COLUMN_EXPENSE_TYPE+" varchar, "+COLUMN_AMOUNT+" float)";
        sqLiteDatabase.execSQL(CREATE_TABLE_ACCOUNT);
        sqLiteDatabase.execSQL(CREATE_TABLE_TRANSACTION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    // transaction logs
    public void logTransaction(String date, String accNo, String expenceType, String amount){
        ContentValues values = new ContentValues();
         values.put(COLUMN_DATE,date);
        values.put(COLUMN_ACCNO,accNo);
        values.put(COLUMN_EXPENSE_TYPE,expenceType);
        values.put(COLUMN_AMOUNT,amount);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TRANSACTION, null,values);
        db.close();
    }

    //get all transaction logs
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        List<Transaction> transactions = new LinkedList<>();
        String query = "SELECT*FROM " + TABLE_TRANSACTION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String accNo = cursor.getString(0);
            String date = cursor.getString(1);
            String exType = cursor.getString(2);
            double amount = cursor.getDouble(3);
            ExpenseType expenseType = ExpenseType.valueOf(exType);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date date1 =formatter.parse(date);
            calendar.setTime(date1);
            Date date2 = calendar.getTime();

            Transaction transaction = new Transaction(date2,accNo,expenseType,amount);
            transactions.add(transaction);
        }
        return transactions;
     }

     //get limited transaction logs
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        List<Transaction> transactions = new LinkedList<>();
        String query = "SELECT*FROM " + TABLE_TRANSACTION + " LIMIT " + String.valueOf(limit) ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String accNo = cursor.getString(0);
            String date = cursor.getString(1);
            String exType = cursor.getString(2);
            double amount = cursor.getDouble(3);
            ExpenseType expenseType = ExpenseType.valueOf(exType);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date date1 =formatter.parse(date);
            calendar.setTime(date1);
            Date date2 = calendar.getTime();

            Transaction transaction = new Transaction(date2,accNo,expenseType,amount);
            transactions.add(transaction);
        }
        return transactions;
    }

    //update the balance
    public void updateDetails(String accountNo, ExpenseType expenseType, double amount) {
        Account account = showAccount(accountNo);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(COLUMN_BALANCE, (Double.toString(Double.valueOf(account.getBalance()) - amount)));
                break;
            case INCOME:
                values.put(COLUMN_BALANCE, (Double.toString(Double.valueOf(account.getBalance()) + amount)));
                break;
        }
        db.update(TABLE_ACCOUNT, values, COLUMN_ACCNO + " = ?", new String[]{accountNo});
    }

    //delete the account
    public void deleteAccount(String accountNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNT, COLUMN_ACCNO + " = ?",
                new String[]{accountNo});
        db.close();
    }

    //show all the account numbers
    public List showAccountNumberList() {
        List accountnumbers = new ArrayList<>();

// Select All Query
        String selectQuery = "SELECT " + COLUMN_ACCNO + " FROM " + TABLE_ACCOUNT + " ORDER BY " +
                COLUMN_ACCNO + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String accnum = cursor.getString(cursor.getColumnIndex(COLUMN_ACCNO));
                accountnumbers.add(accnum);
            } while (cursor.moveToNext());
        }

// close db connection
        db.close();

// return notes list
        return accountnumbers;
    }



    //show all the accounts
    public List showAccountList() {
        List accounts = new ArrayList<>();

// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ACCOUNT + " ORDER BY " +
                COLUMN_ACCNO + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Account acc = new Account(cursor.getString(cursor.getColumnIndex(COLUMN_ACCNO)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_BANKNAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ACC_HOLDER_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_BANKNAME)));

                accounts.add(acc);
            } while (cursor.moveToNext());
        }

// close db connection
        db.close();

// return notes list
        return accounts;
    }


    //show account related to given account no
    public Account showAccount(String accountNo) {
        SQLiteDatabase db = this.getReadableDatabase(); //get readable database as we are not inserting anything

        Cursor cursor = db.query(TABLE_ACCOUNT,
                new String[]{COLUMN_ACCNO, COLUMN_BALANCE, COLUMN_ACC_HOLDER_NAME, COLUMN_BALANCE},
                COLUMN_ACCNO + "=?",
                new String[]{accountNo}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

//prepare account object
        Account account = new Account(
                cursor.getString(cursor.getColumnIndex(COLUMN_ACCNO)),
                cursor.getString(cursor.getColumnIndex(COLUMN_BANKNAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_ACC_HOLDER_NAME)),
                cursor.getDouble(cursor.getColumnIndex(COLUMN_BALANCE)));

        cursor.close();

        return account;
    }

    //insert new account to database
    public void insertAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase(); // get writable database as we want to write data

        ContentValues values = new ContentValues(); //if there are autoincrement values ,auto add them
        values.put(COLUMN_ACCNO, account.getAccountNo());
        values.put(COLUMN_BANKNAME, account.getBankName());
        values.put(COLUMN_ACC_HOLDER_NAME, account.getAccountHolderName());
        values.put(COLUMN_BALANCE, account.getBalance());

        db.insert(TABLE_ACCOUNT, null, values); //insert row

        db.close();
    }

}

