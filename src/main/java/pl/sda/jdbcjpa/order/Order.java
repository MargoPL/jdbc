package pl.sda.jdbcjpa.order;

import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sda.jdbcjpa.BaseEntity;
import pl.sda.jdbcjpa.person.Customer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Setter
@Table(name = "Orders")
@NoArgsConstructor
public class Order extends BaseEntity {

    private String customerName;

    private BigDecimal totalCost;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Enumerated
    private OrderStatus orderStatus;

    @ManyToOne
    private Customer customer;

}
