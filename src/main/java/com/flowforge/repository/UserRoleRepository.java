package com.flowforge.repository;

import com.flowforge.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query(nativeQuery = false, value = "SELECT ur FROM UserRole ur WHERE ur.userRoles.id=:id")
    List<UserRole> findUserRoleByUserId(@Param("id") Long id);

    @Query(nativeQuery = false, value = "SELECT ur FROM UserRole ur WHERE ur.userRoles.email=:email")
    List<UserRole> findUserRoleByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(nativeQuery = false, value = "DELETE FROM UserRole ur WHERE ur.userRoles.id=:id")
    void deleteUserRoleByUserId(@Param("id") Long id);
}
