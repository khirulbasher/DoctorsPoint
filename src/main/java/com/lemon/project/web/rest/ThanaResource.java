package com.lemon.project.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lemon.project.domain.Thana;

import com.lemon.project.repository.ThanaRepository;
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
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Thana.
 */
@RestController
@RequestMapping("/api")
public class ThanaResource {

    private final Logger log = LoggerFactory.getLogger(ThanaResource.class);

    private static final String ENTITY_NAME = "thana";

    private final ThanaRepository thanaRepository;
    private final EntityService entityService;

    public ThanaResource(ThanaRepository thanaRepository, EntityService entityService) {
        this.thanaRepository = thanaRepository;
        this.entityService = entityService;
    }

    /**
     * POST  /thanas : Create a new thana.
     *
     * @param thana the thana to create
     * @return the ResponseEntity with status 201 (Created) and with body the new thana, or with status 400 (Bad Request) if the thana has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/thanas")
    @Timed
    public ResponseEntity<Thana> createThana(@Valid @RequestBody Thana thana) throws URISyntaxException {
        log.debug("REST request to save Thana : {}", thana);
        if (thana.getId() != null) {
            throw new BadRequestAlertException("A new thana cannot already have an ID", ENTITY_NAME, "idexists");
        }
        modify(thana);
        Thana result = thanaRepository.save(thana);
        return ResponseEntity.created(new URI("/api/thanas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /thanas : Updates an existing thana.
     *
     * @param thana the thana to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated thana,
     * or with status 400 (Bad Request) if the thana is not valid,
     * or with status 500 (Internal Server Error) if the thana couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/thanas")
    @Timed
    public ResponseEntity<Thana> updateThana(@Valid @RequestBody Thana thana) throws URISyntaxException {
        log.debug("REST request to update Thana : {}", thana);
        if (thana.getId() == null) {
            return createThana(thana);
        }
        Thana result = thanaRepository.save(thana);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, thana.getId().toString()))
            .body(result);
    }

    /**
     * GET  /thanas : get all the thanas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of thanas in body
     */
    @GetMapping("/thanas")
    @Timed
    public ResponseEntity<List<Thana>> getAllThanas(Pageable pageable) {
        log.debug("REST request to get a page of Thanas");
        Page<Thana> page = thanaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/thanas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /thanas/:id : get the "id" thana.
     *
     * @param id the id of the thana to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the thana, or with status 404 (Not Found)
     */
    @GetMapping("/thanas/{id}")
    @Timed
    public ResponseEntity<Thana> getThana(@PathVariable Long id) {
        log.debug("REST request to get Thana : {}", id);
        Thana thana = thanaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(thana));
    }

    /**
     * DELETE  /thanas/:id : delete the "id" thana.
     *
     * @param id the id of the thana to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/thanas/{id}")
    @Timed
    public ResponseEntity<Void> deleteThana(@PathVariable Long id) {
        log.debug("REST request to delete Thana : {}", id);
        thanaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    private void modify(Thana thana) {
        try {
            entityService.modify(thana);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
