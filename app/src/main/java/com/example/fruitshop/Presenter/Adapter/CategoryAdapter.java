package com.example.fruitshop.Presenter.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fruitshop.Domain.Entities.Category;
import com.example.fruitshop.Presenter.Activity.ProductCategoryActivity;
import com.example.fruitshop.databinding.CategoryViewholderBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    ArrayList<Category> categories;
    Context context;
    CategoryViewholderBinding binding;
    public CategoryAdapter(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CategoryViewholderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        context = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.binding.categoryName.setText(category.getName());

        if(category.getImageUrl().contains("/")){
            Glide.with(context).load(category.getImageUrl())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.binding.categoryImage);
        }else{
            int drawableResource = holder.itemView.getResources().getIdentifier(category.getImageUrl().trim(),
                    "drawable",holder.itemView.getContext().getPackageName());
            Glide.with(context).load(drawableResource)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.binding.categoryImage);
        }

        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, ProductCategoryActivity.class);
            intent.putExtra("item",category);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final CategoryViewholderBinding binding;

        public ViewHolder(@NonNull CategoryViewholderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
