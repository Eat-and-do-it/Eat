package com.project.eat.order.orderItemOption;

import com.project.eat.domain.order.OrderItemOption;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderItemOptionRepository {

    private final EntityManager em;

    public void save(OrderItemOption orderItemOption) {
        em.persist(orderItemOption);
    }
}
