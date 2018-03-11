package com.lemon.project.web.rest.external;

import com.codahale.metrics.annotation.Timed;
import com.lemon.project.domain.Country;
import com.lemon.project.repository.CountryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by lemon on 3/11/2018.
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "StringBufferMayBeStringBuilder", "ConstantConditions", "RedundantThrows"})
@RestController
@RequestMapping("/ex")
public class ExternalTerritoryResource {

    private final CountryRepository countryRepository;

    @Inject
    public ExternalTerritoryResource(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @GetMapping("/all/country/{limit}/{from}")
    @Timed
    public ResponseEntity<List<Country>> findAllCountry(@PathVariable("limit") Long limit,@PathVariable("from") Long from) {
        return Optional.ofNullable(countryRepository.findAllByLimit(limit,from)).map(val->new ResponseEntity<>(val, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
