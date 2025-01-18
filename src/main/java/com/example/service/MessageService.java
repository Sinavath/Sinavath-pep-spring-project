package com.example.service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message create(Message message) {
        // check if message is empty
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        // check if message has more than 255 chars
        if (message.getMessageText().length() >= 255) {
            throw new IllegalArgumentException();
        }
        // make sure message posted by user
        if (message.getPostedBy() == null) {
            throw new IllegalArgumentException();
        }
        Optional<Account> account = accountRepository.findById(message.getPostedBy());
        if (account.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return messageRepository.save(message);
    }

    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    public Optional<Message> getById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    public int update(Integer messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            throw new IllegalArgumentException();
        }

        Message existingMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException());

        existingMessage.setMessageText(newMessageText);
        messageRepository.save(existingMessage);

        return 1;
    }

    public boolean deleteById(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }
        return false; 
    }

    public List<Message> getByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
