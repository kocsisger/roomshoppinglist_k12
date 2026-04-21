package hu.unideb.inf.roomshoppinglist;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import hu.unideb.inf.roomshoppinglist.databinding.ActivityMainBinding;
import hu.unideb.inf.roomshoppinglist.model.ShoppingListDatabase;
import hu.unideb.inf.roomshoppinglist.model.ShoppingListItem;

public class MainActivity extends AppCompatActivity {

    ShoppingListDatabase shoppingListDatabase;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        shoppingListDatabase = Room.databaseBuilder(this, ShoppingListDatabase.class, "shoppinglist_db")
                .fallbackToDestructiveMigration(true)
                .build();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shoppingListDatabase.shoppingListDAO().getAllItems().observe(this,
                shoppingListItems -> binding.recyclerView.setAdapter(new ViewAdapter(shoppingListItems)));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                                                  ItemTouchHelper.LEFT + ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                ShoppingListItem sli = ((ViewAdapter)binding.recyclerView.getAdapter()).getDataItemAt(
                        viewHolder.getAbsoluteAdapterPosition());

                new Thread(
                        () -> shoppingListDatabase.shoppingListDAO().deleteItem(sli)
                ).start();
            }
        });

        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
    }

    public void addItem(View view) {
        new Thread(
                () -> {
                    ShoppingListItem sli = new ShoppingListItem();
                    sli.setName(binding.newItemEditText.getText().toString());
                    shoppingListDatabase.shoppingListDAO().insertListItem(sli);
                    /*String list = shoppingListDatabase.shoppingListDAO().getAllItems().toString();
                    Log.d("CheckDB", list);
                    runOnUiThread(() -> binding.shoppingListTextView.setText(list));*/
                }
        ).start();
    }

    public void clearDB(View view) {
        new Thread(() -> shoppingListDatabase.shoppingListDAO().deleteDB()).start();
    }
}
