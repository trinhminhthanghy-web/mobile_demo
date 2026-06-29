package com.example.fruitshop.Presenter.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.fruitshop.Application.ViewModel.UserViewModel;
import com.example.fruitshop.Domain.Entities.User;
import com.example.fruitshop.Infrastructure.Data.UserHelper;
import com.example.fruitshop.Presenter.Custom.MyToast;
import com.example.fruitshop.R;
import com.example.fruitshop.databinding.ActivityUpdateProfileBinding;

public class UpdateProfileActivity extends ChooseImageActivity {
    ActivityUpdateProfileBinding binding;
    UserViewModel userViewModel;
    UserHelper userHelper;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userHelper = new UserHelper(this);
        user = userHelper.getUserSigned();
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v->finish());
        loadUser();

        binding.btnUpload.setOnClickListener(v->openImagePicker());

        binding.btnUpdate.setOnClickListener(v->{
            if(binding.edtName.getText().toString().equals("")){
                MyToast.showError(this,"Tên không được để trống");
                return;
            }
            user.setName(binding.edtName.getText().toString());
            user.setGender(binding.edtGender.getText().toString());
            user.setBirthDay(binding.edtBirthDay.getText().toString());
            user.setAddress(binding.edtAddress.getText().toString());
            user.setImageUrl(currentImageUrl.toString());
            userViewModel.updateUser(user).thenAccept(x->{
                userHelper.removeUser();
                userHelper.saveUser(user);
               runOnUiThread(()->{
                   MyToast.showSuccess(UpdateProfileActivity.this,"Cập nhật thông tin thành công");
                   finish();
               });
            });
        });
    }

    private void loadUser() {
        binding.edtName.setText(user.getName());
        binding.edtAddress.setText(user.getAddress());
        binding.edtBirthDay.setText(user.getBirthDay());
        binding.edtGender.setText(user.getGender());
        if(user.getImageUrl() != null){
            if(user.getImageUrl().contains("/")){
                Glide.with(this).load(user.getImageUrl())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.image);
            }else{
                int drawableResource = getResources().getIdentifier(user.getImageUrl().trim(),
                        "drawable",getPackageName());
                Glide.with(this).load(drawableResource)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.image);
            }
        }

    }

    @Override
    protected void loadImage() {
        Glide.with(this).load(currentImageUrl).override(200,200).apply(RequestOptions.circleCropTransform()).into(binding.image);
    }
}