package com.example.fruitshop.Presenter.Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fruitshop.Application.ViewModel.CategoryViewModel;
import com.example.fruitshop.Application.ViewModel.ProductViewModel;
import com.example.fruitshop.Domain.Entities.Category;
import com.example.fruitshop.Domain.Entities.Product;
import com.example.fruitshop.Presenter.Custom.MyModal;
import com.example.fruitshop.Presenter.Custom.MyToast;
import com.example.fruitshop.R;
import com.example.fruitshop.databinding.ActivityCreateProductBinding;

import java.util.ArrayList;
import java.util.List;

public class CreateProductActivity extends ChooseImageActivity {

    ActivityCreateProductBinding binding;
    Dialog dialog;
    ProductViewModel productViewModel;
    CategoryViewModel categoryViewModel;
    int selectedCategoryId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateProductBinding.inflate(getLayoutInflater());
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v->finish());
        binding.btnUpload.setOnClickListener(v->openImagePicker());

        categoryViewModel.getCategories().observe(this,categories -> {
            binding.txtDanhMuc.setOnClickListener(v->{
                dialog = new Dialog(CreateProductActivity.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(800,1000);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.edittext_white_bg);
                dialog.show();

                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(CreateProductActivity.this, android.R.layout.simple_list_item_1,new ArrayList<>(categories)){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        // Gọi phương thức getView() của ArrayAdapter gốc
                        View view = super.getView(position, convertView, parent);

                        // Lấy tên danh mục từ đối tượng DanhMuc
                        Category category = getItem(position);
                        if (category != null) {
                            TextView textView = (TextView) view.findViewById(android.R.id.text1);
                            textView.setText(category.getName());  // Hiển thị tên danh mục
                        }

                        return view;
                    }
                    @Override
                    public Filter getFilter() {
                        return new Filter() {
                            @Override
                            protected FilterResults performFiltering(CharSequence constraint) {
                                FilterResults results = new FilterResults();
                                List<Category> filteredList = new ArrayList<>();

                                if (constraint == null || constraint.length() == 0) {
                                    // Nếu không có từ khóa lọc, trả về danh sách gốc
                                    filteredList.addAll(categories);
                                } else {
                                    // Lọc danh sách theo tên danh mục
                                    for (Category category : categories) {
                                        if (category.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                            filteredList.add(category);
                                        }
                                    }
                                }

                                results.values = filteredList;
                                results.count = filteredList.size();
                                return results;
                            }

                            @Override
                            protected void publishResults(CharSequence constraint, FilterResults results) {
                                if (results != null && results.values != null) {
                                    clear();
                                    addAll((List<Category>) results.values);
                                    notifyDataSetChanged();
                                }
                            }
                        };
                    }
                };

                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        binding.txtDanhMuc.setText(adapter.getItem(position).getName());
                        selectedCategoryId = adapter.getItem(position).getId();
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            });
        });




        binding.btnCreate.setOnClickListener(v->{
            if(binding.edtName.getText().toString().equals("")){
                MyToast.showError(this,"Tên không được để trống");
                return;
            }
            if(binding.edtUnit.getText().toString().equals("")){
                MyToast.showError(this,"Đơn vị không được để trống");
                return;
            }
            if(binding.edtPrice.getText().toString().equals("")){
                MyToast.showError(this,"Giá tiền không được để trống");
                return;
            }
            if(selectedCategoryId == -1){
                MyToast.showError(this,"Vui lòng chọn danh mục");
                return;
            }
            Product product = new Product();
            product.setName(binding.edtName.getText().toString());
            product.setCategoryId(selectedCategoryId);
            product.setPrice(Integer.parseInt(binding.edtPrice.getText().toString()));
            product.setUnit(binding.edtUnit.getText().toString());
            product.setDescription(binding.edtDescription.getText().toString());
            String imageUrl = saveImageToInternalStorage(currentImageUrl);
            product.setImageUrl(imageUrl);
            productViewModel.addProduct(product).thenAccept(x->{
                runOnUiThread(()->{
                    MyModal.showSuccess(CreateProductActivity.this,"Tạo sản phẩm thành công",()->finish());
                });
            });
        });
    }

    @Override
    protected void loadImage() {
        Glide.with(this).load(currentImageUrl).override(200,200).apply(RequestOptions.circleCropTransform()).into(binding.image);
    }
}