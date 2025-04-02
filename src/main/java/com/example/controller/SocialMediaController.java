package com.example.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.AuthenticationFailedException;
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
}
