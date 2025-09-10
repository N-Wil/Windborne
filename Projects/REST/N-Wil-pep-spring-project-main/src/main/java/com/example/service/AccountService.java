package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    AccountRepository ar;

    @Autowired
    public AccountService(AccountRepository ar){
        this.ar = ar;
    }

    /**
     * @param account a transient account
     * @return the persisted account
     */
    public Account persistAccount(Account a){
        return ar.save(a);
    }
    /**
     * @return all a entities
     */
    public List<Account> getAllAccounts(){
        return ar.findAll();
    }
    /**
     *
     * @param id id of account entity
     * @return a account entity
     */
    public Account getAccountById(int id){
        Optional<Account> optionalAccount = ar.findById(id);
        if(optionalAccount.isPresent()){
            return optionalAccount.get();
        }
        else{
            return null;
        }    
    }
    public Account getAccountByUsername(String uname){
        return ar.findByUsername(uname);
    }
    /**
     */
    public void deleteAccount(int id){
        ar.deleteById(id);
    }
    /**
     * and return the updated account.
     * @return the updated account entity
     */
    public Account updateAccount(int id, Account replacement){
        //findById returns a type Optional<Account>. This helps the developer avoid null pointer
        //exceptions. We can use the method .get() to convert an Optional<Grocery> to Grocery.
        Optional<Account> optionalAccount = ar.findById(id);
        if(optionalAccount.isPresent()){
            Account temp = optionalAccount.get();
            temp.setUsername(replacement.getUsername());
            ar.save(temp);
            return temp;
        }
        else{
            return null;
        }
    }


    //for registration
    public Account register(Account a){
        //check for null input, blank username, too short password
        if(a == null || a.getUsername().equals("") || a.getPassword().length() < 4){
            return null;
        }
        //check for account with username already exists
        else if(ar.findByUsername(a.getUsername()) != null){
            return a;
        }
        //otherwise add to database, in this case the persisted account will have an accountId
        else{
            return this.persistAccount(a);
        }
    }
    
    //for login
    public Account login(Account a){
        //check for null or malformed account
        if(a == null || a.getUsername() == null || a.getUsername().equals("")){
            return null;
        }
        else{
            //getting valid account, then checking for matching password
            //could be done in one line without new object assignment
            //but less readable
            Account temp = ar.findByUsername(a.getUsername());
            if(temp != null && temp.getPassword().equals(a.getPassword())){
                return temp;
            }
            else{
                return null;
            }
        }
    }
}

