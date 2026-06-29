package com.example.fruitshop.Presenter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fruitshop.Application.Model.ProductCart;
import com.example.fruitshop.Application.ViewModel.OrderViewModel;
import com.example.fruitshop.Domain.Entities.DetailOrder;
import com.example.fruitshop.Domain.Entities.Order;
import com.example.fruitshop.Domain.Entities.User;
import com.example.fruitshop.Infrastructure.Data.CartHelper;
import com.example.fruitshop.Infrastructure.Data.UserHelper;
import com.example.fruitshop.Presenter.Adapter.CartAdapter;
import com.example.fruitshop.Presenter.Custom.MyModal;
import com.example.fruitshop.Presenter.Custom.MyToast;
import com.example.fruitshop.Presenter.Event.OnQuantityChangeListener;
import com.example.fruitshop.R;
import com.example.fruitshop.databinding.ActivityCartBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements OnQuantityChangeListener {
    ActivityCartBinding binding;
    CartHelper cartHelper;
    UserHelper userHelper;
    boolean codMethod = true;
    OrderViewModel orderViewModel;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        cartHelper = new CartHelper(this);
        userHelper = new UserHelper(this);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v-> finish());

        ArrayList<ProductCart> productCarts = cartHelper.getProductsInCart();
        if(productCarts.isEmpty()){
            binding.container.setVisibility(View.INVISIBLE);
            binding.btnPurchase.setVisibility(View.INVISIBLE);
            binding.txtEmpty.setVisibility(View.VISIBLE);
            return;
        }

        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.cartRecyclerView.setAdapter(new CartAdapter(this,productCarts));
        loadTotalPrice();

        binding.btnCOD.setOnClickListener(v->{
            if(codMethod) return;
            codMethod = true;
            binding.btnCOD.setBackground(getResources().getDrawable(R.drawable.bg_primary_selected,null));
            binding.btnTransfer.setBackground(getResources().getDrawable(R.drawable.bg_secondary_selected,null));
        });

        binding.btnTransfer.setOnClickListener(v->{
            if(!codMethod) return;
            codMethod = false;
            binding.btnTransfer.setBackground(getResources().getDrawable(R.drawable.bg_primary_selected,null));
            binding.btnCOD.setBackground(getResources().getDrawable(R.drawable.bg_secondary_selected,null));
        });

        binding.btnPurchase.setOnClickListener(v->{
            User currentUser = userHelper.getUserSigned();
            if(currentUser == null){
                MyModal.showLoginDialog(this,()-> startActivity(new Intent(v.getContext(), SignInActivity.class)));
                return;
            }
            Long totalPrice = Long.parseLong(binding.txtTotalPrice.getText().toString().replace("đ",""));
            Order order = new Order(currentUser.getId(),sdf.format(new Date()),"Chưa thanh toán",codMethod ? "COD": "TRANSFER",totalPrice);
            List<DetailOrder> detailOrders = new ArrayList<>();
            productCarts.forEach(productCart -> {
                DetailOrder detailOrder = new DetailOrder(productCart.getId(),productCart.getQuantity(),productCart.getPrice());
                detailOrders.add(detailOrder);
            });

            orderViewModel.createOrder(order,detailOrders).thenAccept(x->{
                cartHelper.clear();
                runOnUiThread(()->{
                    MyModal.showSuccess(CartActivity.this,"Đặt hàng thành công",()-> finish());
                });
            }).exceptionally(ex -> {
                System.out.println("Ngoại lệ: " + ex.getMessage());
                return null;
            });
        });
    }
    private void loadTotalPrice(){
        ArrayList<ProductCart> productCarts = cartHelper.getProductsInCart();
        long totalPrice = 0;
        for (ProductCart product : productCarts) {
            totalPrice += (long)( product.getPrice() * product.getQuantity());
        }
        binding.txtTotalPrice.setText(totalPrice + "đ");
    }

    @Override
    public void onQuantityChanged(ProductCart productCart) {
        loadTotalPrice();
    }
}