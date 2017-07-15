package com.scorpiomiku.cookbook.basket;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.sql.BasketDataHelper;

public class CreateNewBasketActivity extends AppCompatActivity {

    private Button mCancelButton;
    private Button mSaveButton;
    private EditText mNameEditText;
    private EditText mMaterialEditText;

    private String mNameText;
    private String mMaterialText;

    private BasketDataHelper mBasketDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_basket);
        mCancelButton = (Button) findViewById(R.id.create_basket_cancel_button);
        mSaveButton = (Button) findViewById(R.id.create_basket_save_button);
        mNameEditText = (EditText) findViewById(R.id.create_basket_name_edit_text);
        mMaterialEditText = (EditText) findViewById(R.id.create_basket_material_edit_text);
        mBasketDataHelper = new BasketDataHelper(this,"BasketStore.db",null,1);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(RESULT_CANCELED, i);
                finish();
            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNameText = mNameEditText.getText() + "";
                mMaterialText = mMaterialEditText.getText() + "";
                SQLiteDatabase db = mBasketDataHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("foodname", mNameText);
                values.put("material", mMaterialText);
                db.insert("Basket", null, values);
                values.clear();
                Toast.makeText(CreateNewBasketActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("name_return", mNameText);
                intent.putExtra("material_return", mMaterialText);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
