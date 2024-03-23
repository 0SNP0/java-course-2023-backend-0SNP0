package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
}
