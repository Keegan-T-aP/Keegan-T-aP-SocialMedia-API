package Service;

import Model.Account;
import DAO.AccountDAO;

//import java.util.*;
import io.javalin.Javalin;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO dao){
        accountDAO = dao;
    }

    public Account getAccountById(int id) {
        return accountDAO.getAccountByID(id);
    }

    public Account addAccount(Account user) {
                //test to make sure requirements are met
        if (user.getUsername() == "" || user.getPassword().length() < 4) {
            return null;
        }

        if(accountDAO.getAccountByUsername(user.getUsername()) != null) {
            return null;
        }

        // make the new user
        return accountDAO.insertUser(user);
    }

    public Account accountLogin(Account user) {
        if (accountDAO.getAccountByUsername(user.getUsername()) == null) {
            return null;
        }

        Account acc = accountDAO.getAccountByUsername(user.getUsername());

        if (!acc.getPassword().equals(user.getPassword())) {
            return null;
        }

        return accountDAO.getAccountByUsername(user.getUsername());
    }
}
