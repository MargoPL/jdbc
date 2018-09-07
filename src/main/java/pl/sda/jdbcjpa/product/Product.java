package pl.sda.jdbcjpa.product;

import lombok.Getter;
import lombok.Setter;
import pl.sda.jdbcjpa.BaseEntity;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "P")
@DiscriminatorColumn(name = "discr",discriminatorType = DiscriminatorType.STRING)
public abstract class Product extends BaseEntity{

    private String name;
}
