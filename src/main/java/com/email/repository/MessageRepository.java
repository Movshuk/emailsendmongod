package com.email.repository;


import com.email.model.Message;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    @Query("{'attachments' : ?0}")
    List<Message> getAllWithAttachment(String attachment);

    @Query("{$and :["
            + "?#{ [0] == null ? { $where : 'true'} : { '_id' : [0] } },"
            + "?#{ [1] == null ? { $where : 'true'} : { 'status' : [1] } },"
            + "?#{ [2] == null ? { $where : 'true'} : { 'subject' : [2] } },"
            + "?#{ [3] == null ? { $where : 'true'} : { 'from' : [3] } },"
            + "?#{ [4] == null ? { $where : 'true'} : { 'to' : [4] } },"
            + "]}")
    List<Message> getAllMessagesByParams(ObjectId id,
                                 String status,
                                 String subject,
                                 String from,
                                 String to);
}
