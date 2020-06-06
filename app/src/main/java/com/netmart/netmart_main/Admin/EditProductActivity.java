package com.netmart.netmart_main.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.netmart.netmart_main.R;
import com.netmart.netmart_main.ViewHolder.EditProductsViewHolder;
import com.squareup.picasso.Picasso;

public class EditProductActivity extends AppCompatActivity {

    private RecyclerView editProductsList;
    private DatabaseReference productsRef;
    private AutoCompleteTextView inputTextEditProd;
    private RecyclerView.LayoutManager layoutManager;

    private String searchInputEdit = "";
    private FloatingActionButton editSearchBtn;

    private ImageView backBtn;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productsRef.orderByChild("nameLower").startAt(searchInputEdit.toLowerCase()).endAt(searchInputEdit.toLowerCase() + "\uf8ff"), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, EditProductsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, EditProductsViewHolder>(options) {

                    @NonNull
                    @Override
                    public EditProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_items_layout, viewGroup,  false);
                        EditProductsViewHolder holder = new EditProductsViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EditProductsViewHolder holder, final int position , @NonNull Products model) {
                        Picasso.get().load(model.getImage()).into(holder.editImgView);
                        holder.editProdName.setText(model.getName());
                        holder.editProdDesc.setText(model.getDescription());
                        holder.editProdPrice.setText(model.getPrice());

                        holder.editProd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //String uID = getRef(position).getKey();
                                String productID =  getRef(position).getKey();
                                editProductGo(productID);
                                finish();
                            }
                        });

                        holder.removeProd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //We want to display what we actually click, so:
                                //getRef(position) = to use position as reference to pass to another activity
                                //getKey() = to get key from database, so that the next activity will display data accurately instead of mixing with other user's order
                                //String uID = getRef(position).getKey();
                                String productID =  getRef(position).getKey();
                                removeProductGo(productID);
                            }
                        });
                    }



                };
        editProductsList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeProductGo(final String productID) {
        CharSequence options[] = new CharSequence[]{
                "Yes", "No"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProductActivity.this);
        builder.setTitle("Are you sure to remove this product?");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //If admins press Yes
                if (which == 0){
                    //We want to display what we actually click, so:
                    //getRef(position) = to use position as reference to pass to another activity
                    //getKey() = to get key from database, so that the next activity will display data accurately instead of mixing with other user's order
                    //productsRef.child(productID).removeValue();
                    productsRef.child(productID).removeValue();
                }
                //else admins pressed No
                else {
                }
            }
        });
        builder.show();
    }

    private void editProductGo(String productID) {
        Intent intent = new Intent(EditProductActivity.this, EditProductFormActivity.class);
        intent.putExtra("Product ID", productID);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        inputTextEditProd = findViewById(R.id.edit_search_input);
        editSearchBtn = findViewById(R.id.edit_search_products);

        editProductsList = findViewById(R.id.edit_products_list);
        layoutManager = new GridLayoutManager(this, 2);
        editProductsList.setLayoutManager(layoutManager);

        backBtn = findViewById(R.id.back_btn_edit_products);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()){
                    //Get the suggestion by childing the key of the string you want to get.
                    String suggestion = suggestionSnapshot.child("nameLower").getValue(String.class);
                    //Add the retrieved string to the list
                    autoComplete.add(suggestion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        inputTextEditProd.setAdapter(autoComplete);
        //AutoCompleteTextView Adapter ends

        //For when we click on the suggested item, it actually go search right away
        inputTextEditProd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchInputEdit = inputTextEditProd.getText().toString();
                //Call back so that recyclerview can be refreshed
                onStart();
            }
        });

        editSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInputEdit = inputTextEditProd.getText().toString();
                //Call back so that recyclerview can be refreshed
                onStart();
            }
        });

        //AutoCompleteTextView RecyclerView Display




    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditProductActivity.this, AdminCategoryActivity.class);
        startActivity(intent);
    }
}
