package com.electronic.store.ElectronicStore_webapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @Column(name = "id")
    private String categoryId;
    @Column(name = "category_title", length = 50, nullable = false)
    private String title;
    @Column(name = "category_description", length = 500)
    private String description;
    private String coverImage;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "category")
    private List<Product> products = new ArrayList<>();


}
