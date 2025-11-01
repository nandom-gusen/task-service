package com.flowforge.repository;

import com.flowforge.model.User;
import com.flowforge.utils.Utils;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hibernate.jpa.HibernateHints.HINT_CACHEABLE;
import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;
import static org.hibernate.jpa.QueryHints.HINT_READONLY;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.email=:email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.phone=:phone")
    Optional<User> findByPhone(@Param("phone") String phone);

//    @Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.handle=:handle")
//    Optional<User> findByHandle(@Param("handle") String handle);

    @Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.email=:emailOrPhone OR u.phone=:emailOrPhone")
    Optional<User> findByEmailOrPhone(@Param("emailOrPhone") String emailOrPhone);

    @Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.userId=:userId")
    Optional<User> findByUserId(@Param("userId") String userId);

//    @Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.locked=true")
//    List<User> findAllLockedAccounts();
//
//    @Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.locked=true")
//    Page<User> findAllLockedAccounts(Pageable pageable);
//
//    @Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.locked=true")
//    @QueryHints(value = {
//            @QueryHint(name = HINT_FETCH_SIZE, value = "" + Utils.BATCH_SIZE + ""),
//            @QueryHint(name = HINT_CACHEABLE, value = "false"),
//            @QueryHint(name = HINT_READONLY, value = "true")
//    })
//    Stream<User> streamAllLockedAccounts();

    @Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.status= :status")
    Page<User> findAllByStatus(@Param("status") String status, Pageable pageable);

    @Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.status= :status")
    List<User> findAllByStatus(@Param("status") String status);

    //////////////////////ADMIN DASHBOARD METRICS/////////////////////////////////
    //////////////////////ADMIN DASHBOARD METRICS/////////////////////////////////
    //////////////////////ADMIN DASHBOARD METRICS/////////////////////////////////
    //////////////////////ADMIN DASHBOARD METRICS/////////////////////////////////
    @Query(nativeQuery = false, value = "SELECT COUNT(u) FROM User u")
    Long countAllClients();

    @Query(nativeQuery = false, value = "SELECT COUNT(u) FROM User u WHERE u.status='ACTIVE' and u.activated=true")
    Long countAllActiveClients();

    @Query("SELECT u FROM User u WHERE u.dateOfBirth = :dateOfBirth")
    Page<User> findAllByDateOfBirth(@Param("dateOfBirth") LocalDate dateOfBirth, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.dateOfBirth = :dateOfBirth")
    Long countAllByDateOfBirth(@Param("dateOfBirth") LocalDate dateOfBirth);
}


