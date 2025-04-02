package com.example.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.MessageCreationFailedException;
import com.example.exception.MessageNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) throws MessageCreationFailedException {
        if (
            message.getMessageText().isBlank() ||
            message.getMessageText().length() > 255 ||
            !accountRepository.findById(message.getPostedBy()).isPresent()
        ) {
            throw new MessageCreationFailedException("Message failed to be created");
        }
        return messageRepository.save(message);
    }

    public List<Message> getMessageList() {
        return messageRepository.findAll();
    }

    public Message getMessage(int messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get();
        }
        return null;
    }

    public String deleteMessage(int messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            messageRepository.deleteById(messageId);
            return "1";
        }
        return null;
    }

    public String updateMessage(int messageId, Message updatedMessage) throws MessageNotFoundException {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (
            !optionalMessage.isPresent() ||
            updatedMessage.getMessageText().length() > 255 ||
            updatedMessage.getMessageText().isBlank()
        ) {
            throw new MessageNotFoundException("Error updating message");
        }
        Message message = optionalMessage.get();
        message.setMessageText(updatedMessage.getMessageText());
        messageRepository.save(message);
        return "1";
    }
}