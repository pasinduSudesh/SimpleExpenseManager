package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DBHelper db;

    public PersistentAccountDAO(Context context) {
        this.db = new DBHelper(context);
    }

    @Override
    public List getAccountNumbersList() {
        return db.showAccountNumberList();
    }

    @Override
    public List getAccountsList() {
        return db.showAccountList();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return db.showAccount(accountNo);
    }

    @Override
    public void addAccount(Account account) {
        db.insertAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        db.deleteAccount(accountNo);

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        db.updateDetails(accountNo ,expenseType ,amount);
    }
}
