package com.example.bookstore.adapters;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.models.ItemOrderModel;
import com.example.bookstore.models.ProductModel;
import com.example.bookstore.utils.ManageLogCart;
import com.example.bookstore.utils.ProcessCurrency;

import java.util.ArrayList;

/**
 * Author : Hào
 * Mô tả : ItemAdapter được sử dụng để gắn giá trị itemLists vào layout layout_products_cart
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>  {
    ArrayList<ProductModel> itemLists;
    AdapterUpdate updateInterface;
    OnProductListener onProductListener;
    Context context;

    public ItemAdapter(ArrayList<ProductModel> itemLists, AdapterUpdate updateInterface, OnProductListener onProductListener, Context context
    ) {
        this.itemLists = itemLists;
        this.updateInterface = updateInterface;
        this.onProductListener = onProductListener;
        this.context = context;

        int S = 0;
        System.out.println(itemLists);


        for (ProductModel product : itemLists)
        {
            Log.d("information", product.getName() + " " + product.getPriceTmp() + " " + product.getQuantity());
            S += product.getPriceTmp() * product.getQuantity();
        }
        Log.i("Sum", "" + S);
        TextView gia_ca = ((Activity) context).findViewById(R.id.hao);
        if (gia_ca != null) {
            Log.i("Sum", "Gia ca thanh cong ");
            gia_ca.setText(ProcessCurrency.convertNumberToString(S));
        }

        TextView tong_tt = ((Activity) context).findViewById(R.id.tong_tien);
        if (tong_tt != null){
            Log.i("Sum", "Tong Thanh Toan thanh cong ");
            tong_tt.setText(ProcessCurrency.convertNumberToString(S + 50000));
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.layout_products_cart, parent, false);
        return new ViewHolder(v, onProductListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameBookView.setText(itemLists.get(position).getName().toString());
        holder.priceView.setText(ProcessCurrency.convertNumberToString(itemLists.get(position).getPriceTmp()));
//        holder.imgView.setImageResource(itemLists.get(position).getImg());
        holder.imgView.setImageResource(R.drawable.nha_gia_kim);
        holder.numberProductView.setText(String.valueOf(itemLists.get(position).getQuantity()));
        final int Fposition = position;

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Increase numberProduct to 1
                int numberProduct = Integer.valueOf(holder.numberProductView.getText().toString()).intValue();
                holder.numberProductView.setText(String.valueOf(numberProduct + 1));
                updateInterface.UpdateTotalCostAndProduct(true, ProcessCurrency.convertStringToNumber(holder.priceView.getText().toString()));

                TextView gia_ca = ((Activity) context).findViewById(R.id.hao);
                int hao = 0;
                if (gia_ca != null)
                    hao = ProcessCurrency.convertStringToNumber(gia_ca.getText().toString());
                TextView tong_tt = ((Activity) context).findViewById(R.id.tong_tien);
                if (tong_tt != null){
                    tong_tt.setText(ProcessCurrency.convertNumberToString(50000 + hao + ProcessCurrency.convertStringToNumber(holder.priceView.getText().toString())));
                    gia_ca.setText(ProcessCurrency.convertNumberToString(hao + ProcessCurrency.convertStringToNumber(holder.priceView.getText().toString())));
                }

                itemLists.get(Fposition).setQuantity(numberProduct + 1);

//                ManageLogCart.writeListProductToFile(itemLists, context.getApplicationContext());
                ProductModel.incDecProductOnCart("haobui", itemLists.get(Fposition).getID(), true);

                notifyDataSetChanged();
            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberProduct = Integer.valueOf(holder.numberProductView.getText().toString()).intValue();
                updateInterface.UpdateTotalCostAndProduct(false, ProcessCurrency.convertStringToNumber(holder.priceView.getText().toString()));

                TextView gia_ca = ((Activity) context).findViewById(R.id.hao);
                int hao = 0;
                if (gia_ca != null)
                    hao = ProcessCurrency.convertStringToNumber(gia_ca.getText().toString());
                TextView tong_tt = ((Activity) context).findViewById(R.id.tong_tien);
                if (tong_tt != null){
                    tong_tt.setText(ProcessCurrency.convertNumberToString(50000 + hao - ProcessCurrency.convertStringToNumber(holder.priceView.getText().toString())));
                    gia_ca.setText(ProcessCurrency.convertNumberToString(hao - ProcessCurrency.convertStringToNumber(holder.priceView.getText().toString())));
                }

                if (numberProduct - 1 == 0){
                    ProductModel.incDecProductOnCart("haobui", itemLists.get(Fposition).getID(), false);
                    itemLists.remove(Fposition);
                }
                else {
                    holder.numberProductView.setText(String.valueOf(numberProduct - 1));
                    itemLists.get(Fposition).setQuantity(numberProduct - 1);

                    ProductModel.incDecProductOnCart("haobui", itemLists.get(Fposition).getID(), false);
                }

                if (itemLists.size() == 0){
                    if (gia_ca != null){
                        View payment_toolbar = ((Activity) context).findViewById(R.id.payment_toolbar_buyer);
                        payment_toolbar.setVisibility(View.GONE);
                    }
                    else{
                        View order_toolbar = ((Activity) context).findViewById(R.id.order_toolbar);
                        order_toolbar.setVisibility(View.GONE);
                    }
                }
//                ManageLogCart.writeListProductToFile(itemLists, context.getApplicationContext());


                Log.i("adapter", "Button Decrease of " + Fposition);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgView;
        TextView nameBookView, priceView, numberProductView;
        ImageButton btnAdd, btnDecrease;
        OnProductListener onProductListener = null;

        public ViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgBookCart);
            nameBookView = itemView.findViewById(R.id.nameBook);
            priceView = itemView.findViewById(R.id.priceBook);
            numberProductView = itemView.findViewById(R.id.numberProduct);
            btnAdd = (ImageButton) itemView.findViewById(R.id.btnAddOfOrder);
            btnDecrease =(ImageButton) itemView.findViewById(R.id.btnRemoveOfOrder);
            this.onProductListener = onProductListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onProductListener.onProductClick(getAdapterPosition());
        }
    }

    public interface OnProductListener{
        void onProductClick(int positionProduct);
    }

    public interface AdapterUpdate{
        void UpdateTotalCostAndProduct(Boolean isPlus, int money);
    }
}