package com.lemon.project.repository;

import com.lemon.project.domain.Thana;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Thana entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThanaRepository extends JpaRepository<Thana, Long> {

}
