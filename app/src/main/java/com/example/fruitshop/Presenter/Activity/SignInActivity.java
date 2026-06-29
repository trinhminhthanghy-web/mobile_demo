package com.example.fruitshop.Presenter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.fruitshop.Application.ViewModel.UserViewModel;
import com.example.fruitshop.Domain.Entities.User;
import com.example.fruitshop.Infrastructure.Data.UserHelper;
import com.example.fruitshop.Infrastructure.Tool.Extension;
import com.example.fruitshop.Infrastructure.Tool.HashUtils;
import com.example.fruitshop.Presenter.Custom.MyToast;
import com.example.fruitshop.R;
import com.example.fruitshop.databinding.ActivitySignInBinding;

import org.mindrot.jbcrypt.BCrypt;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    private UserViewModel userViewModel;
    UserHelper userHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userHelper = new UserHelper(this);
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v->signIn());

        binding.btnRegister.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), SignUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        });
    }

    private void signIn(){
        String email = binding.edtEmail.getText().toString();
        String password = binding.edtPassword.getText().toString();
        binding.btnLogin.setClickable(false);
        binding.btnLogin.setBackgroundResource(R.drawable.btn_primary_disable);
        Extension.observeOnce(userViewModel.getUserByEmail(email),this,user -> {
            if(user != null && user.getPasswordHash().equals(HashUtils.sha256(password))){
                userHelper.saveUser(user);
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }else{
                MyToast.showError(this,"Tài khoản hoặc mật khẩu không hợp lệ");
                binding.btnLogin.setClickable(true);
                binding.btnLogin.setBackgroundResource(R.drawable.btn_primary);
            }
        });
    }
}