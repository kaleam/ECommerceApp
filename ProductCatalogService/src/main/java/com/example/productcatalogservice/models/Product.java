package com.example.productcatalogservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

@Getter
@Setter
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product extends BaseModel{
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;
}