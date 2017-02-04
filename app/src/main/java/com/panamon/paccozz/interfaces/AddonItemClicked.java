package com.panamon.paccozz.interfaces;

/**
 * Created by Hari on 12/1/2016.
 */

public interface AddonItemClicked {
     void onAddonItemClicked(String addonId);
     void onAddonSubItemClicked(String subItemCost, int itemCount,String addOnItemId, String cost, boolean isChecked);
}
