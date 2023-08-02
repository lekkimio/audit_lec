package com.example.auditlec.repo;

import com.example.auditlec.model.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, String>, RevisionRepository<ShippingAddress, String, Long> {
}