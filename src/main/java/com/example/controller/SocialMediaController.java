package com.example.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.AuthenticationFailedException;
import com.example.exception.MessageCreationFailedException;
import com.example.exception.MessageNotFoundException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserRegistrationException;
import com.example.service.AccountService;
import com.example.service.MessageService;

@RestController
public class SocialMediaController {
    private MessageService messageService;
    private AccountService accountService;

    @Autowired
    public SocialMediaController(MessageService messageService, AccountService accountService) {
        this.messageService = messageService;
        this.accountService = accountService;
    }

    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        try {
            accountService.registerAccount(account);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(account, HttpStatus.CONFLICT);
        } catch (UserRegistrationException e) {
            return new ResponseEntity<>(account, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        try {
            return new ResponseEntity<>(accountService.loginAccount(account), HttpStatus.OK);
        } catch (AuthenticationFailedException e) {
            return new ResponseEntity<>(account, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try {
            return new ResponseEntity<>(messageService.createMessage(message), HttpStatus.OK);
        } catch (MessageCreationFailedException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("messages")
    public List<Message> getMessageList() {
        return messageService.getMessageList();
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable int messageId) {
        return new ResponseEntity<>(messageService.getMessage(messageId), HttpStatus.OK);
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable int messageId) {
        return new ResponseEntity<>(messageService.deleteMessage(messageId), HttpStatus.OK);
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<String> updateMessage(@PathVariable int messageId, @RequestBody Message updatedMessage) {
        try {
            return new ResponseEntity<>(messageService.updateMessage(messageId, updatedMessage), HttpStatus.OK);
        } catch (MessageNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("accounts/{accountId}/messages")
    public List<Message> getUserMessages(@PathVariable int accountId) {
        return accountService.getUserMessages(accountId);
    }
}
