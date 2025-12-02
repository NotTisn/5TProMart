package com.fivetpromart.infrastructure.persistence.customer.adapter;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.query.CustomerSearchQuery;
import com.fivetpromart.application.port.out.ICustomerRepository;
import com.fivetpromart.application.port.out.ISignUpRequestRepository;
import com.fivetpromart.domain.model.Customer;
import com.fivetpromart.infrastructure.persistence.customer.CustomerDbo;
import com.fivetpromart.infrastructure.persistence.customer.mapper.CustomerPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.customer.repository.ICustomerJpaRepository;
import com.fivetpromart.infrastructure.persistence.customer.spec.CustomerSpecification;
import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    public List<Customer> findAll() {
        List<CustomerDbo> dbos = customerJpaRepository.findAll();
        return dbos.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Page<Customer> searchCustomers(CustomerSearchQuery query, Pageable pageable) {
        Specification<CustomerDbo> spec = CustomerSpecification.getCustomerSpecification(query);

        Page<CustomerDbo> dboPage = customerJpaRepository.findAll(spec, pageable);

        return dboPage.map(mapper::toDomain);
    }

}
