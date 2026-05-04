package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
    private final DatabaseConduit databaseConduit;
    private final RestTemplate restTemplate;

    public TransactionListener(DatabaseConduit databaseConduit, RestTemplate restTemplate) {
        this.databaseConduit = databaseConduit;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    @Transactional
    public void listen(Transaction transaction) {
        logger.info("Received transaction: {}", transaction);

        UserRecord sender = databaseConduit.getUserById(transaction.getSenderId());
        UserRecord recipient = databaseConduit.getUserById(transaction.getRecipientId());

        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {
            
            // Deduct transaction amount from sender
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            
            // Call Incentive API
            Incentive incentive = restTemplate.postForObject("http://localhost:8080/incentive", transaction, Incentive.class);
            float incentiveAmount = (incentive != null) ? incentive.getAmount() : 0.0f;

            // Add transaction amount + incentive to recipient
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

            // Save records
            databaseConduit.save(sender);
            databaseConduit.save(recipient);

            TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount);
            databaseConduit.saveTransaction(transactionRecord);

            logger.info("Transaction validated, incentive received ({}), and saved: {}", incentiveAmount, transaction);
        } else {
            logger.info("Transaction invalid and discarded: {}", transaction);
        }
    }
}
