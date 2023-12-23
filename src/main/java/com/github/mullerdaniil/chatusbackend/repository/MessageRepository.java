package com.github.mullerdaniil.chatusbackend.repository;

import com.github.mullerdaniil.chatusbackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "select * from message order by timestamp desc limit :limit", nativeQuery = true)
    List<Message> findLastMessages(int limit);
}
