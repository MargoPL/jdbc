package pl.sda.jdbcjpa.person;

import pl.sda.jdbcjpa.JpaMain;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomerDao {

    private EntityManager entityManager =
            JpaMain.ENTITY_MANAGER_FACTORY.createEntityManager();

    public List<Customer> findByStreet(String street){
        TypedQuery<Customer> query = entityManager.createQuery(
                "select c from Customer c where c.street = :street",
                Customer.class
        );
        query.setParameter("street", street);
        return query.getResultList();
    }

    public List<Customer> findCustomersWithOrders() {
        return entityManager.createQuery(
                "select c from Customer c join Order o " +
                        "on o.customer.id = c.id",
                Customer.class
        ).getResultList();
    }

    public List<Customer> findCustomersByCity(String city) {
        return entityManager.createNamedQuery("findCustomersByCityLike")
                .setParameter("city", city).getResultList();
    }
}
