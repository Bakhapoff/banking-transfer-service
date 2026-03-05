package kg.ab.transfer.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import kg.ab.transfer.model.enums.OperationType;
import kg.ab.transfer.model.enums.TransactionStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal balanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "counterpart_account_id")
    private Account counterpartAccount;

    private Transaction() {}

    public static Transaction debit(Account fromAccount, Account toAccount, BigDecimal amount, BigDecimal balanceAfter) {
        Transaction tx = new Transaction();
        tx.operationType = OperationType.DEBIT;
        tx.account = fromAccount;
        tx.counterpartAccount = toAccount;
        tx.amount = amount;
        tx.balanceAfter = balanceAfter;
        tx.status = TransactionStatus.SUCCESS;
        return tx;
    }

    public static Transaction credit(Account fromAccount, Account toAccount, BigDecimal amount, BigDecimal balanceAfter) {
        Transaction tx = new Transaction();
        tx.operationType = OperationType.CREDIT;
        tx.account = toAccount;
        tx.counterpartAccount = fromAccount;
        tx.amount = amount;
        tx.balanceAfter = balanceAfter;
        tx.status = TransactionStatus.SUCCESS;
        return tx;
    }

    public static Transaction failed(Account fromAccount, Account toAccount, BigDecimal amount, OperationType operationType) {
        Transaction tx = new Transaction();
        tx.operationType = operationType;
        tx.account = fromAccount;
        tx.counterpartAccount = toAccount;
        tx.amount = amount;
        tx.balanceAfter = fromAccount.getBalance();
        tx.status = TransactionStatus.FAILED;
        return tx;
    }
}