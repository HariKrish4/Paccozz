package com.panamon.paccozz.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.panamon.paccozz.activity.HotelDetailsActivity;
import com.panamon.paccozz.activity.OrderReviewActivity;
import com.panamon.paccozz.dbadater.CommonDBHelper;

/**
 * Created by Hari on 12/21/2016.
 */

public class Utilities {

    public static void showAlert(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage("You have an ongoing order with a differtent vendor. Do you wish to cancel the order and continue with this vendor?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        CommonDBHelper.getInstance().truncateAllTables();
                        activity.finish();
                        Intent orderReview = new Intent(activity, HotelDetailsActivity.class);
                        activity.startActivity(orderReview);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        activity.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
