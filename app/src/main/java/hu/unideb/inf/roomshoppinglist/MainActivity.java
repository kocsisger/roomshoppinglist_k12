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

import hu.unideb.inf.roomshoppinglist.model.ShoppingListDatabase;
import hu.unideb.inf.roomshoppinglist.model.ShoppingListItem;

public class MainActivity extends AppCompatActivity {

    ShoppingListDatabase shoppingListDatabase;

    EditText newItemEditText;
    TextView shoppingListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        newItemEditText = findViewById(R.id.newItemEditText);
        shoppingListTextView = findViewById(R.id.shoppingListTextView);

        shoppingListDatabase = Room.databaseBuilder(this, ShoppingListDatabase.class, "shoppinglist_db")
                .fallbackToDestructiveMigration(true)
                .build();
    }

    public void addItem(View view) {
        new Thread(
                () -> {
                    ShoppingListItem sli = new ShoppingListItem();
                    sli.setName(newItemEditText.getText().toString());
                    shoppingListDatabase.shoppingListDAO().insertListItem(sli);

                    String list = shoppingListDatabase.shoppingListDAO().getAllItems().toString();
                    Log.d("CheckDB", list);

                    runOnUiThread(() -> shoppingListTextView.setText(list));
                }
        ).start();
    }
}