package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.*;
import io.javalin.Javalin;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO dao){
        accountDAO = dao;
    }

    public Javalin makeNewAccount(Account user) {
        //create javalin to return
        //set default value to 200, assuming all account info is correct
        Javalin app = Javalin.create(); 
        app.get("/register", ctx ->{ 
            ctx.status(200);
        });

        //test to make sure requirements are met
        if (user.getUsername() == "" || user.getPassword().length() < 4) {
            app.get("/register", ctx ->{ 
                ctx.status(400);
            });
        }

        if(accountDAO.getAccountByUsername(user.getUsername()) != null) {
            app.get("/register", ctx ->{ 
                ctx.status(400);
            });
        }

        // make the new user
        accountDAO.insertUser(user);
        return app;
    }

    public Account accountLogin(Account user) {
        if (accountDAO.getAccountByUsername(user.getUsername()) == null) {
            //return status 401
        }

        Account acc = accountDAO.getAccountByUsername(user.getUsername());

        if (!acc.getPassword().equals(user.getPassword())) {
            //return status 401
        }

        return acc;
    }
}
