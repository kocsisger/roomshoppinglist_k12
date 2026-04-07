package hu.unideb.inf.roomshoppinglist;

import android.os.Bundle;
import android.util.Log;

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

        shoppingListDatabase = Room.databaseBuilder(this, ShoppingListDatabase.class, "shoppinglist_db")
                .fallbackToDestructiveMigration(true)
                .build();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        ShoppingListItem sli = new ShoppingListItem();
                        sli.setName("Alma");
                        shoppingListDatabase.shoppingListDAO().insertListItem(sli);

                        Log.d("CheckDB", shoppingListDatabase.shoppingListDAO().getAllItems().toString());
                    }
                }
        ).start();
    }
}