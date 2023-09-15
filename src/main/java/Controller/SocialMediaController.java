package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

import Service.AccountService;
import Model.Account;
import Service.MessageService;
import Model.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accServ;
    MessageService msgServ;

    public SocialMediaController() {
        this.accServ = new AccountService();
        this.msgServ = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postNewAccount);
        app.post("/login", this::postLogin);
        app.post("/messages", this::postNewMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageID);
        app.delete("/messages/{message_id}", this::deleteMessageID);
        app.patch("/messages/{message_id}", this::patchMessageID);
        app.get("/accounts/{account_id}/messages", this::getUserMessages);
        return app;
    }

    //post a new account to the database
    public void postNewAccount(Context ctx) throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        Account acc = map.readValue(ctx.body(), Account.class);
        Account addAcc = accServ.addAccount(acc);
        //return status of success
        if(addAcc != null) {
            ctx.json(map.writeValueAsString(addAcc));
        } else {
            ctx.status(400);
        }
    }

    //handle user login attempts
    public void postLogin(Context ctx) throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        Account acc = map.readValue(ctx.body(), Account.class);
        Account addAcc = accServ.accountLogin(acc);

        if(addAcc != null) {
            ctx.json(map.writeValueAsString(addAcc));
        }else{
            ctx.status(401);
        }
    }
    
    //create a new message
    public void postNewMessage(Context ctx)throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        Message msg = map.readValue(ctx.body(), Message.class);
        Message addMsg = new Message();
        boolean valid = true;
        if (accServ.getAccountById(msg.getPosted_by()) != null) {
            addMsg = msgServ.addMessage(msg);
        } else {
            valid = false;
        }
        if (valid && addMsg != null) {
            ctx.json(map.writeValueAsString(addMsg));
        }else{
            ctx.status(400);
        }
    }

    //get all messages
    public void getAllMessages(Context ctx) {
        List<Message> messages = msgServ.getAllMessages();
        ctx.json(messages);
    }

    //get a message by its id
    public void getMessageID(Context ctx) {
        Message nMsg = msgServ.getMessageByID(Integer.valueOf(ctx.pathParam("message_id")));
        if (nMsg != null) {
            ctx.json(nMsg);
        } else {
            ctx.status(200);
        }
    }

    //delete a message by its id
    public void deleteMessageID(Context ctx) {
        Message nMsg = msgServ.deleteMessageById(Integer.valueOf(ctx.pathParam("message_id")));
        if (nMsg != null) {
            ctx.json(nMsg);
        } else {
            ctx.status(200);
        }
    }

    
    //update a message by its id
    public void patchMessageID(Context ctx) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        Message msg = map.readValue(ctx.body(), Message.class);
        String text = msg.getMessage_text();
        int id = Integer.valueOf(ctx.pathParam("message_id"));

        Message nMsg = msgServ.updateMessageById(text, id);
        if (nMsg != null) {
            ctx.json(nMsg);
        } else {
            ctx.status(400);
        }
    }
    
    //get all messages from a specified user
    public void getUserMessages(Context ctx) {
        int id = Integer.valueOf(ctx.pathParam("account_id"));
        ctx.json(msgServ.getAllMessagesByUserId(id));
    }
    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}