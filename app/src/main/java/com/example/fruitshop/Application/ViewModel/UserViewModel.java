package com.example.fruitshop.Application.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fruitshop.Application.Model.Result;
import com.example.fruitshop.Domain.Entities.User;
import com.example.fruitshop.Infrastructure.Data.AppDatabase;
import com.example.fruitshop.Infrastructure.Data.DAO.UserDao;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserViewModel extends AndroidViewModel {
    private UserDao userDao;

    public UserViewModel(Application application) {
        super(application);
        userDao = AppDatabase.getDatabase(application).userDao();
    }

    public CompletableFuture<Long> addUser(User user) {
        return CompletableFuture.supplyAsync(() -> userDao.add(user));
    }
    public CompletableFuture<Void> updateUser(User user){
        return CompletableFuture.runAsync(()-> {
            userDao.update(user);
        });
    }
    public LiveData<User> getUserById(long id){ return  userDao.getById(id);}
    public LiveData<User> getUserByEmail(String email){
        return userDao.getByEmail(email);
    }

}
