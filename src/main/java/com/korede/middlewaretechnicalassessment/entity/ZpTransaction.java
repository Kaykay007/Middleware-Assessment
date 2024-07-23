package com.korede.middlewaretechnicalassessment.entity;

import com.korede.middlewaretechnicalassessment.enums.ZpTransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "zp_transaction")
public class ZpTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "transaction_reference", unique = true)
    private String transactionReference;
    @Column
    private BigDecimal amount;



    @Column(name = "date_created")
    private LocalDateTime createdAt;
    private String statusMessage;
    private String paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_account_id")
    private ZpCustomerAccount senderAccount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_account_id")
    private ZpCustomerAccount recipientAccount;

    @Enumerated(EnumType.STRING)
    private ZpTransactionStatus status;
}
