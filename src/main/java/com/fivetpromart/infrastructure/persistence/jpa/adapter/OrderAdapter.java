package com.fivetpromart.infrastructure.persistence.jpa.adapter;

//import com.fivetpromart.application.dto.query.OrderSearchQuery;
import com.fivetpromart.application.dto.query.OrderSearchQuery;
import com.fivetpromart.application.port.out.IOrderRepository;
import com.fivetpromart.domain.model.Order;
import com.fivetpromart.infrastructure.persistence.jpa.entity.OrderDbo;
import com.fivetpromart.infrastructure.persistence.jpa.mapper.OrderPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.jpa.repository.IOrderJpaRepository;
//import com.fivetpromart.infrastructure.persistence.jpa.specification.OrderSpecification;
import com.fivetpromart.infrastructure.persistence.jpa.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderAdapter implements IOrderRepository {

    private final IOrderJpaRepository jpaRepository;
    private final OrderPersistenceMapper mapper;

    @Override
    public Order save(Order order) {
        OrderDbo dbo = mapper.toDbo(order);
        OrderDbo savedDbo = jpaRepository.save(dbo);
        return mapper.toDomain(savedDbo);
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return jpaRepository.findById(orderId)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Order> searchOrders(OrderSearchQuery query, Pageable pageable) {
        Specification<OrderDbo> spec = OrderSpecification.getSpecification(query);
        Page<OrderDbo> dboPage = jpaRepository.findAll(spec, pageable);
        return dboPage.map(mapper::toDomain);
    }

    @Override
    public boolean existsById(String orderId) {
        return jpaRepository.existsById(orderId);
    }

    @Override
    public void deleteById(String orderId) {
        jpaRepository.deleteById(orderId);
    }
}
