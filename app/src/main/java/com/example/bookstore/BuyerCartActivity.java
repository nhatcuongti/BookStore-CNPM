package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bookstore.adapters.ItemAdapter;
import com.example.bookstore.models.ItemOrderModel;
import com.example.bookstore.models.ProductModel;
import com.example.bookstore.utils.ManageLogCart;
import com.example.bookstore.utils.ProcessCurrency;

import java.util.ArrayList;

public class BuyerCartActivity extends AppCompatActivity implements ItemAdapter.AdapterUpdate,
        ItemAdapter.OnProductListener,
        View.OnClickListener{

    ArrayList<ProductModel> listItems = new ArrayList<>();
    RecyclerView rv;
    ItemAdapter ia;
    ImageButton backward = null;
    Button purchaseBtn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_buyer_cart);



        initData();
//        ia = new ItemAdapter(listItems, this, this, this);
//
//        rv.setAdapter(ia);
//        rv.setLayoutManager(new LinearLayoutManager(this));

        TextView v = findViewById(R.id.totalCostProduct);

        backward = findViewById(R.id.backward_from_cart_buyer);
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        purchaseBtn = findViewById(R.id.purchaseBtnCart);
        purchaseBtn.setOnClickListener(this);
    }

    /**
     * Author : Hao
     * Mô tả : Hàm này dùng để lấy dữ liệu từ database về và tính toán sẵn một số giá trị mặc định như
     * tổng giá tiền hoặc là tổng số lượng sản phẩm
     */
    private void initData() {
        rv = findViewById(R.id.recyclerView);
        ArrayList<ProductModel> productsOnCart = ProductModel.getCart("haobui", new ProductModel.IQuery() {
            @Override
            public void onSuccess(ArrayList<ProductModel> productModels) {
                Log.i("CART", "Cart: " + productModels.toString());

//                if (productModels == null || productModels.size() == 0)
//                    listItems = new ArrayList<>();

                int totalCostNumber = 0;
                int totalQuantity = 0;
                for (ProductModel product : productModels) {
                    Log.i("CART", "Name : " + product.getName());
                    Log.i("CART", "Price : " + product.getPrice());
                    Log.i("CART", "Quantity : " + product.getQuantity());
                    totalCostNumber += product.getPriceTmp() * product.getQuantity();
                    totalQuantity += product.getQuantity();
                }

                View order_toolbar = findViewById(R.id.order_toolbar);
                if (productModels.size() == 0)
                    order_toolbar.setVisibility(View.GONE);
                else
                    order_toolbar.setVisibility(View.VISIBLE);

                TextView totalCost = findViewById(R.id.totalCostProduct);
                totalCost.setText(ProcessCurrency.convertNumberToString(totalCostNumber));

                TextView totalProduct = findViewById(R.id.totalNumberProductOfCart);
                totalProduct.setText(String.valueOf(totalQuantity));

                ia = new ItemAdapter(productModels, BuyerCartActivity.this, BuyerCartActivity.this, BuyerCartActivity.this);

                rv.setAdapter(ia);
                rv.setLayoutManager(new LinearLayoutManager(BuyerCartActivity.this));
            }
        });
//        Log.i("CART", "" + productsOnCart);
//        Log.i("CART", "Length" + productsOnCart.size());
//        listItems = ManageLogCart.getListProductFromFile(getApplicationContext());

    }

    @Override
    /**
     * Author : Hao
     * Parameter :
     *   -isPlus : bằng True khi click vào dấu "+" ở trong giỏ hàng. Bằng false khi click vào
     *   dấu "-" ở trong giở hàng
     *   -money : Là giá của sản phẩm vừa click vào dấu "+" hoặc dấu "-"
     *
     * Mô tả : Implement Interface của ItemOrder.model . Mục tiêu là thay đổi hai giá trị
     * tổng số lượng sản phẩm và tổng giá tiền
     */
    public void UpdateTotalCostAndProduct(Boolean isPlus, int money) {
        TextView totalProduct = findViewById(R.id.totalNumberProductOfCart);
        TextView totalCost = findViewById(R.id.totalCostProduct);

        int totalProductValue = Integer.valueOf(totalProduct.getText().toString()).intValue(); //Lấy tổng số lượng của sản phẩm
        totalProductValue = (isPlus) ? totalProductValue + 1 : totalProductValue - 1; // Thay đổi tùy vào isPlus
        totalProduct.setText(String.valueOf(totalProductValue)); // Set lại giá trị khi đã tính toán xong

        int totalCostValue = ProcessCurrency.convertStringToNumber(totalCost.getText().toString());// Lấy tổng chi phí của toàn bộ sản phẩm trong đơn hàng
        totalCostValue = (isPlus) ? totalCostValue + money : totalCostValue - money; // Thay đổi tùy vào isPlus
        totalCost.setText(ProcessCurrency.convertNumberToString(totalCostValue)); // Set lại giá trị khi đã tính toán xong

    }

    @Override
    public void onProductClick(int positionProduct) {
        ProductModel product = listItems.get(positionProduct);
        Intent intent = new Intent(this, BuyerDetailProductActivity.class);
        intent.putExtra("clickProduct_ProductDetail", product);
        this.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("listProduct_cart", listItems);
        System.out.println("Hello");
        this.startActivity(intent);
    }

}