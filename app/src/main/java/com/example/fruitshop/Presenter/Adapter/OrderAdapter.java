package com.example.fruitshop.Presenter.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fruitshop.Application.ViewModel.OrderViewModel;
import com.example.fruitshop.Application.ViewModel.ProductViewModel;
import com.example.fruitshop.Domain.Entities.DetailOrder;
import com.example.fruitshop.Domain.Entities.Order;
import com.example.fruitshop.Infrastructure.Tool.Extension;
import com.example.fruitshop.databinding.ProductOrderedViewholderBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    ProductOrderedViewholderBinding binding;
    Context context;
    ArrayList<DetailOrder> detailOrders;
    OrderViewModel orderViewModel;
    ProductViewModel productViewModel;
    LifecycleOwner lifecycleOwner;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public OrderAdapter(ArrayList<DetailOrder> detailOrders, OrderViewModel orderViewModel,ProductViewModel productViewModel,LifecycleOwner lifecycleOwner) {
        this.detailOrders = detailOrders;
        this.orderViewModel = orderViewModel;
        this.productViewModel = productViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ProductOrderedViewholderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        context = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        DetailOrder detailOrder = detailOrders.get(position);
        Extension.observeOnce(orderViewModel.getOrderById(detailOrder.getOrderId()),lifecycleOwner,order -> {
            try {
                holder.binding.txtOrderDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(sdf.parse(order.getOrderDate())));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        Extension.observeOnce(productViewModel.getProductById(detailOrder.getProductId()),lifecycleOwner,product -> {
            if(product.getImageUrl().contains("/")){
                Glide.with(context).load(product.getImageUrl())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.binding.imgProduct);
            }else{
                int drawableResource = holder.itemView.getResources().getIdentifier(product.getImageUrl().trim(),
                        "drawable",holder.itemView.getContext().getPackageName());
                Glide.with(context).load(drawableResource)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.binding.imgProduct);
            }
            holder.binding.txtProductName.setText(product.getName());
            holder.binding.txtUnit.setText(product.getUnit());
        });
        holder.binding.txtQuantity.setText(String.valueOf(detailOrder.getQuantity()));
        holder.binding.txtTotalPrice.setText((detailOrder.getQuantity() * detailOrder.getPrice())+ "đ");
    }

    @Override
    public int getItemCount() {
        return detailOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProductOrderedViewholderBinding binding;
        public ViewHolder(@NonNull ProductOrderedViewholderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
