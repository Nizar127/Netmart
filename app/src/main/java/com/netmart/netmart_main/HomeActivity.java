package com.netmart.netmart_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.netmart.netmart_main.Model.Products;
import com.netmart.netmart_main.Model.Users;
import com.netmart.netmart_main.Prevalent.Prevalant;
import com.netmart.netmart_main.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference prodRef;
    private Users users;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private RecyclerView.LayoutManager layoutManager;
    private TextView searchTextBar;
    private Button selectCategoryBtn;
    private String categoryComp = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);

        prodRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Home");
//        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        searchTextBar = findViewById(R.id.search_bar_go);
        searchTextBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        //Displaying user info in the navigation drawer
        userInfoDisplay(profileImageView, userNameTextView);

        //define recyclerview
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        //setting the recyclerview with the layout manager
        recyclerView.setLayoutManager(layoutManager);

        //As for selecting category so that they can be displayed in category
        selectCategoryBtn = findViewById(R.id.category_selector);
        selectCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory();
            }
        });


    }

    private void openCategory() {
        CharSequence options[] = new CharSequence[]{
                "Any",      //0
                "Chicken",  //1
                "Meat",     //2
                "Vegetable",//3
                "Dairy",    //4
                "Fruits",    //5
                "Grains",    //6
                "Noodles",   //7
                "Spices",   //8
                "Frozen",   //9
                "Bakery",   //10
                "Beverage", //11
                "Seafood",  //12
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Select Category :");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    categoryComp = "";
                    selectCategoryBtn.setText("Category");
                    filterCategory();
                }
                if (which == 1){
                    categoryComp = "chicken";
                    selectCategoryBtn.setText("Chicken");
                    filterCategory();
                }
                if (which == 2){
                    categoryComp = "meat";
                    selectCategoryBtn.setText("Meat");
                    filterCategory();
                }
                if (which == 3){
                    categoryComp = "vegetable";
                    selectCategoryBtn.setText("Vegetable");
                    filterCategory();
                }
                if (which == 4){
                    categoryComp = "dairy";
                    selectCategoryBtn.setText("Dairy");
                    filterCategory();
                }
                if (which == 5){
                    categoryComp = "fruit";
                    selectCategoryBtn.setText("Fruits");
                    filterCategory();
                }
                if (which == 6){
                    categoryComp = "grain";
                    selectCategoryBtn.setText("Grains");
                    filterCategory();
                }
                if (which == 7){
                    categoryComp = "noodle";
                    selectCategoryBtn.setText("Noodles");
                    filterCategory();
                }
                if (which == 8){
                    categoryComp = "spices";
                    selectCategoryBtn.setText("Spices");
                    filterCategory();
                }
                if (which == 9){
                    categoryComp = "frozen";
                    selectCategoryBtn.setText("Frozen");
                    filterCategory();
                }
                if (which == 10){
                    categoryComp = "bakery";
                    selectCategoryBtn.setText("Bakery");
                    filterCategory();
                }
                if (which == 11){
                    categoryComp = "beverage";
                    selectCategoryBtn.setText("Beverages");
                    filterCategory();
                }
                if (which == 12){
                    categoryComp = "seafood";
                    selectCategoryBtn.setText("Seafood");
                    filterCategory();
                }
            }
        });
        builder.show();

    }

    private void userInfoDisplay(final CircleImageView profileImageView, final TextView userNameTextView) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalant.currentOnlineUser.getUsername());

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name;

                if (dataSnapshot.exists()){
                    //Display user info with user image exist
                    if (dataSnapshot.child("image").exists()){

                        String image = dataSnapshot.child("image").getValue().toString();

                        if (dataSnapshot.child("fullname").exists()){
                            name = dataSnapshot.child("fullname").getValue().toString();
                        }
                        else {
                            name = dataSnapshot.child("username").getValue().toString();
                        }

                        Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImageView);
                        userNameTextView.setText(name);
                    }
                    //Display user info without user image exist
                    else if (!dataSnapshot.child("image").exists()) {
                        if (dataSnapshot.child("fullname").exists()) {
                            if (dataSnapshot.child("phoneOrder").exists()){
                                name = dataSnapshot.child("fullname").getValue().toString();
                                userNameTextView.setText(name);
                            }
                        } else {
                            name = dataSnapshot.child("username").getValue().toString();
                            userNameTextView.setText(name);
                        }
                    }
                }
                else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void filterCategory(){
        //Call back so that recyclerview can be refreshed
        onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(prodRef.orderByChild("category").startAt(categoryComp).endAt(categoryComp + "\uf8ff"), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {


                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        //To display the layout to be use for recyclerview
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_items_layout, viewGroup, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        //To replace the id from the content of the layout used for recyclerview
                        //txtProductName, txtProductPrice, txtProductDesc are being called from ProductViewHolder.java
                        holder.txtProductName.setText(model.getName());
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.txtProductPrice.setText("RM " + model.getPrice());
                        holder.txtProductDesc.setText(model.getDescription());

                        //Set on click listener for specific product by
                        // sending the pid of the product to the ProductDetailsActivity class
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                            }
                        });
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_order) {
            Intent intent = new Intent(HomeActivity.this, OrderActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_categories) {
            openCategory();
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_address){
            Intent intent = new Intent(HomeActivity.this, AddressActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {

            CharSequence options[] = new CharSequence[]{
                    "Yes", "No"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Log Out??");

            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //If admins press Yes
                    if (which == 0) {
                        Toast.makeText(HomeActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                        Paper.book().destroy();

                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    //else admins pressed No
                    else {
                    }
                }
            });
            builder.show();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            CharSequence options[] = new CharSequence[]{
                    "Yes", "No"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Exit?");

            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //If admins press Yes
                    if (which == 0){
                        finish();
                    }
                    //else admins pressed No
                    else {
                    }
                }
            });
            builder.show();
            //super.onBackPressed();
        }    }
}
