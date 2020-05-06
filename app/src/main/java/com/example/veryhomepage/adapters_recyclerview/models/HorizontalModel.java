package com.example.veryhomepage.adapters_recyclerview.models;

import com.example.veryhomepage.coupon.CouponVO;
import com.example.veryhomepage.product.ProductVO;
import com.example.veryhomepage.recipe.RecipeVO;

import java.util.ArrayList;

public class HorizontalModel {
    private String title;
    private ArrayList<CouponVO> arrayList;
    private ArrayList<ProductVO> arrayList1;
    private ArrayList<RecipeVO> arrayList2;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<CouponVO> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<CouponVO> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<ProductVO> getArrayList1() {
        return arrayList1;
    }

    public void setArrayList1(ArrayList<ProductVO> arrayList1) {
        this.arrayList1 = arrayList1;
    }

    public ArrayList<RecipeVO> getArrayList2() {
        return arrayList2;
    }

    public void setArrayList2(ArrayList<RecipeVO> arrayList2) {
        this.arrayList2 = arrayList2;
    }
}
