package com.example.fruitshop.Presenter.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.fruitshop.Application.ViewModel.CategoryViewModel;
import com.example.fruitshop.Domain.Entities.Category;
import com.example.fruitshop.Presenter.Custom.MyModal;
import com.example.fruitshop.Presenter.Custom.MyToast;
import com.example.fruitshop.R;
import com.example.fruitshop.databinding.ActivityUpdateCategoryBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UpdateCategoryActivity extends ChooseImageActivity {

    ActivityUpdateCategoryBinding binding;
    CategoryViewModel categoryViewModel;
    Category category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUpdateCategoryBinding.inflate(getLayoutInflater());
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        setContentView(binding.getRoot());
        loadCategory();
        binding.btnBack.setOnClickListener(v->finish());

        binding.btnUpload.setOnClickListener(v->openImagePicker());

        binding.btnUpdate.setOnClickListener(v->{
            if(binding.edtName.getText().toString().equals("")){
                MyToast.showError(this,"Tên không được để trống");
                return;
            }
            category.setName(binding.edtName.getText().toString());
            category.setImageUrl(currentImageUrl.toString());
            categoryViewModel.updateCategory(category).thenAccept(x->{
                runOnUiThread(()->{
                    MyModal.showSuccess(UpdateCategoryActivity.this,"Cập nhật danh mục thành công",()->finish());
                });
            });
        });
    }

    private void loadCategory(){
        category = (Category) getIntent().getSerializableExtra("item");
        currentImageUrl = Uri.parse(category.getImageUrl());
        binding.edtName.setText(category.getName());

        if(category.getImageUrl().contains("/")){
            Glide.with(this).load(category.getImageUrl())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.imgCategory);
        }else{
            int drawableResource = getResources().getIdentifier(category.getImageUrl().trim(),
                    "drawable",getPackageName());
            Glide.with(this).load(drawableResource)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.imgCategory);
        }
    }


    @Override
    protected void loadImage() {
        Glide.with(this).load(currentImageUrl).override(200,200).apply(RequestOptions.circleCropTransform()).into(binding.imgCategory);
    }
}