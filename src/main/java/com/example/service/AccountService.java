package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(Account account) {
        // not blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        // at least 4 characters long
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException();
        }
        // already exists
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount.isPresent()) {
            throw new IllegalStateException();
        }

        return accountRepository.save(account);
    }

    public Account login(String username, String password) {
        // check if username is filled
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        // check if password is filled
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        // find account
        Optional<Account> account = accountRepository.findByUsernameAndPassword(username, password);
        return account.orElseThrow(() -> new IllegalArgumentException());
    }
}
