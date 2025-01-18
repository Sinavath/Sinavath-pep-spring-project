package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody Account account) {
        try {
            Account createdAccount = accountService.registerAccount(account);
            return ResponseEntity.ok(createdAccount);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody Account account) {
        try {
            Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(loggedInAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage (@RequestBody Message message) {
        try {
            Message createdMessage = messageService.create(message);
            return ResponseEntity.ok(createdMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages () {
        List<Message> messages = messageService.getAll();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById (@PathVariable Integer messageId) {
        Optional<Message> message = messageService.getById(messageId);
        return ResponseEntity.ok(message.orElse(null));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage (@PathVariable Integer messageId) {
        boolean isDeleted = messageService.deleteById(messageId);
        if (isDeleted) {
            return ResponseEntity.ok(1); 
        } else {
            return ResponseEntity.ok().build(); 
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage (@PathVariable Integer messageId, @RequestBody String jsonPayload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonPayload);
            String newMessageText = jsonNode.get("messageText").asText();
    
            int rowsUpdated = messageService.update(messageId, newMessageText);
            return ResponseEntity.ok(rowsUpdated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("");
        }
    }

        @GetMapping("/accounts/{accountId}/messages")
        public ResponseEntity<List<Message>> getAllMessagesByUser (@PathVariable Integer accountId) {
            List<Message> messages = messageService.getByAccountId(accountId);
            return ResponseEntity.ok(messages);
        }
}
