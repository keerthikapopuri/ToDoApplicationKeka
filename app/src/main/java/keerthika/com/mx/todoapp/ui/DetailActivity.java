package keerthika.com.mx.todoapp.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import keerthika.com.mx.todoapp.R;
import keerthika.com.mx.todoapp.db.DbHandler;
import keerthika.com.mx.todoapp.utils.NotificationUtils;

public class DetailActivity extends AppCompatActivity {


    @BindView(R.id.parentContainer)
    RelativeLayout parentContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nameTask)
    TextInputEditText nameTask;

    @BindView(R.id.descTask)
    TextInputEditText descTask;

    @BindView(R.id.statusSpinner)
    Spinner statusSpinner;

    @BindView(R.id.btnAdd)
    FloatingActionButton btnAdd;

   @BindView(R.id.prioritySpinner)
   Spinner prioritySpinner;

   @BindView(R.id.categoryText)
   EditText categoryText;

   @BindView(R.id.editTextDate)
   EditText dateText;


    private DbHandler db;

    private String idTaskStr = "";
    private String nameTaskStr = "";
    private String descTaskStr = "";
    private String statusTaskStr = "";
    private String priorityTaskStr="";
    private String dateTaskStr="";
    private String categoryTaskStr="";
    private EditText editTextDate;
    private Calendar calendar;
    //private Spinner prioritySpinner;
    private ArrayAdapter<String> priorityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        editTextDate = findViewById(R.id.editTextDate);
        prioritySpinner = findViewById(R.id.prioritySpinner);
        calendar = Calendar.getInstance();
        // Set an onClickListener for the EditText to show the DatePickerDialog
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        db = new DbHandler(this);

    }
    private List<String> getPriorityOptions() {
        return Arrays.asList("Low", "Medium", "High");
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
        setData();
        setupStatusSpinner();
        setupPrioritySpinner();
        events();


    }

    public void getData() {
        Intent intent = getIntent();

        idTaskStr = intent.getStringExtra("idTask");
        nameTaskStr = intent.getStringExtra("nameTask");
        descTaskStr = intent.getStringExtra("descTask");
        statusTaskStr = intent.getStringExtra("statusTask");
        categoryTaskStr = intent.getStringExtra("categoryTask");
        priorityTaskStr = intent.getStringExtra("priorityTask");

        dateTaskStr = intent.getStringExtra("dateTask");

        // Log the values to check if they are correct
        Log.d("DetailActivity", "idTaskStr: " + idTaskStr);
        Log.d("DetailActivity", "nameTaskStr: " + nameTaskStr);
        Log.d("DetailActivity", "descTaskStr: " + descTaskStr);
        Log.d("DetailActivity", "statusTaskStr: " + statusTaskStr);
        Log.d("DetailActivity", "categoryTaskStr: " + categoryTaskStr);
        Log.d("DetailActivity", "dateTaskStr: " + dateTaskStr);
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Handle the selected date
                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        editTextDate.setText(selectedDate);
                    }
                },
                year,
                month,
                day
        );

        // Show DatePickerDialog
        datePickerDialog.show();
    }
    @SuppressLint("SetTextI18n")
    public void setData(){
        toolbar.setTitle(nameTaskStr.toUpperCase());

        nameTask.setText(nameTaskStr);
        descTask.setText(descTaskStr);
        categoryText.setText(categoryTaskStr);
        dateText.setText(dateTaskStr);
    }
    private void setupStatusSpinner() {
        // Assuming you have an array of status options, update it accordingly
        String[] statusOptions = {"New", "Progress", "Completed"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        if (statusTaskStr.equals("Completed")) {
            statusSpinner.setSelection(adapter.getPosition("Completed"));
        } else if (statusTaskStr.equals("New")) {
            statusSpinner.setSelection(adapter.getPosition("New"));
        } else {
            statusSpinner.setSelection(adapter.getPosition("Progress"));
        }


    }
    private void setupPrioritySpinner() {
        // Assuming you have an array of priority options, update it accordingly
        String[] priorityOptions = {"Low", "Medium", "High"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorityOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        // Set the selected priority if it's available
        if (!priorityTaskStr.isEmpty()) {
            prioritySpinner.setSelection(adapter.getPosition(priorityTaskStr));
        }
    }



    public void events(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected status from the spinner
                String statusFinal = statusSpinner.getSelectedItem().toString();

                String newName = nameTask.getText().toString();
                String newDesc = descTask.getText().toString();
               String priority=prioritySpinner.getSelectedItem().toString();
                String category=categoryText.getText().toString();
                String date=editTextDate.getText().toString();
                //Toast.makeText(getApplicationContext(), "Priority: " + priority, Toast.LENGTH_SHORT).show();

                if (newName.trim().length() > 0 && newDesc.trim().length() > 0 && priority.trim().length()>0) {
                    db.updateNota(idTaskStr, newName, newDesc, statusFinal,priority,category,date);
                    new NotificationUtils().showBotification(DetailActivity.this, "Task updated", "Task updated successfully");
                } else {
                    new NotificationUtils().showBotification(DetailActivity.this, "Task not updated", "Your task was not updated as all required fields were not filled.");
                }
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

                alertDeleteItem(idTaskStr);

                return true;


            case android.R.id.home:
                goToBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goToBack();


    }


    public void alertDeleteItem(String nota_id){

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_alert_dialog)
                .setMessage(R.string.message_alert_dialog)
                .setPositiveButton(R.string.button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db.deleteNota(nota_id);
                        new NotificationUtils().showBotification(DetailActivity.this, "Task "+nameTaskStr+" deleted", "The task has been deleted "+nameTaskStr);
                        goToBack();
                    }
                })
                .setNegativeButton(R.string.button_negative, null)
                .setIcon(R.drawable.ic_alert)
                .show();

    }


    public void goToBack(){
        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}
