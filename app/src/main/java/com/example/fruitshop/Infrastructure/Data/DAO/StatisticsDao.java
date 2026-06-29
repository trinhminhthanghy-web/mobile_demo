package com.example.fruitshop.Infrastructure.Data.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.fruitshop.Application.Model.RevenueByDate;
import com.example.fruitshop.Application.Model.TopSellingProduct;

import java.math.BigDecimal;
import java.util.List;

@Dao
public interface StatisticsDao {
    @Query("SELECT orderDate AS date, SUM(totalPrice) AS revenue " +
            "FROM orders " +
            "WHERE orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY orderDate " +
            "ORDER BY orderDate ASC")
    LiveData<List<RevenueByDate>> getRevenueByDate(String startDate, String endDate);
    @Query("SELECT SUM(totalPrice) FROM orders " +
            "WHERE orderDate BETWEEN :startDate AND :endDate")
    LiveData<Double> getTotalRevenue(String startDate, String endDate);
    @Query("SELECT COUNT(*) FROM orders " +
            "WHERE orderDate BETWEEN :startDate AND :endDate")
    LiveData<Integer> getOrderCount(String startDate, String endDate);

    @Query("SELECT products.name AS productName, products.imageUrl AS productImageUrl, products.unit AS productUnit , SUM(detail_orders.quantity) AS totalSold " +
            "FROM products " +
            "INNER JOIN detail_orders ON products.id = detail_orders.productId " +
            "GROUP BY products.id " +
            "ORDER BY totalSold DESC " +
            "LIMIT 5")
    LiveData<List<TopSellingProduct>> getTopSellingProducts();
}
