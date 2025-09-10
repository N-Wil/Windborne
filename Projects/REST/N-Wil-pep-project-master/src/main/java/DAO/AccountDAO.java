package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;

import Util.ConnectionUtil;
import Model.Account;

public class AccountDAO{
    private Connection conn; 

    //Add account to table, automatically adds an account id
    //returns an account object that was added if successful, null otherwise
    public Account insertAccount(Account a){
        Account createdAccount = null;
        try{
            conn = ConnectionUtil.getConnection();
            String sql = String.format("INSERT INTO account (username, password) VALUES ('%s', '%s');", 
                                                                            a.getUsername(), a.getPassword());
            Statement s = conn.createStatement();

            s.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            if(rs.next()){
                createdAccount = new Account(rs.getInt(1), a.getUsername(), a.getPassword());
            }

            conn.close();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        return createdAccount;
    }    

    //Remove account from table, return type may need to be changed later
    public void removeAccount(Account a){
        try{
            conn = ConnectionUtil.getConnection();
            String sql = String.format("DELETE FROM account WHERE account_id = %s;", a.getAccount_id());
            Statement s = conn.createStatement();
            s.executeUpdate(sql);

            conn.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    //Get account by account_id
    public Account getAccountById(int id){
        try{
            conn = ConnectionUtil.getConnection();
            String sql = String.format("SELECT * FROM account WHERE account_id = %s;", id);
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);
            if(rs.next()){
               return new Account(rs.getInt(1), rs.getString(2), rs.getString(3));
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        return null;
    }

    //Get account by username, should only return 1 as username is unique
    public Account getAccountByUsername(String uname){
        try{
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, uname);

            ResultSet rs = s.executeQuery();
            if(rs.next()){
                return new Account(rs.getInt(1), rs.getString(2), rs.getString(3));
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }




    //Get all accounts
    public ArrayList<Account> getAllAccounts(){
        ArrayList<Account> accounts = new ArrayList<Account>();
        try{
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account;";
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while(rs.next()){
                accounts.add(new Account(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return accounts;
    }

    







}
