package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.*;
import com.example.entity.*;

import java.util.List;


/**
 * You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    private AccountService aServ;
    private MessageService mServ;
    

    @Autowired
    public SocialMediaController(AccountService aServ, MessageService mServ){
        this.aServ = aServ;
        this.mServ = mServ;
    }

    //Mapping for story 1
    @PostMapping("/register")
    public ResponseEntity<Account> registration(@RequestBody Account a){
        Account temp = aServ.register(a);
        //general error
        if(temp == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(a);
        }
        //duplicate account name error
        else if(temp.getAccountId() == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(a);
        }
        //success
        else{
            return ResponseEntity.status(HttpStatus.OK).body(temp);
        }
    }

    //Mapping for story 2
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account a){
        Account temp = aServ.login(a);
        if(temp == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(a);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(temp);
        }
    }


    //Mapping for story 3
    @PostMapping("/messages")
    public ResponseEntity<Message> postNewMessage(@RequestBody Message m){
        Message temp = mServ.createMessage(m);
        if(temp != null){
            return ResponseEntity.status(HttpStatus.OK).body(temp);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
        }
    }

    //Mapping for story 4
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> msgs = mServ.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(msgs);
    }

    //Mapping for story 5
    @GetMapping("/messages/{messageId}") 
    public ResponseEntity<Message> getMessageByMessageId(@PathVariable("messageId") int id){
        Message newMessage = mServ.getMessageById(id);
        if(newMessage == null){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(newMessage);
        }
    }

    //Mapping for story 6
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable("messageId") int id){
        Message deleted = mServ.deleteMessage(id);
        if(deleted == null){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(1);
        }
    }

    //Mapping for story 7
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable("messageId") int id, @RequestBody Message m){
        Message updated = mServ.updateMessage(id, m);
        if(updated == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(1);
        }
    }
    

    //Mapping for story 8
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByUser(@PathVariable("accountId") int id){
        return ResponseEntity.status(HttpStatus.OK).body(mServ.getAllMessagesByUser(id));
    }
}
