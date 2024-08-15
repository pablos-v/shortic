package org.pablos.backendcountingservice.repository;

import org.pablos.backendcountingservice.domain.entity.Click;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickRepository extends JpaRepository<Click, Long> {
}
