package keerthika.com.mx.todoapp.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

import butterknife.BindView;
import butterknife.ButterKnife;
import keerthika.com.mx.todoapp.R;
import keerthika.com.mx.todoapp.adapter.NoteAdapter;
import keerthika.com.mx.todoapp.db.DbHandler;
import keerthika.com.mx.todoapp.model.Nota;
import keerthika.com.mx.todoapp.utils.NotificationUtils;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemClickListener, NoteAdapter.ItemLongClickListener {


    @BindView(R.id.parentContainer)
    RelativeLayout parentContainer;

    @BindView(R.id.btnAdd)
    FloatingActionButton btnAdd;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private DbHandler db;
    private ArrayList<Nota> notas;
    private NoteAdapter noteAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        db = new DbHandler(this);


    }


    @Override
    protected void onStart() {
        super.onStart();

        events();
        setData();
    }

    public void setData(){
        notas = db.getAllNotas();
        noteAdapter = new NoteAdapter(this, notas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        noteAdapter.setClickListener(this);
        noteAdapter.setLongClickListener(this);
        noteAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(noteAdapter);
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.item_selected, getCategoryOptions());

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Set a listener for item selection on the Spinner
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle task filtering based on the selected category
                filterTasksByCategory(categoryAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

    }


    public void events(){

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete:
                    deleteAllData();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private List<String> getCategoryOptions() {
        // Retrieve distinct categories from your database
        List<String> categories = db.getDistinctCategories();
        categories.add(0, "All"); // Add an "All" option at the beginning
        return categories;
    }

    private void filterTasksByCategory(String selectedCategory) {
        if ("All".equals(selectedCategory)) {
            // Display all tasks
            noteAdapter = new NoteAdapter(MainActivity.this, db.getAllNotas());
        } else {
            // Display tasks based on the selected category
            noteAdapter = new NoteAdapter(MainActivity.this, db.getNotasByCategory(selectedCategory));
        }

        // Update the RecyclerView adapter
        recyclerView.swapAdapter(noteAdapter, true);
    }

    public void deleteAllData(){

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_alert_dialog_all)
                .setMessage(R.string.message_alert_dialog_all)
                .setPositiveButton(R.string.button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db.deleteAllNotas();
                        notas = db.getAllNotas();
                        noteAdapter = new NoteAdapter(MainActivity.this, notas);
                        recyclerView.swapAdapter(noteAdapter, true);
                        new NotificationUtils().showBotification(MainActivity.this, "Deleted tasks", "All tasks have been successfully deleted");


                    }
                })
                .setNegativeButton(R.string.button_negative, null)
                .setIcon(R.drawable.ic_alert)
                .show();



    }


    @Override
    public void onItemClick(View view, int position) {
        Nota nota = notas.get(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("idTask", nota.getId());
        intent.putExtra("nameTask", nota.getName());
        intent.putExtra("descTask", nota.getDescription());
        intent.putExtra("statusTask", nota.getStatus());
        intent.putExtra("categoryTask", nota.getCategory());
        intent.putExtra("dateTask", nota.getDate());
        intent.putExtra("priorityTask",nota.getPriority());

        startActivity(intent);
        finish();

    }

    @Override
    public void onItemLongClick(View view, int position) {
        alertDeleteItem(notas.get(position), position);
    }

    public void alertDeleteItem(Nota nota, int position){

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_alert_dialog)
                .setMessage(R.string.message_alert_dialog)
                .setPositiveButton(R.string.button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db.deleteNota(nota.getId());
                        notas.remove(position);
                        noteAdapter = new NoteAdapter(MainActivity.this, notas);
                        recyclerView.swapAdapter(noteAdapter, true);
                        new NotificationUtils().showBotification(MainActivity.this, "Task "+nota.getName()+"deleted", "The task "+nota.getName()+" has been succesfully deleted");


                    }
                })
                .setNegativeButton(R.string.button_negative, null)
                .setIcon(R.drawable.ic_alert)
                .show();

    }


}

