package com.netmart.netmart_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.netmart.netmart_main.Model.Products;
import com.netmart.netmart_main.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class SearchProductsActivity extends AppCompatActivity {

    //private FloatingActionButton searchFab;
    private Button searchFab;
    private AutoCompleteTextView inputText;
    private RecyclerView searchList;
    private String searchInput;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products");

    private ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        inputText = findViewById(R.id.search_product_name);
        searchFab = findViewById(R.id.search_fab);
        searchList = findViewById(R.id.search_list);

        backBtn = findViewById(R.id.back_btn_search);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchList.setLayoutManager(new GridLayoutManager(this, 2));

        //AutoCompleteTextView Adapter starts
        //Create a new ArrayAdapter with your context and the simple layout for the dropdown menu provided by Android
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //for when we click on the suggested item

        inputText.setAdapter(autoComplete);
        //For when we click on the suggested item, it actually go search right away
        inputText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchInput = inputText.getText().toString();
                searchProducts();
            }
        });


        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = inputText.getText().toString();
                searchProducts();
            }
        });
    };



    private void searchProducts() {

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ref.orderByChild("nameLower").startAt(searchInput.toLowerCase()).endAt(searchInput.toLowerCase() + "\uf8ff"), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {


                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_items_layout, viewGroup, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getName());
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.txtProductPrice.setText("RM " + model.getPrice());
                        holder.txtProductDesc.setText(model.getDescription());

                        //Set on click listener for specific product by
                        // sending the pid of the product to the ProductDetailsActivity class
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                //intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                            }
                        });
                    }
                };
}
}
