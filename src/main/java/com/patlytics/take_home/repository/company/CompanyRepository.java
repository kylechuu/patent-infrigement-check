package com.patlytics.take_home.repository.company;

import com.patlytics.take_home.model.CompanyEntity;
import com.patlytics.take_home.model.PatentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends MongoRepository<CompanyEntity, String> {
    Optional<CompanyEntity> findByName(String name);
}
