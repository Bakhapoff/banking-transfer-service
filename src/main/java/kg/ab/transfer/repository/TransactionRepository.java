package kg.ab.transfer.repository;

import kg.ab.transfer.model.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.account.accountNumber = :accountNumber " +
            "AND t.status = kg.ab.transfer.model.enums.TransactionStatus.SUCCESS " +
            "AND (cast(:from as localDateTime) IS NULL OR t.createdAt >= :from) " +
            "AND (cast(:to as localDateTime) IS NULL OR t.createdAt <= :to)")
    Page<Transaction> findByAccountNumberAndDateRange(@Param("accountNumber") String accountNumber,
                                                      @Param("from") LocalDateTime from,
                                                      @Param("to") LocalDateTime to,
                                                      Pageable pageable);
}