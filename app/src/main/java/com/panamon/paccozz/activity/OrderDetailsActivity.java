package com.panamon.paccozz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.panamon.paccozz.R;
import com.panamon.paccozz.adapter.OrderDetailsAdapter;
import com.panamon.paccozz.common.ExpandableHeightListView;

import java.util.Arrays;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {
    TextView itemName, itemPrice, orderId, date, itemStatus, itemName1,
            itemPrice1, itemName2, itemprice2, itemTotal, vat, discount, totalpayableAmount, packageMode, paymentMode, OrderCode;

    String name, price, orderID, orderCode, pushTime, status, discountStr, packageModeStr, totCostStr, itemnamestr, serviceStr, payableStr, itemrateStr;
    ExpandableHeightListView elv;
    OrderDetailsAdapter orderDetailsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemName = (TextView) findViewById(R.id.name);
        itemPrice = (TextView) findViewById(R.id.price);
        orderId = (TextView) findViewById(R.id.orderid);
        OrderCode = (TextView) findViewById(R.id.orderid1);
        date = (TextView) findViewById(R.id.date);
        itemStatus = (TextView) findViewById(R.id.status);
        itemName1 = (TextView) findViewById(R.id.name1);
        itemPrice1 = (TextView) findViewById(R.id.price1);
        itemName2 = (TextView) findViewById(R.id.name2);
        itemprice2 = (TextView) findViewById(R.id.price2);
        itemTotal = (TextView) findViewById(R.id.itemTotal);
        vat = (TextView) findViewById(R.id.vat);
        discount = (TextView) findViewById(R.id.discount);
        totalpayableAmount = (TextView) findViewById(R.id.totalPayableAmount);
        packageMode = (TextView) findViewById(R.id.packageMode);
        paymentMode = (TextView) findViewById(R.id.paymentMode);
        elv = (ExpandableHeightListView) findViewById(R.id.exlv);
        elv.setExpanded(true);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        orderID = intent.getStringExtra("orderid");
        orderCode = intent.getStringExtra("ordercode");
        pushTime = intent.getStringExtra("pushtime");
        status = intent.getStringExtra("status");
        discountStr = intent.getStringExtra("discount");
        packageModeStr = intent.getStringExtra("packagemode");
        totCostStr = intent.getStringExtra("totcost");
        itemnamestr = intent.getStringExtra("item");
        serviceStr = intent.getStringExtra("service");
        payableStr = intent.getStringExtra("pay");
        itemrateStr = intent.getStringExtra("itemrate");

        List<String> itemsName = Arrays.asList(itemnamestr.split("\\s*,\\s*"));
        Log.e("items", itemsName.size() + "y");

        List<String> itemsPrice = Arrays.asList(itemrateStr.split("\\s*,\\s*"));
        Log.e("items", itemsPrice.size() + "y");



        itemName.setText(name);
        itemPrice.setText("₹ " + payableStr);
        orderId.setText("Order Id :" + orderID);
        date.setText(pushTime);
        itemStatus.setText(status);
        OrderCode.setText(orderCode);
        totalpayableAmount.setText("₹ " + payableStr);
        vat.setText("₹ " +serviceStr);
        discount.setText("₹ " +discountStr);
        itemTotal.setText("₹ " + totCostStr);
        packageMode.setText(packageModeStr);
        orderDetailsAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this, R.layout.activity_order_details, itemsName, itemsPrice);
        elv.setAdapter(orderDetailsAdapter);


    }
}
