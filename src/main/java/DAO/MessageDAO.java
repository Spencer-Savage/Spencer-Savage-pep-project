package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    public Message insertMessage(Message newMessage){
        Connection db = ConnectionUtil.getConnection();
        
        try {
            String sqlInsertNewMessage = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatementToInsertNewMessage = 
                db.prepareStatement(sqlInsertNewMessage, Statement.RETURN_GENERATED_KEYS);
            
            preparedStatementToInsertNewMessage.setInt(1, newMessage.getPosted_by());
            preparedStatementToInsertNewMessage.setString(2, newMessage.getMessage_text());
            preparedStatementToInsertNewMessage.setLong(3, newMessage.getTime_posted_epoch());
            preparedStatementToInsertNewMessage.executeUpdate();

            ResultSet rSetContainsPrimaryKey = preparedStatementToInsertNewMessage.getGeneratedKeys();
            if (rSetContainsPrimaryKey.next()){
                int systemGenAccountID = rSetContainsPrimaryKey.getInt(1);
                return new Message(systemGenAccountID, newMessage.getPosted_by(),
                    newMessage.getMessage_text(), newMessage.getTime_posted_epoch());
            }
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
 
        return null;
    }

    public List<Message> getAllMessages(){
        Connection db = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sqlSelectAllMessages = 
                "SELECT message_id, posted_by, message_text, time_posted_epoch " + 
                "FROM message;";  
            PreparedStatement preparedStatementToSelectAllMessages = 
                db.prepareStatement(sqlSelectAllMessages);

            ResultSet rSet = preparedStatementToSelectAllMessages.executeQuery();
            while (rSet.next()){
                messages.add(new Message(rSet.getInt(1), rSet.getInt(2),
                    rSet.getString(3), rSet.getLong(4)));
            }
            return messages;

        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }

        return messages;
    }

    public List<Message> getAllAccountMessages(int accountID){
        Connection db = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();   
        
        try {
            String sqlSelectAllAccountMessages = 
                "SELECT message_id, posted_by, message_text, time_posted_epoch " + 
                "FROM message " +
                "WHERE posted_by = ?;";
            PreparedStatement preparedStatementToSelectAllAccountMessages = 
                db.prepareStatement(sqlSelectAllAccountMessages);
            
            preparedStatementToSelectAllAccountMessages.setInt(1, accountID);

            ResultSet rSet = preparedStatementToSelectAllAccountMessages.executeQuery();
            while (rSet.next()){
                messages.add(new Message(rSet.getInt(1), rSet.getInt(2),
                    rSet.getString(3), rSet.getLong(4)));
            }
            return messages;
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }

        return messages;
    }

    public Message getMessageById(int messageId){
        Connection db = ConnectionUtil.getConnection();
        
        try {
            String sqlSelectMessageWithMessageId = 
                "SELECT message_id, posted_by, message_text, time_posted_epoch " + 
                "FROM message " + 
                "WHERE message_id = ?;";
            PreparedStatement preparedStatementToSelectMessageWithMessageId = 
                db.prepareStatement(sqlSelectMessageWithMessageId);
            
            preparedStatementToSelectMessageWithMessageId.setInt(1, messageId);
            
            ResultSet rSet = preparedStatementToSelectMessageWithMessageId.executeQuery();
            if (rSet.next()){
                return new Message(rSet.getInt(1), rSet.getInt(2),
                    rSet.getString(3), rSet.getLong(4));
            }
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }

        return null;
    }
    
    public boolean deleteMessage(int messageId){
        Connection db = ConnectionUtil.getConnection();
        
        try {
            String sqlDeleteMessage = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatementDeleteMessage = 
                db.prepareStatement(sqlDeleteMessage, Statement.RETURN_GENERATED_KEYS);
            
            preparedStatementDeleteMessage.setInt(1, messageId);

            int deleted = preparedStatementDeleteMessage.executeUpdate();
            if (deleted > 0){
                return true;
            }
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }

        return false;
    }

    public boolean updateMessageText(Message updateMessage){
        Connection db = ConnectionUtil.getConnection();  
       
        try {
            String sqlUpdateMessageText = 
                "UPDATE message " +
                "SET message_text = ? " +
                "WHERE message_id = ?;";
            PreparedStatement preparedStatementToUpdateMessage = db.prepareStatement(sqlUpdateMessageText);

            preparedStatementToUpdateMessage.setString(1, updateMessage.getMessage_text());
            preparedStatementToUpdateMessage.setInt(2, updateMessage.getMessage_id());

            int rowAffected = preparedStatementToUpdateMessage.executeUpdate();
            if (rowAffected > 0){
                return true;
            }
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
        
        return false;
    }
}
