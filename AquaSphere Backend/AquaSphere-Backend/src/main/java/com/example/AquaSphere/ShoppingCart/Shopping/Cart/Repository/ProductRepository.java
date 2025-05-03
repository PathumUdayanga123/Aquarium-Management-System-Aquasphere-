package com.example.aquaSphere.ShoppingCart.Shopping.Cart.Repository;

import com.example.aquaSphere.ShoppingCart.Shopping.Cart.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    List<Product> findAllById(List<Long> Ids);
//    List<Product> findAllByIdIn(List<Long> ids);
}
