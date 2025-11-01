package com.flowforge.repository;

import com.flowforge.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(nativeQuery = false, value = "SELECT r FROM Role r WHERE r.uuid=:uuid")
    Optional<Role> findRoleByUuid(@Param("uuid") String uuid);

    @Query(nativeQuery = false, value = "SELECT r FROM Role r WHERE r.name=:name")
    Optional<Role> findRoleByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(nativeQuery = false, value = "DELETE FROM Role r WHERE r.name=:name")
    void deleteRoleByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(nativeQuery = false, value = "DELETE FROM Role r WHERE r.uuid=:uuid")
    void deleteRoleByUuid(@Param("uuid") String uuid);
}

