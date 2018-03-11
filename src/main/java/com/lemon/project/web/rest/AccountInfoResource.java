package com.lemon.project.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lemon.project.domain.AccountInfo;

import com.lemon.project.repository.AccountInfoRepository;
import com.lemon.project.security.AuthoritiesConstants;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AccountInfo.
 */
@RestController
@RequestMapping("/api")
@Secured({AuthoritiesConstants.ADMIN,AuthoritiesConstants.ROLE_MGT})
public class AccountInfoResource {

    private final Logger log = LoggerFactory.getLogger(AccountInfoResource.class);

    private static final String ENTITY_NAME = "accountInfo";

    private final AccountInfoRepository accountInfoRepository;

    public AccountInfoResource(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }

    /**
     * POST  /account-infos : Create a new accountInfo.
     *
     * @param accountInfo the accountInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountInfo, or with status 400 (Bad Request) if the accountInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-infos")
    @Timed
    public ResponseEntity<AccountInfo> createAccountInfo(@Valid @RequestBody AccountInfo accountInfo) throws URISyntaxException {
        log.debug("REST request to save AccountInfo : {}", accountInfo);
        if (accountInfo.getId() != null) {
            throw new BadRequestAlertException("A new accountInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountInfo result = accountInfoRepository.save(accountInfo);
        return ResponseEntity.created(new URI("/api/account-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-infos : Updates an existing accountInfo.
     *
     * @param accountInfo the accountInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountInfo,
     * or with status 400 (Bad Request) if the accountInfo is not valid,
     * or with status 500 (Internal Server Error) if the accountInfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-infos")
    @Timed
    public ResponseEntity<AccountInfo> updateAccountInfo(@Valid @RequestBody AccountInfo accountInfo) throws URISyntaxException {
        log.debug("REST request to update AccountInfo : {}", accountInfo);
        if (accountInfo.getId() == null) {
            return createAccountInfo(accountInfo);
        }
        AccountInfo result = accountInfoRepository.save(accountInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-infos : get all the accountInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountInfos in body
     */
    @GetMapping("/account-infos")
    @Timed
    public ResponseEntity<List<AccountInfo>> getAllAccountInfos(Pageable pageable) {
        log.debug("REST request to get a page of AccountInfos");
        Page<AccountInfo> page = accountInfoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /account-infos/:id : get the "id" accountInfo.
     *
     * @param id the id of the accountInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountInfo, or with status 404 (Not Found)
     */
    @GetMapping("/account-infos/{id}")
    @Timed
    public ResponseEntity<AccountInfo> getAccountInfo(@PathVariable Long id) {
        log.debug("REST request to get AccountInfo : {}", id);
        AccountInfo accountInfo = accountInfoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountInfo));
    }

    /**
     * DELETE  /account-infos/:id : delete the "id" accountInfo.
     *
     * @param id the id of the accountInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountInfo(@PathVariable Long id) {
        log.debug("REST request to delete AccountInfo : {}", id);
        accountInfoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
