package com.example.demo.repository;

import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    void deleteByOrder(Orders order);

//    @Query("SELECT new com.example.demo.dto.FoodStatisticDTO(f.id_food, f.name, f.price, f.category, f.image_url, " +
//            "SUM(od.quantity), SUM(od.quantity * f.price)) " +
//            "FROM OrderDetail od JOIN od.food f " +
//            "GROUP BY f.id_food, f.name, f.price, f.category, f.image_url " +
//            "ORDER BY SUM(od.quantity) DESC")
//    List<FoodStatisticDTO> findTopOrderedFoods(Pageable pageable);
//
//    @Query("SELECT new com.example.demo.dto.FoodStatisticDTO(f.id_food, f.name, f.price, f.category, f.image_url, " +
//            "SUM(od.quantity), SUM(od.quantity * f.price)) " +
//            "FROM OrderDetail od JOIN od.food f " +
//            "GROUP BY f.id_food, f.name, f.price, f.category, f.image_url " +
//            "ORDER BY SUM(od.quantity * f.price) DESC")
//    List<FoodStatisticDTO> findFoodsByRevenueDesc(Pageable pageable);
//
//    @Query("SELECT new com.example.demo.dto.FoodStatisticDTO(f.id_food, f.name, f.price, f.category, f.image_url, " +
//            "SUM(od.quantity), SUM(od.quantity * f.price)) " +
//            "FROM OrderDetail od JOIN od.food f " +
//            "GROUP BY f.id_food, f.name, f.price, f.category, f.image_url " +
//            "ORDER BY SUM(od.quantity * f.price) ASC")
//    List<FoodStatisticDTO> findFoodsByRevenueAsc(Pageable pageable);
//
//    @Query("SELECT fi.ingredient.name, SUM(fi.quantity * od.quantity) " +
//            "FROM OrderDetail od " +
//            "JOIN od.food f " +
//            "JOIN f.foodIngredients fi " +
//            "GROUP BY fi.ingredient.name")
//    Map<String, Integer> calculateTotalUsedIngredients();
}
