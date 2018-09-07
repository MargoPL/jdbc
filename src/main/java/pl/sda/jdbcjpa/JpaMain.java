package pl.sda.jdbcjpa;

import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQuery;
import pl.sda.jdbcjpa.order.Order;
import pl.sda.jdbcjpa.order.OrderStatus;
import pl.sda.jdbcjpa.order.QOrder;
import pl.sda.jdbcjpa.person.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class JpaMain {

    public final static EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("sdajpaPU");

    private static CustomerDao customerDao = new CustomerDao();

    public static void main(String[] args) {
//        createCustomer();
//        findCustomerJPQL("Nowak");
//        findCustomerEM(1);

        createCustomerWithOrder();
        findCustomersWithOrders();
        findCustomersByCityLike();
        findCustomersWithOrdersUsingQueryDSL();
    }

    private static void findCustomersWithOrdersUsingQueryDSL() {
        QCustomer customer = QCustomer.customer;
        QOrder order = QOrder.order;
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        List<CustomerDto> fetch = new JPAQuery<>(em)
                .select(new QCustomerDto(customer.id,customer.city))
                .from(customer)
                .join(order).fetchAll()
                .on(customer.id.eq(order.customer.id))
                .fetch();
        System.out.println();
//        for (Tuple tuple : fetch) {
//            System.out.println(tuple.get(0, Integer.class) + " " + tuple.get(1, String.class));
//        }
    }

    private static void findCustomersByCityLike() {
        List<Customer> jelCustomers = customerDao.findCustomersByCity("Jel%");
        System.out.println(jelCustomers);
    }

    private static void findCustomersWithOrders() {
        List<Customer> customersWithOrders = customerDao.findCustomersWithOrders();
        for (Customer customersWithOrder : customersWithOrders) { //LAZY powoduje dociąganie danych
            System.out.println(customersWithOrder.getOrdersList().size());
        }
    }

    private static void createCustomerWithOrder() {
        Customer customer = new Customer();
        customer.setFirstName("Karol");
        customer.setSurname("Kowalski");
        customer.setStreet("Jagodowa");
        customer.setCity("Jelenia Góra");
        customer.setPostalCode("88-888");
        customer.setThisFieldIsNotToPersist("BUM");

        Order order = new Order();
        order.setCreateDate(new Date());
        order.setOrderStatus(OrderStatus.NEW);
        order.setTotalCost(BigDecimal.valueOf(1.9));
        order.setCustomerName("Karol K");

        customer.setOrdersList(Lists.newArrayList(order));
        order.setCustomer(customer);

        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(customer);
//        entityManager.persist(order);
        entityManager.getTransaction().commit();
        List<Order> ordersList = customer.getOrdersList();
        System.out.println();
    }

    private static void findCustomerEM(Integer id) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        Customer customer = entityManager.find(Customer.class, id);
        System.out.println(customer);
    }

    private static void findCustomerJPQL(String surname) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        TypedQuery<Customer> query = entityManager.createQuery(
                "select c from Customer c where c.surname = :surname",
                Customer.class);
//        query.setParameter(0, surname); // można używać z "?"
        query.setParameter("surname", surname);
        List<Customer> resultList = query.getResultList();
        System.out.println(resultList);
    }

    private static void createCustomer() {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Customer customer = new Customer();
        customer.setFirstName("Andrzej");
        customer.setSurname("Nowak");
        customer.setCity("Zielona Góra");
        customer.setPostalCode("65-001");
        customer.setStreet("Akacjowa");
        transaction.begin();
        entityManager.persist(customer);
        transaction.commit();
    }
}
