package com.example.fruitshop.Presenter.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.fruitshop.Application.ViewModel.ProductViewModel;
import com.example.fruitshop.Domain.Entities.Category;
import com.example.fruitshop.Domain.Entities.Product;
import com.example.fruitshop.Presenter.Adapter.ProductAdapter;
import com.example.fruitshop.R;
import com.example.fruitshop.databinding.ActivityProductCategoryBinding;

import java.util.ArrayList;

public class ProductCategoryActivity extends AppCompatActivity {

    ActivityProductCategoryBinding binding;
    Category category;
    ProductViewModel productViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProductCategoryBinding.inflate(getLayoutInflater());
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v->finish());
        category = (Category) getIntent().getSerializableExtra("item");
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
        productViewModel.getProductsByCategory(category.getId()).observe(this,products -> {
            binding.recyclerView.setAdapter(new ProductAdapter((ArrayList<Product>) products,productViewModel,this));
            binding.progressBar.setVisibility(View.INVISIBLE);
        });

        binding.txtTitle.setText(category.getName());
    }
}