package com.example.todoappclassform;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEMS_TEXT = "item_text";
    public static final String KEY_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;
    List<String> items;

    Button btnadd;
    EditText edItem;
    RecyclerView rvItems;
    itemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnadd = findViewById(R.id.button);
        edItem = findViewById(R.id.editText);
        rvItems = findViewById(R.id.rvItems);


        loadItems();


        itemsAdapter.OnLongClickListener onLongClickListener =  new itemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //Declare the items from the model;
                items.remove(position);
                //Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };

        itemsAdapter.OnclickListener onClickListener  = new itemsAdapter.OnclickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position" + position);
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(KEY_ITEMS_TEXT, items.get(position));
                i.putExtra(KEY_POSITION, position);
                startActivityForResult(i, EDIT_TEXT_CODE);
            }


        };
        itemsAdapter = new itemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toDoItem = edItem.getText().toString();

                items.add(toDoItem);
                itemsAdapter.notifyItemInserted(items.size() - 1);
                edItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }
    //handle the result of editactivity


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEMS_TEXT);
            int position = data.getExtras().getInt(KEY_POSITION);

            // update the model at the right position with new item text
            items.set(position, itemText);
            //notify the adapter
            itemsAdapter.notifyItemChanged(position);
            // persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "items updatted successfully", Toast.LENGTH_SHORT).show();

        } else {
            Log.w("Mainactivity", "unknown call to onactivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }
    // this function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }

    }
    // this function saves items by writing them into the data fiel
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}