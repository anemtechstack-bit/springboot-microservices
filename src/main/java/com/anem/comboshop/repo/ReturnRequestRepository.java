package com.anem.comboshop.repo;

import com.anem.comboshop.domain.ReturnRequest;
import com.anem.comboshop.domain.ReturnStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {
    List<ReturnRequest> findByUsernameOrderByCreatedAtDesc(String username);
    List<ReturnRequest> findByStatusOrderByCreatedAtDesc(ReturnStatus status);
}
