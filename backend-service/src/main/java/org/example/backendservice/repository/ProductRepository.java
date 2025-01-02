package org.example.backendservice.repository;

import org.example.backendservice.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    Iterable<Product> findAllByTitleLikeIgnoreCase(String filter);

    @Query("from Product p where p.owner = :userName")
    Iterable<Product> findProductsByOwner(@Param("userName") String userName);

    @Query("from Product p where p.owner = :userName and lower(p.title) like lower(concat('%', :title, '%'))")
    Iterable<Product> findProductsByOwnerAndTitle(@Param("userName") String userName, @Param("title") String title);
}
