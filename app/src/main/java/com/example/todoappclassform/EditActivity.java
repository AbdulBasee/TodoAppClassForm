package com.example.todoappclassform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        etItem = findViewById(R.id.etitem);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit item");
        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEMS_TEXT));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // create an intent which will contain the result
                Intent intent = new Intent();
                // pass the data (results of editing)
                intent.putExtra(MainActivity.KEY_ITEMS_TEXT, etItem.getText().toString());
                intent.putExtra(MainActivity.KEY_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_POSITION));
                // finish activity, close the screen and go back
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}