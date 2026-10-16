package sv.edu.ues.vl23003.actividad_api_rest.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import sv.edu.ues.vl23003.actividad_api_rest.data.model.Product;
import sv.edu.ues.vl23003.actividad_api_rest.databinding.ItemProductBinding;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface OnProductActionListener {
        void onEdit(Product product);
        void onDelete(Product product);
    }

    private List<Product> productList;
    private OnProductActionListener listener;

    public ProductAdapter(List<Product> productList, OnProductActionListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        public ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product, OnProductActionListener listener) {
            binding.tvProductName.setText(product.getNombre());
            binding.tvProductDescription.setText(product.getDescripcion());
            String priceText = "$ " + String.format("%.2f", product.getPrecio());
            binding.tvProductPrice.setText(priceText);
            String stockText = "Stock: " + product.getStock();
            binding.tvProductStock.setText(stockText);

            binding.btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(product);
            });

            binding.btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(product);
            });
        }
    }
}