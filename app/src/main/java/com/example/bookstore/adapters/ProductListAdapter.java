package com.example.bookstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.models.ProductModel;
import com.example.bookstore.utils.ProcessCurrency;

import java.util.ArrayList;


/**
 * Created by reiko-lhnhat on 12/1/2021.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> implements Filterable {
    private ArrayList<ProductModel> productList;
    private ArrayList<ProductModel> fillteredProductList;

    private OnProductListener mOnProductListener;
    public ProductListAdapter(ArrayList<ProductModel> productList, OnProductListener onProductListener) {
        this.productList = productList;
        this.mOnProductListener = onProductListener;
        this.fillteredProductList = productList;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_onlist_layout, parent, false);
        return new ViewHolder(view,  mOnProductListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel data = fillteredProductList.get(position);
        holder.imageProduct.setImageResource(data.getImg());
        holder.nameProduct.setText(data.getName());
        holder.price.setText(ProcessCurrency.convertNumberToString(Integer.parseInt(data.getPrice())) + " vnđ");
    }

    @Override
    public int getItemCount() {
        return fillteredProductList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageProduct;
        TextView nameProduct;
        TextView price;
        OnProductListener onProductListener;
        ViewHolder(View v, OnProductListener onProductListener) {
            super(v);
            imageProduct = v.findViewById(R.id.imageProductonList);
            nameProduct = v.findViewById(R.id.nameProductonList);
            price = v.findViewById(R.id.priceProductonList);
            this.onProductListener = onProductListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           onProductListener.onProductClick(getAdapterPosition());
        }
    }

    public interface OnProductListener{
        void onProductClick(int positionProduct);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty())
                    fillteredProductList = productList;
                else {
                    ArrayList<ProductModel> list = new ArrayList<>();
                    for(ProductModel pro : productList){
                        if(pro.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(pro);
                        }
                    }
                    fillteredProductList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = fillteredProductList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                fillteredProductList = (ArrayList<ProductModel>) results.values;
                notifyDataSetChanged();
            }
        } ;
    }
}
