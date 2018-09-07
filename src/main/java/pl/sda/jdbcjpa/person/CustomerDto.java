package pl.sda.jdbcjpa.person;

import com.querydsl.core.annotations.QueryProjection;

public class CustomerDto {
    private Integer id;
    private String city;

    @QueryProjection
    public CustomerDto(Integer id, String city) {
        this.id = id;
        this.city = city;
    }
}
