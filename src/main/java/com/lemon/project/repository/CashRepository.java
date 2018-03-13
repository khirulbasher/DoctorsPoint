package com.lemon.project.repository;

import com.lemon.project.domain.Cash;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cash entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashRepository extends JpaRepository<Cash, Long> {

}
