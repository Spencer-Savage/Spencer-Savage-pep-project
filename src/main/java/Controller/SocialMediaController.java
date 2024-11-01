package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService uService = new AccountService();
    MessageService mService = new MessageService();

    public SocialMediaController(){
        this.uService = new AccountService();
        this.mService = new MessageService();

    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        //Account Handlers
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postAccountLoginHandler);

        //Message Handlers
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAccountMessagesHandler);

        return app;
    }

    
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper oMapper = new ObjectMapper();
        Account ctxBodyAccount = oMapper.readValue(ctx.body(), Account.class);

        Account registeredAccount = uService.registerAccount(ctxBodyAccount);
        if(registeredAccount != null){
            ctx.status(200).json(oMapper.writeValueAsString(registeredAccount));
        } else{
            ctx.status(400);
        }
    }
    
    private void postAccountLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper oMapper = new ObjectMapper();
        Account ctxBodyAccount = oMapper.readValue(ctx.body(), Account.class);

        Account loggedInAccount = uService.login(ctxBodyAccount);
        if(loggedInAccount != null){
            ctx.status(200).json(oMapper.writeValueAsString(loggedInAccount));
        } else{
            ctx.status(401);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper oMapper = new ObjectMapper();
        Message ctxBodyMessage = oMapper.readValue(ctx.body(), Message.class);

        Message createdMessage = mService.createMessage(ctxBodyMessage);
        if(createdMessage != null){
            ctx.status(200).json(oMapper.writeValueAsString(createdMessage));
        } else{
            ctx.status(400);
        }
    }

    private void getMessagesHandler(Context ctx) throws JsonProcessingException{
        List<Message> retreivedMessages = mService.getAllMessages();
        ctx.status(200).json(retreivedMessages);
    }

    private void getMessageHandler(Context ctx) throws JsonProcessingException{
        int messageId = 0;

        //Try-catch block for Integer.parseInt() call.
        try{
            messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message retreivedMessage = mService.getMessage(messageId);
            if (retreivedMessage != null){
                ObjectMapper oMapper = new ObjectMapper();
                ctx.status(200).json(oMapper.writeValueAsString(retreivedMessage));
            } else{
                ctx.status(200);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            ctx.status(200);
        }

        
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        int messageId = 0;
        try{
            messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message deletedMessage = mService.deleteMessage(messageId);
            if (deletedMessage != null){
                ObjectMapper oMapper = new ObjectMapper();
                ctx.status(200).json(oMapper.writeValueAsString(deletedMessage));
            } else{
                ctx.status(200);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            ctx.status(200);
        }

        
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException{
        int messageId = 0;
        
        try{
            messageId = Integer.parseInt(ctx.pathParam("message_id"));
            
            ObjectMapper oMapper = new ObjectMapper();
            Message ctxBodyMessage = oMapper.readValue(ctx.body(), Message.class);
            ctxBodyMessage.setMessage_id(messageId);;
            Message updatedMessage = mService.updateMessage(ctxBodyMessage);
            
            if(updatedMessage != null){
                ctx.status(200).json(oMapper.writeValueAsString(updatedMessage));
            } else{
                ctx.status(400);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            ctx.status(400);
        }

        
    }

    private void getAccountMessagesHandler(Context ctx) throws JsonProcessingException{
        int account_id = 0;
        try{
            account_id = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> retreivedMessages = mService.getAllAccountMessages(account_id);
            ctx.status(200).json(retreivedMessages);

        //Empty list provided in catch block due to story requirement.
        } catch (Exception e){
            System.out.println(e.getMessage());
            List<Message> errorEmptyList = new ArrayList<>();
            ctx.status(200).json(errorEmptyList);
        }
    }
}