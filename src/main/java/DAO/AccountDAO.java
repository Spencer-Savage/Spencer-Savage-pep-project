package DAO;

import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;

public class AccountDAO {
    
    public Account insertAccount(Account newAccount){
        Connection db = ConnectionUtil.getConnection();
        
        try {
            String sqlInsertNewAccount = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement preparedStatementToInsertNewAccount = 
                db.prepareStatement(sqlInsertNewAccount, Statement.RETURN_GENERATED_KEYS);

            preparedStatementToInsertNewAccount.setString(1, newAccount.getUsername());
            preparedStatementToInsertNewAccount.setString(2, newAccount.getPassword());

            preparedStatementToInsertNewAccount.executeUpdate();
            ResultSet rSetContainsPrimaryKey = preparedStatementToInsertNewAccount.getGeneratedKeys();
            if (rSetContainsPrimaryKey.next()){
                int systemGenAccountID = rSetContainsPrimaryKey.getInt(1);
                return new Account(systemGenAccountID, newAccount.getUsername(), newAccount.getPassword());
            }

        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }

        return null;
    }
    
    public Account loginAccount(Account lAccount){
        Connection db = ConnectionUtil.getConnection();
        
        try {
            String sqlSelectAccountWithUsernamePassword = 
                "SELECT account_id, username, password " + 
                "FROM account " + 
                "WHERE username = ? AND password = ?;";
            PreparedStatement preparedStatementToInsertNewAccount = 
                db.prepareStatement(sqlSelectAccountWithUsernamePassword);

            preparedStatementToInsertNewAccount.setString(1, lAccount.getUsername());
            preparedStatementToInsertNewAccount.setString(2, lAccount.getPassword());

            ResultSet rSet = preparedStatementToInsertNewAccount.executeQuery();
            if (rSet.next()){
                int accountID = rSet.getInt(1);
                return new Account(accountID, lAccount.getUsername(), lAccount.getPassword());
            }
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }

        return null;
    }

    public Account getAccountById(int accountId){
        Connection db = ConnectionUtil.getConnection();
        
        try {
            String sqlSelectAccountWithAccountId = 
                "SELECT account_id, username, password " + 
                "FROM account " + 
                "WHERE account_id = ?";     
            PreparedStatement preparedStatementToSelectAccount = 
                db.prepareStatement(sqlSelectAccountWithAccountId);

            preparedStatementToSelectAccount.setInt(1, accountId);
            
            ResultSet rSet = preparedStatementToSelectAccount.executeQuery();
            if (rSet.next()){
                int accountID = rSet.getInt(1);
                return new Account(accountID, rSet.getString(2), rSet.getString(3));
            }
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }

        return null;
    }
}

