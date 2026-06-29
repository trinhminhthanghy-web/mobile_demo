package com.example.fruitshop.Presenter.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fruitshop.Domain.Entities.User;
import com.example.fruitshop.Infrastructure.Data.UserHelper;
import com.example.fruitshop.Presenter.Activity.AdminActivity;
import com.example.fruitshop.Presenter.Activity.SignInActivity;
import com.example.fruitshop.Presenter.Activity.UpdateProfileActivity;
import com.example.fruitshop.R;
import com.example.fruitshop.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    UserHelper userHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater);
        userHelper = new UserHelper(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAdmin.setOnClickListener(v->{
            startActivity(new Intent(requireContext(), AdminActivity.class));
        });

        binding.btnUpdateInfo.setOnClickListener(v->{
            startActivity(new Intent(requireContext(), UpdateProfileActivity.class));
        });

        binding.btnLogOut.setOnClickListener(v->{
            userHelper.removeUser();
            Intent intent = new Intent(v.getContext(), SignInActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}