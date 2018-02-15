package com.greenlab.agromonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.greenlab.agromonitor.adapters.ProductListAdapter;

import java.util.ArrayList;

public class NewProjectActivity extends AppCompatActivity {

    ArrayList<String> listProducts;
    RadioButton radioCana;
    RadioButton radioSoja;
    RecyclerView recyclerProducs;
    ProductListAdapter productListAdapter;
    Button btnAddProduct;
    EditText productLabelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_project_layout);

        radioCana = findViewById(R.id.radio_cana_de_acucar);
        radioSoja = findViewById(R.id.radio_soja);
        recyclerProducs = findViewById(R.id.recycler_products);
        btnAddProduct = findViewById(R.id.btn_add_products);
        productLabelText = findViewById(R.id.add_product_label);
        listProducts = new ArrayList<>();

        productListAdapter = new ProductListAdapter(listProducts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerProducs.setLayoutManager(mLayoutManager);
        recyclerProducs.setItemAnimator(new DefaultItemAnimator());
        recyclerProducs.setAdapter(productListAdapter);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product = productLabelText.getText().toString();

                if (!product.isEmpty()){
                    listProducts.add(product);
                    productListAdapter.notifyDataSetChanged();
                }
            }
        });

        populateFields();

    }

    public void populateFields(){
        this.listProducts.clear();
        this.radioCana.setChecked(true);
    }
}
