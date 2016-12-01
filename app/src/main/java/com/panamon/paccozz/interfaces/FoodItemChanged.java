package com.panamon.paccozz.interfaces;

/**
 * Created by Hari on 11/30/2016.
 */

public interface FoodItemChanged {
    void onFoodItemCountChanged(String itemId,int itemCount);
    void onCustomizationClicked(String itemId);
}
