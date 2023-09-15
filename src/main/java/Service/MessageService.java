package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO dao) {
        messageDAO = dao;
    }

    //add a message
    public Message addMessage(Message msg) {
        String message = msg.getMessage_text();
        if (message == "" || message.length() >= 255) {
            return null;
        }

        return messageDAO.insertMessage(msg);
    }

    //get all messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    //get a message by its id
    public Message getMessageByID(int id) {
        return messageDAO.getMessageByID(id);
    }

    //delete a message by its id
    public Message deleteMessageById(int id) {
        if (messageDAO.getMessageByID(id) == null) {
            return null;
           }
        Message message = messageDAO.getMessageByID(id);
        messageDAO.deleteMessage(id);
        return message;
    }

    //update a message by its id
    public Message updateMessageById(String text, int id) {
        if (messageDAO.getMessageByID(id) == null) {
            return null;
        }

        if (text.length() >= 255 || text == "") {
            return null;
        }

        messageDAO.updateMessage(id, text);
        Message message = messageDAO.getMessageByID(id);
        return message;
    }

    //get all messages from specified user
    public List<Message> getAllMessagesByUserId(int id) {
        List<Message> msgs = messageDAO.getMessageByUser(id);
        return msgs;
    }
}
