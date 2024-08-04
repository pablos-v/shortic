package org.pablos.backendcountingservice.repository;

import com.ctc.wstx.util.ElementId;
import org.pablos.backendcountingservice.domain.entity.Click;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClickRepository extends JpaRepository<Click, Long> {
//    Page<Click> findAllByLinkId(Long linkId, Pageable pageable);
}
