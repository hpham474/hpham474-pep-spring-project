package com.example.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.AuthenticationFailedException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserRegistrationException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private MessageRepository messageRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, MessageRepository messageRepository) {
        this.accountRepository = accountRepository;
        this.messageRepository = messageRepository;
    }

    public void registerAccount(Account account) throws UserAlreadyExistsException, UserRegistrationException {
        if (
            !account.getUsername().isBlank()&& 
            account.getPassword().length() >= 4 && 
            accountRepository.findByUsername(account.getUsername()) == null
        ) {
            accountRepository.save(account);
        } else if (
            !account.getUsername().isBlank() &&
            account.getPassword().length() >= 4
        ) {
            throw new UserAlreadyExistsException("User already exists");
        } else {
            throw new UserRegistrationException("Error making new account");
        }
    }

    public Account loginAccount(Account account) throws AuthenticationFailedException {
        Account attemptedLogin = accountRepository.findByUsername(account.getUsername());
        if (attemptedLogin == null || !attemptedLogin.getPassword().equals(account.getPassword())) {
            throw new AuthenticationFailedException("The username or password you entered is incorrect");
        }
        return attemptedLogin;
    }

    public List<Message> getUserMessages(int accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            return messageRepository.findAllByPostedBy(accountId);
        }
        return null;
    }
}
