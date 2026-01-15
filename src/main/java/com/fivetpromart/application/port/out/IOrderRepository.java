package com.fivetpromart.application.port.out;

//import com.fivetpromart.application.dto.query.OrderSearchQuery;
import com.fivetpromart.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface IOrderRepository {
    
    Order save(Order order);
    
    Optional<Order> findById(String orderId);

    boolean existsById(String orderId);

    void deleteById(String orderId);

    //Page<Order> searchOrders(OrderSearchQuery query, Pageable pageable);
}
