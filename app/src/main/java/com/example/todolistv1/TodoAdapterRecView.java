package com.example.todolistv1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class TodoAdapterRecView extends RecyclerView.Adapter<TodoAdapterRecView.ViewHolder> {

    private ArrayList<String> items = new ArrayList<>();
    private Context context;

    public TodoAdapterRecView(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_todo_rv, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtItem.setText(items.get(position));

        holder.parent.setOnClickListener(view -> {
            // Show the AlertDialog with "Edit" and "Delete" options
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Choose an action");

            String[] options = {"Edit", "Delete"};
            builder.setItems(options, (dialog, which) -> {  //cho cai array tren vao thanh cac lua chon

                if (which == 0) {
                    // Edit option
                    AlertDialog.Builder editBuilder = new AlertDialog.Builder(context);
                    editBuilder.setTitle("Edit Item");

                    final EditText input = new EditText(context);
                    input.setText(items.get(position));  // Pre-fill with the current item
                    editBuilder.setView(input);

                    editBuilder.setPositiveButton("Save", (editDialog, editWhich) -> {
                        String newItem = input.getText().toString();
                        items.set(position, newItem);  // Update the item in the list
                        notifyItemChanged(position);  // Notify the adapter
                        Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show();
                    });

                    editBuilder.setNegativeButton("Cancel", (editDialog, editWhich) -> editDialog.cancel());
                    editBuilder.show();

                } else if (which == 1) {
                    // Delete option
                    items.remove(position);  // Remove the item from the list
                    notifyItemRemoved(position);  // Notify the adapter
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                }
            });

            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(String item) {
        this.items.add(item);
        notifyItemInserted(items.size() - 1); // Notify only the newly added item
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtItem;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItem = itemView.findViewById(R.id.txtItem);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
