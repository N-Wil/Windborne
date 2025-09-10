package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;

import Util.ConnectionUtil;
import Model.Message;

public class MessageDAO{
    private Connection conn; 

    //Add message to table, message_id is auto-generated
    public Message insertMessage(Message m){
        Message createdMessage = null;
        try{
            conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement s = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

            s.setInt(1, m.getPosted_by());
            s.setString(2, m.getMessage_text());
            s.setLong(3, m.getTime_posted_epoch());

            s.executeUpdate();
            ResultSet rs = s.getGeneratedKeys();
            if(rs.next()){
                createdMessage = new Message(rs.getInt(1), m.getPosted_by(), m.getMessage_text(), m.getTime_posted_epoch());
            }

            conn.close();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        return createdMessage;
    }    

    //Remove message from table
    public void removeMessageById(int id){
        try{
            conn = ConnectionUtil.getConnection();
            String sql = String.format("DELETE FROM message WHERE message_id = %s;", id);
            Statement s = conn.createStatement();
            s.executeUpdate(sql);

            conn.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    //check that poster of message exists in accounts table
    //weird method, unsure which class it technically belongs in
    //but works best here withough any more imports
    public boolean userExists(Message m){
        try{
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE account.account_id = ?;";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, m.getPosted_by());


            return s.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    //Get a specific message by message_id
    public Message getMessageById(int id){
        try{
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, id);

            ResultSet rs = s.executeQuery();
            if(rs.next()){
                //used column names here instead of numbers -\0_o/-
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                                                                rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
            
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }




    //Get all messages
    public ArrayList<Message> getAllMessages(){
        ArrayList<Message> messages = new ArrayList<Message>();
        try{
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message;";
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while(rs.next()){
                messages.add(new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4)));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return messages;
    }


    //Get all messages for a specific given user id
    public ArrayList<Message> selectAllMessagesByUserId(int userId){
        ArrayList<Message> msgs = new ArrayList<Message>();

        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, userId);

            ResultSet rs = s.executeQuery();
            while(rs.next()){
                msgs.add(new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4)));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return msgs;
    }


    //Alter message contents
    public Message updateMessageTextById(int id, String newText){
        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, newText);
            s.setInt(2, id);
            
            s.executeUpdate();

            //return the updated message
            return getMessageById(id);

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    







}