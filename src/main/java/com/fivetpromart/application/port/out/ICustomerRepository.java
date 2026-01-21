package com.fivetpromart.application.port.out;

import com.fivetpromart.application.dto.query.CustomerSearchQuery;
import com.fivetpromart.domain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    Optional<Customer> findById(String userId);
    Optional<Customer> findByIdIncludingDeleted(String userId);
    boolean existsByPhoneNumber(String phoneNumber);
    void delete(Customer customer);

    List<Customer> findAll();
    Page<Customer> searchCustomers(CustomerSearchQuery query, Pageable pageable);
    //Customer findByPhoneNumer(String );
}
