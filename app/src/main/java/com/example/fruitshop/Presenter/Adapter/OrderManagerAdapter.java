package com.example.fruitshop.Presenter.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitshop.Application.ViewModel.OrderViewModel;
import com.example.fruitshop.Application.ViewModel.UserViewModel;
import com.example.fruitshop.Domain.Entities.Order;
import com.example.fruitshop.Infrastructure.Tool.Extension;
import com.example.fruitshop.Presenter.Activity.DetailOrderActivity;
import com.example.fruitshop.Presenter.Activity.UpdateCategoryActivity;
import com.example.fruitshop.Presenter.Custom.MyToast;
import com.example.fruitshop.R;
import com.example.fruitshop.databinding.OrderManagerViewholderBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderManagerAdapter extends RecyclerView.Adapter<OrderManagerAdapter.ViewHolder> {
    ArrayList<Order> orders;
    Context context;
    OrderViewModel orderViewModel;
    UserViewModel userViewModel;
    OrderManagerViewholderBinding binding;
    LifecycleOwner lifecycleOwner;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public OrderManagerAdapter(ArrayList<Order> orders, OrderViewModel orderViewModel, UserViewModel userViewModel, LifecycleOwner lifecycleOwner) {
        this.orders = orders;
        this.orderViewModel = orderViewModel;
        this.userViewModel = userViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public OrderManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = OrderManagerViewholderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        context = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderManagerAdapter.ViewHolder holder, int position) {
        Order order = orders.get(position);
        Extension.observeOnce(userViewModel.getUserById(order.getUserId()),lifecycleOwner,user -> {
            holder.binding.txtCustomerName.setText(user.getName());

        });
        try {
            holder.binding.txtOrderDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(sdf.parse(order.getOrderDate())));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.binding.txtTotalPrice.setText(order.getTotalPrice() + "đ");
        holder.binding.txtMethod.setText(order.getPaymentMethod());
        holder.binding.txtStatus.setText(order.getStatus());
        holder.binding.btnMenu.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(context,holder.binding.btnMenu);
            popupMenu.inflate(R.menu.update_delete_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getTitle().toString()) {
                    case "Xem chi tiết":
                        Intent intent = new Intent(context, DetailOrderActivity.class);
                        intent.putExtra("item",order);
                        context.startActivity(intent);
                        return true;
                    case "Xóa":
                        // Thêm logic xóa ở đây
                        orderViewModel.deleteOrder(order).thenAccept(x->{
                            MyToast.showSuccess(context,"Xóa thành công");
                        });;
                        return true;
                }
                return false;
            });

            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OrderManagerViewholderBinding binding;
        public ViewHolder(@NonNull OrderManagerViewholderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
