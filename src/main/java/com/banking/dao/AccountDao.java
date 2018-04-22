package com.banking.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.banking.model.Account;

@Repository
public interface AccountDao extends CrudRepository<Account, Long> {
	Account findByNumber(String accountNumber);
}
