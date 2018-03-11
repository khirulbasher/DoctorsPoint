package com.lemon.project.repository;

import com.lemon.project.domain.Country;
import feign.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Country entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query("SELECT model FROM Country model WHERE model.id >= :fromId AND COUNT(model)<=:limitNo")
    List<Country> findAllByLimit(@Param("limitNo") Long limitNo,@Param("fromId") Long fromId);
}
