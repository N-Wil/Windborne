package com.example.service;

import com.example.entity.Message;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    MessageRepository mr;
    AccountRepository ar;

    @Autowired
    public MessageService(MessageRepository mr, AccountRepository ar){
        this.mr = mr;
        this.ar = ar;
    }

    //save message
    public Message persistMessage(Message m){
        return mr.save(m);
    }

    //return all messages
    public List<Message> getAllMessages(){
        return mr.findAll();
    }

    //return all messages for a specific user (postedBy references accountId in account table)
    public List<Message> getAllMessagesByUser(int postedBy){
        return mr.findAllByPostedBy(postedBy);
    }


    //get one message by its messageId
    public Message getMessageById(int id){
        Optional<Message> optMessage = mr.findById(id);
        if(optMessage.isPresent()){
            return optMessage.get();
        }
        else{
            return null;
        }
    }

    //get one message by its postedBy, messageText, timePostedEpoch
    //public Message getMessageby  (){}

    //delete message, maybe unnecessary
    public Message deleteMessage(int id){
        Message temp = getMessageById(id);
        if(temp == null){
            return null;
        }
        else{
            mr.deleteById(id);
            return temp;
        }
    }

    //update message text, returns changed message or null if non-existant
    //checks for blank message text and length over 255 characters (again, directions inconsistent)
    //doesnt necessarily need to return a whole Message object for this particular use case
    //but I think it's more universal this way
    public Message updateMessage(int id, Message newMessage){
        //logic check newMessage, from above (short circuits everything after)
        if(newMessage.getMessageText().length() > 255 || newMessage.getMessageText().isEmpty()){
            return null;
        }
        //otherwise
        Optional<Message> optMessage = mr.findById(id);
        if(optMessage.isPresent()){
            Message temp = optMessage.get();
            temp.setMessageText(newMessage.getMessageText());
            mr.save(temp);
            return temp;

        }
        else{
            return null;
        }
    }

    //to post message
    public Message createMessage(Message m){
        //check input not null, message text not null, blank, or over 254 chars (directions unclear, say "under 255" but test case has 256 chars)
        if(m == null){return null;}
        else if(m.getMessageText() == null || m.getMessageText().isEmpty() || m.getMessageText().length() >= 255){
            return null;
        }
        //check for valid postedBy
        else{
            Optional<Account> poster = ar.findById(m.getPostedBy());
            if(poster.isEmpty()){
                return null;
            }       
            //all good to post now
            else{
                return persistMessage(m);
            }
        }
    
    }




}
