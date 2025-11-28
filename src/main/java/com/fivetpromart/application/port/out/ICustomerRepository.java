package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.Customer;

import java.util.Optional;

public interface ICustomerRepository {
    Customer save(Customer customer);

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    //Customer findByPhoneNumer(String );
}
