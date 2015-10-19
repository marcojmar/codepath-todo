package com.example.mmar.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class MainActivity extends Activity {
    private ArrayList<String> items = new ArrayList<String>();
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private EditText etItem;
    private int editPosition;

    private String data_file = "marco_todo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        etItem = (EditText) findViewById(R.id.etItem);

        // To delete the Todo task
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItem();
                return true;
            }
        });

        // To update the Todo task
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editPosition = position;
                String itemName = items.get(editPosition);

                Intent i = new Intent(view.getContext(), EditItemActivity.class);
                i.putExtra("MainActivity", itemName);
                startActivityForResult(i, 1);

            }
        });


        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String name = data.getExtras().getString("NewItem");

        // update the ListView object and save to the file
        items.set(editPosition, name);
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        writeItem();
    }

    public void populateArrayItems(String task) {
        if (!task.isEmpty()) {
            items.add(task);

        }
        //aitemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addItem(View v) {
        populateArrayItems(etItem.getText().toString());
        etItem.setText(null);
        writeItem();
    }

    public void readItems() {
        File r_file = new File(getFilesDir(), data_file);
        try {
           items = new ArrayList<String>(FileUtils.readLines(r_file));

        }
        catch (Exception e) {
            //
        }
    }

    public void writeItem() {
        File w_file = new File(getFilesDir(), data_file);
        try {
            FileUtils.writeLines(w_file, items);
        }
        catch (Exception e) {
            //
        }
    }

}
