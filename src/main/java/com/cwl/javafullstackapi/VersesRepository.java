package com.cwl.javafullstackapi;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersesRepository extends MongoRepository<BibleStudyPlan, ObjectId> {
}
