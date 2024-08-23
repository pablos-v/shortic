package org.pablos.backendgivingservice.repository;

import org.pablos.backendgivingservice.entity.FastLink;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FastLinkRepository extends MongoRepository<FastLink, String> {
}

