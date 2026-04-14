package hu.unideb.inf.roomshoppinglist;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

        shoppingListDatabase.shoppingListDAO().getAllItems().observe(this,
                shoppingListItems -> binding.shoppingListTextView.setText(shoppingListItems.toString()));
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