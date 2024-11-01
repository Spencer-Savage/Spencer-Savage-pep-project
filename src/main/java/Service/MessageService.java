package Service;

import DAO.MessageDAO;

import java.util.List;

import DAO.AccountDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDao;
    AccountDAO accountDao;

    public MessageService(){
        this.messageDao = new MessageDAO();
        this.accountDao = new AccountDAO();
    }

    public Message createMessage(Message newMessage){
        if (!isValidMessage(newMessage, false)){
            return null;
        }
        return messageDao.insertMessage(newMessage);
    }

    public Message getMessage(int messageId){
        return messageDao.getMessageById(messageId);
    }

    public Message deleteMessage(int messageId){
        Message messageToDelete = messageDao.getMessageById(messageId);
        if (messageToDelete == null){
            return null;
        }
       
        boolean messageWasDeleted = messageDao.deleteMessage(messageId);
        if (!messageWasDeleted){
            return null;
        }

        return messageToDelete;
    }

    public Message updateMessage(Message updateMessage){
        if (!isValidMessage(updateMessage, true)){
            return null;
        }

        boolean wasUpdated = messageDao.updateMessageText(updateMessage);
        if (!wasUpdated){
            return null;
        }

        return getMessage(updateMessage.getMessage_id());
    }

    public List<Message> getAllMessages(){
        return messageDao.getAllMessages();
    }

    public List<Message> getAllAccountMessages(int accountId){
        return messageDao.getAllAccountMessages(accountId);
    }

    private boolean isValidMessage(Message messageToValidate, boolean isUpdate){
        if (messageToValidate.getMessage_text().isBlank()){
            return false;
        }
        if(messageToValidate.getMessage_text().length() >= 255){
            return false;
        }
        if(isUpdate == false && accountDao.getAccountById(messageToValidate.getPosted_by()) == null){
            return false;
        }

        return true;
    }
}
