package com.incident.repository;

import com.incident.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserinfoRepository extends JpaRepository<UserInfo,Long> {
     Optional<UserInfo> findByEmail(String email);
}
