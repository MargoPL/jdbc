package pl.sda.jdbcjpa.person;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Customer {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String firstName;

    private String surname;

    private String city;

    private String street;

    private String postalCode;

    @Transient
    private String thisFieldIsNotToPersist;
}
