package com.example.veryhomepage.recipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.veryhomepage.R;
import com.example.veryhomepage.main.Util;

public class RecipeActivity extends AppCompatActivity {
    private ImageView ivRecipeAct;
    private TextView tvR_Name, tvR_Price;
    private int imageSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        findViews();
        showResult();
    }


    private void findViews() {
        ivRecipeAct = findViewById(R.id.ivRecipeAct);
        tvR_Name = findViewById(R.id.tvR_Name);
    }

    private void showResult() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        RecipeVO recipeVO = (RecipeVO) bundle.getSerializable("recipeVO");

        String base64 = recipeVO.getRecipe_photo();

        byte[] dedcodedString = Base64.decode(base64.split(",")[1], Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(dedcodedString,0 ,dedcodedString.length);
        Bitmap covertedImage = Util.getResizedBitmap(decodedByte, imageSize);
        ivRecipeAct.setImageBitmap(covertedImage);

        tvR_Name.setText("料理包名稱: " + recipeVO.getRecipe_name());
//        tvR_Price.setText("料理包價格:" + recipeVO.getRecipe_price());
    }
}
