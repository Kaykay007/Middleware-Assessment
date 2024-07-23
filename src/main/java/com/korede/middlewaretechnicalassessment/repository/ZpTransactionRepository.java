package com.korede.middlewaretechnicalassessment.repository;

import com.korede.middlewaretechnicalassessment.entity.ZpTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZpTransactionRepository extends JpaRepository<ZpTransaction, Long> {
    Optional<ZpTransaction> findByTransactionReference(String transactionReference);
}

