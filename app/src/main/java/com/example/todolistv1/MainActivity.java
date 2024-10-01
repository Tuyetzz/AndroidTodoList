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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView todoRecView;
    ArrayList<String> items = new ArrayList<>();
    TodoAdapterRecView adapter;

    // Firebase Database reference
    private DatabaseReference dbRef;
    private FloatingActionButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Database
        dbRef = FirebaseDatabase.getInstance().getReference("todos");

        // Set the toolbar as the ActionBar
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (FirebaseApp.getApps(this).isEmpty()) {
            Toast.makeText(this, "Firebase not initialized!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Firebase initialized successfully!", Toast.LENGTH_SHORT).show();
        }

        saveButton = findViewById(R.id.saveBtn);  // Reference to the save button
        todoRecView = findViewById(R.id.todoRecView);

        adapter = new TodoAdapterRecView(this);
        adapter.setItems(items);
        todoRecView.setAdapter(adapter);
        todoRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Fetch data from Firebase and update RecyclerView
        fetchDataFromFirebase();

        // Set up save button listener
        saveButton.setOnClickListener(v -> {
            Toast.makeText(this, "Prepare to save", Toast.LENGTH_SHORT).show();
            if (!items.isEmpty()) {
                dbRef.setValue(items)
                        .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Data saved", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(MainActivity.this, "No items to save", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDataFromFirebase() {
        // Lắng nghe sự thay đổi dữ liệu từ Firebase
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Dọn dẹp danh sách hiện tại trước khi thêm dữ liệu mới
                items.clear();

                // Duyệt qua tất cả các giá trị trong node "todos"
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String item = snapshot.getValue(String.class); // Giả định rằng mỗi mục là một chuỗi
                    items.add(item);
                }

                // Cập nhật RecyclerView
                adapter.setItems(items);
                Toast.makeText(MainActivity.this, "Data loaded from Firebase", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Toast.makeText(MainActivity.this, "Failed to load data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            // Show a dialog for adding a new item
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
