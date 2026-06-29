package com.example.fruitshop.Infrastructure.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.fruitshop.Domain.Entities.Category;
import com.example.fruitshop.Domain.Entities.DetailOrder;
import com.example.fruitshop.Domain.Entities.Favorite;
import com.example.fruitshop.Domain.Entities.Order;
import com.example.fruitshop.Domain.Entities.Product;
import com.example.fruitshop.Domain.Entities.User;
import com.example.fruitshop.Infrastructure.Data.DAO.CategoryDao;
import com.example.fruitshop.Infrastructure.Data.DAO.FavoriteDao;
import com.example.fruitshop.Infrastructure.Data.DAO.OrderDao;
import com.example.fruitshop.Infrastructure.Data.DAO.ProductDao;
import com.example.fruitshop.Infrastructure.Data.DAO.StatisticsDao;
import com.example.fruitshop.Infrastructure.Data.DAO.UserDao;
import com.example.fruitshop.Infrastructure.Tool.HashUtils;

import java.util.ArrayList;

@Database(
        entities = {User.class, Category.class, Product.class, Order.class, Favorite.class, DetailOrder.class},
        version = 5,
        exportSchema = false
)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract CategoryDao categoryDao();
    public abstract UserDao userDao();
    public abstract FavoriteDao favoriteDao();
    public abstract OrderDao orderDao();
    public abstract StatisticsDao statisticsDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app.db")
                            .addCallback(seedDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback seedDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@androidx.annotation.NonNull androidx.sqlite.db.SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Seed dữ liệu mặc định
            new Thread(() -> {
                AppDatabase database = INSTANCE;
                if (database != null) {
                    ArrayList<Category> categories = new ArrayList<>();
                    categories.add(new Category(1,"Rau củ","cat1"));
                    categories.add(new Category(2,"Hoa quả","cat2"));
                    categories.add(new Category(3,"Sữa","cat3"));
                    categories.add(new Category(4,"Đồ uống","cat4"));
                    categories.add(new Category(5,"Đồ ăn","cat5"));
                    database.categoryDao().addRange(categories);

                    ArrayList<Product> products = new ArrayList<>();
                    products.add(new Product(1,"Cam",2,10000,"orange","kg", "Cam"));
                    products.add(new Product(2,"Dứa",2,10000,"pineapple","kg", "Dứa"));
                    products.add(new Product(3,"Dưa hấu",2,10000,"watermelon","kg", "Dưa hấu"));
                    products.add(new Product(4,"Dâu tây",2,10000,"strawberry","kg", "Dâu tây"));
                    database.productDao().addRange(products);


                    ArrayList<User> users = new ArrayList<>();
                    String password = HashUtils.sha256("123456");
                    users.add(new User("Khang","khang@gmail.com",password,null));
                    database.userDao().addRange(users);
                }
            }).start();
        }
    };

}
