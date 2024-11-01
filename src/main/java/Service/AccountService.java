package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO dao;

    public AccountService(){
        dao = new AccountDAO();
    }

    public Account registerAccount(Account newAccount){
        if (!isValidAccount(newAccount)){
            return null;
        }
        return dao.insertAccount(newAccount);
    }

    public Account login(Account accountToLogin){
        if (!isValidAccount(accountToLogin)){
            return null;
        }
        return dao.loginAccount(accountToLogin);
    }

    private  boolean isValidAccount(Account accountToValidate){
        //Validate account based off user story requirement.
        if (accountToValidate == null){
            return false;
        }
        if (accountToValidate.getUsername().isBlank() || accountToValidate.getPassword().isBlank()){
            return false;
        }
        if (accountToValidate.getPassword().length() < 4){
            return false;
        }

        return true;
    }
}
