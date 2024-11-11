package com.patlytics.take_home.repository.patent;

import com.patlytics.take_home.model.PatentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatentRepository extends MongoRepository<PatentEntity, String> {
    Optional<PatentEntity> findByPublicationNumber(String publicationNumber);
}
