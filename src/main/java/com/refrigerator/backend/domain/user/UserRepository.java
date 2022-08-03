package com.refrigerator.backend.domain.user;

import com.refrigerator.backend.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
