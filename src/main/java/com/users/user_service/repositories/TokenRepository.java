package com.users.user_service.repositories;

import com.users.user_service.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Token save(Token token);

    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(String token, Boolean deleted, Date expiryAt);

//    Boolean findByValueAndMarkAsDeletedSaveDete(String token, Boolean Deleted);
    @Modifying
    @Query("UPDATE Token SET deleted = true WHERE value = :token")
    int updateDeletedByValue(String token);


}
