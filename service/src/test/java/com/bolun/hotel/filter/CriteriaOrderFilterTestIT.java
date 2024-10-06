package com.bolun.hotel.filter;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.Order_;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.User_;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.filter.paramresolver.OrderFilterParamResolver;
import com.bolun.hotel.util.HibernateTestUtil;
import com.bolun.hotel.util.OrderFilter;
import com.bolun.hotel.util.TestDataImporter;
import com.bolun.hotel.util.TestObjectsUtils;
import com.bolun.integration.IntegrationTestBase;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(OrderFilterParamResolver.class)
class CriteriaOrderFilterTestIT extends IntegrationTestBase {

    @BeforeAll
    static void createSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        TestDataImporter.importData(sessionFactory);
    }

    @Test
    void findAllByStatus(OrderFilter orderFilter) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> order = criteria.from(Order.class);
        QPredicate predicateBuilder = QPredicate.builder()
                .add(orderFilter.getStatus(), status -> cb.equal(order.get(Order_.status), status));
        criteria.select(order).where(predicateBuilder.getPredicates().toArray(new Predicate[0]));

        List<Order> ordersActualResult = session.createQuery(criteria).list();

        assertThat(ordersActualResult).hasSize(2);
        List<OrderStatus> ordersStatuses = ordersActualResult.stream()
                .map(Order::getStatus)
                .toList();
        assertThat(ordersStatuses)
                .isNotEmpty()
                .allMatch(status -> status.equals(OrderStatus.REJECTED));
    }

    @Test
    void findAllByEmail(OrderFilter orderFilter) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> order = criteria.from(Order.class);
        Predicate predicate = QPredicate.builder()
                .add(orderFilter.getUser(), it -> cb.like(order.get(Order_.user).get(User_.email), '%' + it.getEmail() + '%'))
                .buildCriteria();
        criteria.select(order).where(predicate);

        List<Order> actualOrders = session.createQuery(criteria).list();

        assertThat(actualOrders).hasSize(4);
        List<String> emails = actualOrders.stream()
                .map(Order::getUser)
                .map(User::getEmail)
                .toList();
        assertThat(emails)
                .isNotEmpty()
                .allMatch(email -> email.equals(orderFilter.getUser().getEmail()));
    }

    @Test
    void findOrderApartmentUserByOrderId() {
        Order order = TestObjectsUtils.getOrder();
        User user = TestObjectsUtils.getUser("my@mail.ru");
        Apartment apartment = TestObjectsUtils.getApartment();
        order.add(user);
        order.add(apartment);
        session.persist(user);
        session.persist(apartment);
        session.persist(order);
        EntityGraph<Order> entityGraph = session.createEntityGraph(Order.class);
        entityGraph.addAttributeNodes("user");
        entityGraph.addAttributeNodes("apartment");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);

        Order orderActualResult = session.find(Order.class, order.getId(), properties);

        assertNotNull(orderActualResult);
        assertNotNull(orderActualResult.getUser());
        assertNotNull(orderActualResult.getApartment());
    }

    @Test
    void findAllApartmentsFromOrdersByUserEmail(OrderFilter orderFilter) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> order = criteria.from(Order.class);
        EntityGraph<Order> entityGraph = session.createEntityGraph(Order.class);
        entityGraph.addAttributeNodes("apartment");
        Predicate predicate = QPredicate.builder()
                .addNested(
                        orderFilter.getUser(),
                        User::getEmail,
                        email -> cb.equal(order.get(Order_.user).get(User_.email), email)
                ).buildCriteria();
        criteria.select(order).where(predicate);

        List<Order> actualOrders = session.createQuery(criteria)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .list();

        List<Apartment> apartments = actualOrders.stream()
                .map(Order::getApartment)
                .distinct()
                .toList();
        assertThat(apartments).hasSize(2);
    }

    @Test
    void findAllApartmentsFromOrdersByUserFirstName(OrderFilter orderFilter) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> order = criteria.from(Order.class);
        EntityGraph<Order> entityGraph = session.createEntityGraph(Order.class);
        entityGraph.addAttributeNodes("apartment");
        Predicate predicate = QPredicate.builder()
                .addNested(
                        orderFilter.getUser(),
                        User::getFirstName,
                        firstname -> cb.equal(order.get(Order_.user).get(User_.firstName), firstname)
                )
                .buildCriteria();
        criteria.select(order).where(predicate);

        List<Order> actualOrders = session.createQuery(criteria)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .list();

        List<Apartment> apartments = actualOrders.stream()
                .map(Order::getApartment)
                .distinct()
                .toList();
        assertThat(apartments).hasSize(4);
    }
}

