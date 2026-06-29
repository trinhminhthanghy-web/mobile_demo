package com.example.fruitshop.Presenter.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fruitshop.Application.ViewModel.CategoryViewModel;
import com.example.fruitshop.Domain.Entities.Category;
import com.example.fruitshop.Presenter.Custom.MyModal;
import com.example.fruitshop.Presenter.Custom.MyToast;
import com.example.fruitshop.R;
import com.example.fruitshop.databinding.ActivityCreateCategoryBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CreateCategoryActivity extends ChooseImageActivity {
    CategoryViewModel categoryViewModel;

    ActivityCreateCategoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateCategoryBinding.inflate(getLayoutInflater());
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v->finish());

        binding.btnUpload.setOnClickListener(v->openImagePicker());

        binding.btnCreate.setOnClickListener(v->{
            if(binding.edtName.getText().toString().equals("")){
                MyToast.showError(this,"Tên không được để trống");
                return;
            }
            Category category = new Category();
            category.setName(binding.edtName.getText().toString());
            String imageUrl = saveImageToInternalStorage(currentImageUrl);
            category.setImageUrl(imageUrl);
            categoryViewModel.addCategory(category).thenAccept(x->{
                runOnUiThread(()->{
                    MyModal.showSuccess(CreateCategoryActivity.this,"Tạo danh mục thành công",()->finish());
                });
            });
        });
    }

    @Override
    protected void loadImage() {
        Glide.with(this).load(currentImageUrl).override(200,200).apply(RequestOptions.circleCropTransform()).into(binding.imgCategory);
    }
}