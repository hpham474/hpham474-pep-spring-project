package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserRegistrationException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void registerAccount(Account account) throws UserAlreadyExistsException, UserRegistrationException {
        if (
            !account.getUsername().isEmpty()&& 
            account.getPassword().length() >= 4 && 
            accountRepository.findByUsername(account.getUsername()) == null
        ) {
            accountRepository.save(account);
        } else if (
            !account.getUsername().isEmpty() &&
            account.getPassword().length() >= 4
        ) {
            throw new UserAlreadyExistsException("User already exists");
        } else {
            throw new UserRegistrationException("Error making new account");
        }
    }


}
