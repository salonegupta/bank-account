package com.banking.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banking.model.Transaction;

@Repository
public interface TransactionDao extends CrudRepository<Transaction, Long> {

	@Query(value = "SELECT COALESCE(sum(transaction.amount), 0) as sum, count(transaction.id) as count from Transaction transaction "
			+ "where transaction.account.number = :accountNumber and transaction.type = :type and "
			+ "transaction.date >= :startDate and transaction.date <= :endDate")
	List<Object[]> getTransactionTotalByAccountNumberAndDate(@Param("accountNumber") String accountNumber,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("type") Transaction.TransactionType type);

	@Query(value = "from Transaction transaction where transaction.account.number = :accountNumber and transaction.id = :id ")
	Transaction findByAccountNumberAndId(@Param("accountNumber") String accountNumber, @Param("id") long id);
}
