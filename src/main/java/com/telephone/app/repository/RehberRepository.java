package com.telephone.app.repository;

import com.telephone.app.domain.Rehber;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rehber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RehberRepository extends JpaRepository<Rehber, Long> {}
