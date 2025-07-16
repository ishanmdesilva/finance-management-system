package com.inova.pfms.repository;

import com.inova.pfms.entity.OtpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRequestRepository extends JpaRepository<OtpRequest, String> {
    Optional<OtpRequest> findFirstByStatusAndActionOrderByGeneratedOnDesc(String status, String action);
}
