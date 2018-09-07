package pl.sda.jdbcjpa.product;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "E")
public class Ebook extends Product{

}
