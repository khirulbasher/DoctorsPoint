package com.lemon.project.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lemon.project.domain.Country;
import com.lemon.project.repository.CountryRepository;
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

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Country.
 */
@RestController
@RequestMapping("/api")
public class CountryResource {

    private final Logger log = LoggerFactory.getLogger(CountryResource.class);

    private static final String ENTITY_NAME = "country";

    private final CountryRepository countryRepository;
    private final EntityService entityService;

    @Inject
    public CountryResource(CountryRepository countryRepository, EntityService entityService) {
        this.countryRepository = countryRepository;
        this.entityService = entityService;
    }

    /**
     * POST  /countries : Create a new country.
     *
     * @param country the country to create
     * @return the ResponseEntity with status 201 (Created) and with body the new country, or with status 400 (Bad Request) if the country has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/countries")
    @Timed
    public ResponseEntity<Country> createCountry(@Valid @RequestBody Country country) throws URISyntaxException {
        log.debug("REST request to save Country : {}", country);
        if (country.getId() != null) {
            throw new BadRequestAlertException("A new country cannot already have an ID", ENTITY_NAME, "idexists");
        }
        modify(country);
        Country result = countryRepository.save(country);
        return ResponseEntity.created(new URI("/api/countries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /countries : Updates an existing country.
     *
     * @param country the country to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated country,
     * or with status 400 (Bad Request) if the country is not valid,
     * or with status 500 (Internal Server Error) if the country couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/countries")
    @Timed
    public ResponseEntity<Country> updateCountry(@Valid @RequestBody Country country) throws URISyntaxException {
        log.debug("REST request to update Country : {}", country);
        if (country.getId() == null) {
            return createCountry(country);
        }
        modify(country);
        Country result = countryRepository.save(country);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, country.getId().toString()))
            .body(result);
    }

    /**
     * GET  /countries : get all the countries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of countries in body
     */
    @GetMapping("/countries")
    @Timed
    public ResponseEntity<List<Country>> getAllCountries(Pageable pageable) {
        log.debug("REST request to get a page of Countries");
        Page<Country> page = countryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/countries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /countries/:id : get the "id" country.
     *
     * @param id the id of the country to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the country, or with status 404 (Not Found)
     */
    @GetMapping("/countries/{id}")
    @Timed
    public ResponseEntity<Country> getCountry(@PathVariable Long id) {
        log.debug("REST request to get Country : {}", id);
        Country country = countryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(country));
    }

    /**
     * DELETE  /countries/:id : delete the "id" country.
     *
     * @param id the id of the country to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/countries/{id}")
    @Timed
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        log.debug("REST request to delete Country : {}", id);
        countryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    private void modify(Country country) {
        try {
            entityService.modify(country);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
