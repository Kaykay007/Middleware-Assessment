package com.korede.middlewaretechnicalassessment.repository;

import com.korede.middlewaretechnicalassessment.entity.ZpCustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZpAccountRepository extends JpaRepository<ZpCustomerAccount, Long> {
   Optional<ZpCustomerAccount> findByAccountNumber(String accountNumber);
}
