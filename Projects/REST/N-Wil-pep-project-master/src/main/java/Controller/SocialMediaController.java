package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import static org.mockito.ArgumentMatchers.notNull;

// PROBABLY NEED
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import Model.*;
import Service.*;

/**
 * You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private AccountService aServ;
    private MessageService mServ;

    public SocialMediaController(){
        aServ = new AccountService();
        mServ = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
    
        //User story 1 endpoint
        app.post("/register", this::registerUserHandler);

        //User story 2 endpoint
        app.post("/login", this::loginHandler);

        //User story 3 endpoint
        app.post("/messages", this::newMessageHandler);

        //User story 4 endpoint
        app.get("/messages", this::getAllMessagesHandler);

        //User story 5 endpoint
        app.get("/messages/{message_id}", this::getMessageByIdHandler);

        //User story 6 endpoint
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);

        //User story 7 endpoint
        app.patch("/messages/{message_id}", this::updateMessageGivenMessageIdHandler);
        
        //User story 8 endpoint
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromUserAccountIdHandler);




        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    //handler for story 1
    private void registerUserHandler(Context ctx) throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = aServ.addAccount(account);

        if(addedAccount == null){
            ctx.status(400);
        }else{
            ctx.json(addedAccount).status(200);
            //ctx.json(mapper.writeValueAsString(addedAccount)).status(200);
        }
    }

    //handler for story 2
    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = aServ.verifyAccount(account.getUsername(), account.getPassword());

        if(addedAccount == null){
            ctx.status(HttpStatus.UNAUTHORIZED);
        }
        else{
            ctx.json(addedAccount).status(HttpStatus.OK);
        }
    }

    //handler for story 3
    private void newMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = mServ.addMessage(msg);

        if(newMessage == null){
            ctx.status(HttpStatus.BAD_REQUEST);
        }
        else{
            ctx.json(newMessage).status(200);
        }
    }

    //handler for story 4
    private void getAllMessagesHandler(Context ctx){
        ctx.json(mServ.getAllMessages()).status(200);
    }

    //handler for story 5
    private void getMessageByIdHandler(Context ctx){
        //maybe should be if/else on getMessageById() being null
        //instead of try/catch but it works out the same
        //wouldn't catch wrong number format for message_id though
        //maybe ObjectMapper .writeValueAsString() method
        try{
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            ctx.json(mServ.getMessageById(id)).status(200);
        }
        //NullPointerException handles when getMessageById returns null, which cannot be used by .json()
        catch(NumberFormatException | NullPointerException e){
             ctx.json("").status(200);
        }
    }

    //handler for story 6
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException{
        try{
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            Message m = mServ.deleteMessageById(id);
            if(m == null){
                ctx.json("").status(200);
            }
            else{
                ctx.json(m).status(200);
            }
            
        }
        catch(NumberFormatException e){
            ctx.result("").status(200);
        }

    }

    //handler for story 7
    //(i think this is how the all others should be structured too for MVC framework, 
    //but also MVC is sometimes silly)
    private void updateMessageGivenMessageIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message temp = om.readValue(ctx.body(), Message.class);

        Message newMessage = mServ.updateMessageById(ctx.pathParam("message_id"), temp.getMessage_text());

        if(newMessage != null){
            ctx.json(newMessage).status(200);
        }
        else{
            ctx.status(400);
        }
    }

    //handler for story 8
    private void getAllMessagesFromUserAccountIdHandler(Context ctx){
        String id = ctx.pathParam("account_id");
        ctx.json(mServ.getMessagesFromUserId(id)).status(200);
    }

}
