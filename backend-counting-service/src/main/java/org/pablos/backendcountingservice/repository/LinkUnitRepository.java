package org.pablos.backendcountingservice.repository;

import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkUnitRepository extends JpaRepository<LinkUnit, Long> {
    Optional<LinkUnit> findByShortLink(String link);
}
