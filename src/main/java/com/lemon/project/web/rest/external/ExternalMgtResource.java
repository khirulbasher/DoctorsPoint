package com.lemon.project.web.rest.external;

import com.codahale.metrics.annotation.Timed;
import com.lemon.project.domain.Cash;
import com.lemon.project.domain.Transaction;
import com.lemon.project.repository.CashRepository;
import com.lemon.project.repository.TransactionRepository;
import com.lemon.project.utility.model.PageUtil;
import com.lemon.project.web.rest.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by lemon on 3/20/2018.
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "StringBufferMayBeStringBuilder", "ConstantConditions", "RedundantThrows"})
@RestController
@RequestMapping("/ex")
public class ExternalMgtResource {
    private final CashRepository cashRepository;
    private final TransactionRepository transactionRepository;

    @Inject
    public ExternalMgtResource(CashRepository cashRepository, TransactionRepository transactionRepository) {
        this.cashRepository = cashRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/mgt/cash")
    @Timed
    public ResponseEntity<List<Cash>> allCash(Pageable pageable) {
        Page<Cash> cash=cashRepository.findAll(pageable);
        HttpHeaders httpHeaders= PaginationUtil.generatePaginationHttpHeaders(cash,"/mgt/cash");
        return new ResponseEntity<>(cash.getContent(),httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/mgt/transaction")
    @Timed
    public ResponseEntity<List<Transaction>> allTransaction(Pageable pageable) {
        Page<Transaction> cash=transactionRepository.findAll(pageable);
        HttpHeaders httpHeaders= PaginationUtil.generatePaginationHttpHeaders(cash,"/mgt/transaction");
        return new ResponseEntity<>(cash.getContent(),httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/mgt/cashAll/{size}")
    @Timed
    public ResponseEntity<List<Cash>> allCashList(@PathVariable Integer size) {
        if(size<=0)
            return new ResponseEntity<>(cashRepository.findAll(), HttpStatus.OK);
        return new ResponseEntity<>(cashRepository.findAll(new PageUtil(size,0)).getContent(), HttpStatus.OK);
    }

    @GetMapping("/mgt/transactionAll/{size}")
    @Timed
    public ResponseEntity<List<Transaction>> allTransactionList(@PathVariable Integer size) {
        if(size<=0)
            return new ResponseEntity<>(transactionRepository.findAll(),HttpStatus.OK);
        return new ResponseEntity<>(transactionRepository.findAll(new PageRequest(0,size)).getContent(),HttpStatus.OK);
    }
}
