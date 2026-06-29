package com.example.fruitshop.Infrastructure.Data.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fruitshop.Domain.Entities.Product;
import com.example.fruitshop.Domain.Relations.ProductWithCategory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void add(Product product);
    @Insert
    void addRange(List<Product> products);
    @Update
    void update(Product product);
    @Delete
    void delete(Product product);
    @Query("SELECT COUNT(*) FROM products")
    int count();
    @Query("SELECT * FROM products WHERE id = :id")

    LiveData<Product> getById(int id);
    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAll();
    @Query("SELECT * FROM products WHERE name LIKE '%' || :name || '%'")
    LiveData<List<Product>> getByName(String name);
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE productId = :productId AND userId = :userId)")
    LiveData<Boolean> getFavoriteState(int productId, long userId);
    @Query("SELECT * FROM products INNER JOIN favorites ON products.id = favorites.productId WHERE favorites.userId = :userId")
    LiveData<List<Product>> getFavoriteProducts(long userId);
    @Query("SELECT products.* FROM products INNER JOIN categories ON products.categoryId = categories.id WHERE categories.id = :categoryId")
    LiveData<List<Product>> getProductsByCategory(int categoryId);
}
