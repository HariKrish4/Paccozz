package com.panamon.paccozz.activity;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.panamon.paccozz.R;
import com.panamon.paccozz.common.Singleton;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        startPayment();
    }

    @Override
    public void onPaymentSuccess(String s) {
        Snackbar.make(progressBar, "Payment Success: " + s, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Snackbar.make(progressBar, "Payment Error: " + s, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void startPayment() {

        progressBar.setVisibility(View.VISIBLE);
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.mipmap.ic_launcher);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: Rentomojo || HasGeek etc.
             */
            options.put("name", Singleton.getInstance().UserName);

            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Order #123456");

            options.put("currency", "INR");
            options.put("email", Singleton.getInstance().UserEmail);
            options.put("contact", Singleton.getInstance().UserMobile);
            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            double amount = getIntent().getDoubleExtra("amount", 0);
            int amountInt = (int) amount * 100;
            options.put("amount", amountInt + "");

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("pay", "Error in starting Razorpay Checkout", e);
        }

        progressBar.setVisibility(View.GONE);
    }
}
