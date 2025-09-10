package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.ArrayList;

public class MessageService{
    MessageDAO dao;

    public MessageService(){
        dao = new MessageDAO();
    }

    //from user story 3
    //should fail for message too long, or posted_by does not refer to an actual account
    public Message addMessage(Message m){
        //input validation
        //null input
        if(m == null){return null;}
        //message text blank/null
        else if(m.getMessage_text() == null || m.getMessage_text().equals("")){return null;}
        //message too long
        else if(m.getMessage_text().length() >= 255){return null;}
        //posted_by does not correspond to a member in accounts table
        else if(dao.userExists(m) == false){return null;}
        //otherwise attempt to add message to database
        else{
            return dao.insertMessage(m);
        }
    }

    //from user story 4
    //returns an ArrayList of all messages in the database, no validation done
    public ArrayList<Message> getAllMessages(){
        return dao.getAllMessages();
    }

    //from user story 5
    //returns message object with the given id, or null if it doesnt exist
    public Message getMessageById(int id){
        return dao.getMessageById(id);
    }

    //from user story 6
    //deletes a message with the given id from the database, checks for message existence first
    //perhaps should do input checking of id here instead of above, but it works 
    //for now
    public Message deleteMessageById(int id){
        Message m = dao.getMessageById(id);
        if(m == null){
            return null;
        }
        else{
            dao.removeMessageById(id);
            return m;
        }
    }

    //from story 7
    //checks that string id input can be read as an int
    public Message updateMessageById(String HTML_id, String newText){
        //message length cannot be 0 or not over 255 (directions are inconsistent with user story 3)
        if(newText == null || 
                    newText.length() > 255 || 
                            newText.length() == 0){

            return null;
        }

        try{
            //check that a valid integer was provided from the HTML parameter string
            int id = Integer.parseInt(HTML_id);
            //check that a message already exists with that id
            if(dao.getMessageById(id) == null){
                return null;
            }

            //we now know a message with that id exists, now it can be overwritten
            return dao.updateMessageTextById(id, newText);            
        }
        catch(NumberFormatException e){
            return null;
        }
    }

    //from story 9
    public ArrayList<Message> getMessagesFromUserId(String HTML_id){
        try{
            int id = Integer.parseInt(HTML_id);
            return dao.selectAllMessagesByUserId(id);
        }
        catch(NumberFormatException e){
            return null;
        }
    }







}
