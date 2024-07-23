package com.korede.middlewaretechnicalassessment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "zp_customer_account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ZpCustomerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column
    private BigDecimal balance;

    @OneToMany(mappedBy = "senderAccount", cascade = CascadeType.ALL)
    private List<ZpTransaction> sentTransactions;

    @OneToMany(mappedBy = "recipientAccount", cascade = CascadeType.ALL)
    private List<ZpTransaction> receivedTransactions;
}
