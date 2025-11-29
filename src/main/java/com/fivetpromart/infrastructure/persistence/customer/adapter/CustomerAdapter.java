package com.fivetpromart.infrastructure.persistence.customer.adapter;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.port.out.ICustomerRepository;
import com.fivetpromart.application.port.out.ISignUpRequestRepository;
import com.fivetpromart.domain.model.Customer;
import com.fivetpromart.infrastructure.persistence.customer.CustomerDbo;
import com.fivetpromart.infrastructure.persistence.customer.mapper.CustomerPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.customer.repository.ICustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerAdapter implements ICustomerRepository {

    private final ICustomerJpaRepository customerJpaRepository;
    private final CustomerPersistenceMapper mapper;

    @Override
    public Customer save(Customer customer) {
        CustomerDbo dbo = mapper.toDbo(customer);
        CustomerDbo saved = customerJpaRepository.save(dbo);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        return customerJpaRepository.findByPhoneNumber(phoneNumber).map(mapper::toDomain);
    }

    @Override
    public Optional<Customer> findById(String customerId) {
        return customerJpaRepository.findById(customerId).map(mapper::toDomain);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return customerJpaRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public void delete(Customer customer) {
        CustomerDbo dbo = mapper.toDbo(customer);
        customerJpaRepository.delete(dbo);
    }

}
