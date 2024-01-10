package keerthika.com.mx.todoapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import keerthika.com.mx.todoapp.R;
import keerthika.com.mx.todoapp.db.DbHandler;
import keerthika.com.mx.todoapp.utils.NotificationUtils;

public class CreateNoteActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nameTask)
    TextInputEditText nameTask;

    @BindView(R.id.descTask)
    TextInputEditText descTask;

    @BindView(R.id.actionBtn)
    FloatingActionButton btnAdd;

    @BindView(R.id.categoryText)
    EditText categoryText;

    @BindView(R.id.prioritySpinner)
    Spinner prioritySpinner;

    @BindView(R.id.editTextDate)
    EditText editTextDate;

    private DbHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        db = new DbHandler(this);

        // Set up the priority spinner
        setupPrioritySpinner();

        // Set an onClickListener for the EditText to show the DatePickerDialog
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
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

    private void setupPrioritySpinner() {
        // Assuming you have an array of priority options, update it accordingly
        String[] priorityOptions = {"Low", "Medium", "High"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorityOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
    }

    private void saveNote() {
        // Retrieve values from the UI
        String name = nameTask.getText().toString().trim();
        String desc = descTask.getText().toString().trim();
        String category = categoryText.getText().toString().trim();
        String priority = prioritySpinner.getSelectedItem().toString();
        String date = editTextDate.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(category) || TextUtils.isEmpty(priority) || TextUtils.isEmpty(date)) {
            // Handle invalid input
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert the date to a format suitable for your database
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date());

        // Perform database insertion
        db.insertNotas(name, desc,priority, category, formattedDate);

        Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show();

        // Optional: You can clear the input fields after saving
        nameTask.setText("");
        descTask.setText("");
        categoryText.setText("");
        prioritySpinner.setSelection(0); // Assuming "Low" is the default priority
        editTextDate.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        events();
    }

    public void events() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.editTextDate:
                        showDatePickerDialog();
                        break;

                    case R.id.actionBtn:
                        saveNote();
                        goToBack();
                        break;
                }
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            goToBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goToBack();
    }

    public void goToBack() {
        Intent intent = new Intent(CreateNoteActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
