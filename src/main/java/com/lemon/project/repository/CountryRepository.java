package com.lemon.project.repository;

import com.lemon.project.domain.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Country entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query("SELECT model FROM Country model WHERE model.id >= :fromId")
    List<Country> findAllByLimit(@Param("fromId") Long fromId, Pageable pageable);

    @Query(value = "SELECT model FROM Country model WHERE model.id >= :fromId LIMIT :lim",nativeQuery = true)
    List<Country> findAllByNativeLimit(@Param("fromId") Long fromId, @Param("lim") Integer lim);
}
