package com.example.todolistv1;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView todoRecView;
    ArrayList<String> items = new ArrayList<>();
    TodoAdapterRecView adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set the toolbar as the ActionBar
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        todoRecView = findViewById(R.id.todoRecView);


        items.add("banana");
        items.add("apple");
        items.add("pear");

        adapter = new TodoAdapterRecView(this);
        adapter.setItems(items);
        todoRecView.setAdapter(adapter);
        todoRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            // Show a dialog for adding an item
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add New Item");

            final EditText input = new EditText(this);
            input.setHint("Enter item name");
            builder.setView(input);

            builder.setPositiveButton("Add", (dialog, which) -> {
                String newItem = input.getText().toString();
                Toast.makeText(this, "Created new item", Toast.LENGTH_SHORT).show();
                items.add(newItem);
                adapter.setItems(items);
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
