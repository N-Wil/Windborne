package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService{
    AccountDAO dao;

    public AccountService(){
        dao = new AccountDAO();
    }

    //from user story 1
    //should fail (return null) for blank username, password less than 4 characters,
    //or account already exists
    public Account addAccount(Account a){

        //input validation
        //null method parameter object FAILS
        if(a == null){return null;}

        //username blank (could still potentially be null) FAILS
        else if(a.getUsername().equals("")){return null;}    

        //password less than 4 characters FAILS
        else if(a.getPassword().length() < 4){return null;}

        //account with username already exists FAILS
        else if(dao.getAccountByUsername(a.getUsername()) != null){return null;}

        //else attempt to persist to database PASSES
        else{
            return dao.insertAccount(a);
        }
    }
    

    //from user story 2
    //check if a provided account has matching password for login
    public Account verifyAccount(String username, String password){
        Account a = dao.getAccountByUsername(username);
        //account with provided username IS NOT in database
        if(a == null){
            return null;
        }
        //account with provided username IS in database, but password DOES NOT match
        else if(!(a.getPassword().equals(password))){
            return null;
        }
        //account IS present in database AND password matches
        else{
            return a;
        }
    }

















}

