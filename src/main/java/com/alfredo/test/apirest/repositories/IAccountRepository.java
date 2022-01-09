package com.alfredo.test.apirest.repositories;

import com.alfredo.test.apirest.models.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepository extends JpaRepository<Account,Integer> {

    Account findByAccount(String account);
    
}
