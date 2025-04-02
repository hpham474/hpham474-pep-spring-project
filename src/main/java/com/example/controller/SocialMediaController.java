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
import com.example.exception.MessageNotFoundException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserRegistrationException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
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
    public ResponseEntity<String> updateMessage(@PathVariable int messageId, @RequestBody String messageText) {
        try {
            return new ResponseEntity<>(messageService.updateMessage(messageId, messageText), HttpStatus.OK);
        } catch (MessageNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
