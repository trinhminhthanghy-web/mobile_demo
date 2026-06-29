package com.example.fruitshop.Application.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fruitshop.Domain.Entities.Category;
import com.example.fruitshop.Domain.Entities.Favorite;
import com.example.fruitshop.Domain.Entities.Product;
import com.example.fruitshop.Infrastructure.Data.AppDatabase;
import com.example.fruitshop.Infrastructure.Data.DAO.FavoriteDao;
import com.example.fruitshop.Infrastructure.Data.DAO.ProductDao;
import com.example.fruitshop.Infrastructure.Data.UserHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProductViewModel extends AndroidViewModel {
    ProductDao productDao;
    FavoriteDao favoriteDao;
    UserHelper userHelper;
    public ProductViewModel(@NonNull Application application) {
        super(application);
        productDao = AppDatabase.getDatabase(application).productDao();
        favoriteDao = AppDatabase.getDatabase(application).favoriteDao();
        userHelper = new UserHelper(application);
    }

    public CompletableFuture<Void> addProduct(Product product){
        return CompletableFuture.runAsync(()->{
            productDao.add(product);
        });
    }

    public CompletableFuture<Void> updateProduct(Product product){
        return CompletableFuture.runAsync(()->{
            productDao.update(product);
        });
    }

    public CompletableFuture<Void> deleteProduct(Product product){
        return CompletableFuture.runAsync(()->{
            productDao.delete(product);
        });
    }

    public LiveData<Boolean> getFavoriteState(int productId){
        return productDao.getFavoriteState(productId,userHelper.getUserSigned().getId());
    }
    public CompletableFuture<Void> favoriteProduct(int productId){
        Favorite favorite = new Favorite();
        favorite.setProductId(productId);
        favorite.setUserId(userHelper.getUserSigned().getId());
        return CompletableFuture.runAsync(()->{
            favoriteDao.add(favorite);
        });
    }

    public CompletableFuture<Void> unFavoriteProduct(int productId){
        Favorite favorite = new Favorite();
        favorite.setProductId(productId);
        favorite.setUserId(userHelper.getUserSigned().getId());
        return CompletableFuture.runAsync(()->{
            favoriteDao.delete(favorite);
        });
    }
    public LiveData<Product> getProductById(int id){
        return productDao.getById(id);
    }

    public LiveData<List<Product>> getAllFavoritedProducts(long userId){
        return productDao.getFavoriteProducts(userId);
    }

    public LiveData<List<Product>> getAll(){
       return productDao.getAll();
    }

    public LiveData<List<Product>> getByName(String name){
        return productDao.getByName(name);
    }
    public LiveData<List<Product>> getProductsByCategory(int categoryId){
        return productDao.getProductsByCategory(categoryId);
    }


}
