package com.lemon.project.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lemon.project.domain.Cash;

import com.lemon.project.repository.CashRepository;
import com.lemon.project.service.EntityService;
import com.lemon.project.web.rest.errors.BadRequestAlertException;
import com.lemon.project.web.rest.util.HeaderUtil;
import com.lemon.project.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Cash.
 */
@RestController
@RequestMapping("/api")
public class CashResource {

    private final Logger log = LoggerFactory.getLogger(CashResource.class);

    private static final String ENTITY_NAME = "cash";

    private final CashRepository cashRepository;

    public CashResource(CashRepository cashRepository) {
        this.cashRepository = cashRepository;
    }

    /**
     * POST  /cash : Create a new cash.
     *
     * @param cash the cash to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cash, or with status 400 (Bad Request) if the cash has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cash")
    @Timed
    public ResponseEntity<Cash> createCash(@Valid @RequestBody Cash cash) throws URISyntaxException {
        log.debug("REST request to save Cash : {}", cash);
        if (cash.getId() != null) {
            throw new BadRequestAlertException("A new cash cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cash.setLastTransactionDate(LocalDate.now());
        Cash result = cashRepository.save(cash);
        return ResponseEntity.created(new URI("/api/cash/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cash : Updates an existing cash.
     *
     * @param cash the cash to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cash,
     * or with status 400 (Bad Request) if the cash is not valid,
     * or with status 500 (Internal Server Error) if the cash couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cash")
    @Timed
    public ResponseEntity<Cash> updateCash(@Valid @RequestBody Cash cash) throws URISyntaxException {
        log.debug("REST request to update Cash : {}", cash);
        if (cash.getId() == null) {
            return createCash(cash);
        }
        Cash result = cashRepository.save(cash);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cash.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cash : get all the cash.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cash in body
     */
    @GetMapping("/cash")
    @Timed
    public ResponseEntity<List<Cash>> getAllCash(Pageable pageable) {
        log.debug("REST request to get a page of Cash");
        Page<Cash> page = cashRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cash");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cash/:id : get the "id" cash.
     *
     * @param id the id of the cash to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cash, or with status 404 (Not Found)
     */
    @GetMapping("/cash/{id}")
    @Timed
    public ResponseEntity<Cash> getCash(@PathVariable Long id) {
        log.debug("REST request to get Cash : {}", id);
        Cash cash = cashRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cash));
    }

    /**
     * DELETE  /cash/:id : delete the "id" cash.
     *
     * @param id the id of the cash to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cash/{id}")
    @Timed
    public ResponseEntity<Void> deleteCash(@PathVariable Long id) {
        log.debug("REST request to delete Cash : {}", id);
        cashRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
