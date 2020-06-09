package com.demo.repository;

 import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.ServerDetail;
/**
 * Mongo Repository.
 * @author vinayak
 *
 */
@Repository
public interface  ServerDetailsRepository extends  MongoRepository<ServerDetail, String>{

}
