package org.example;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

    private String cep;

    private String state;

    private String city;

    private String neighborhood;

    private String street;

    private String service;

    private Location location;
}
