package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.sql.Connection;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private  List<Transaction> transactions;
    private DBHelper db;
    public PersistentTransactionDAO(Context context){
        transactions = new LinkedList<>();
        db = new DBHelper(context);
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String dateS = date.toString();
        String expenseTypeS = expenseType.toString();
        db.logTransaction(dateS,accountNo,expenseTypeS,accountNo);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        return db.getAllTransactionLogs();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        return db.getPaginatedTransactionLogs(limit);
    }
}
