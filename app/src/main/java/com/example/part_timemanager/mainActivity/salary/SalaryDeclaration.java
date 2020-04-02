package com.example.part_timemanager.mainActivity.salary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.example.part_timemanager.R;
import com.example.part_timemanager.adapter.salary_adapter;
import com.example.part_timemanager.mainActivity.MainActivity;
import com.example.part_timemanager.model.WorkedDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SalaryDeclaration extends AppCompatActivity {

    AutoCompleteTextView edtName;
    TextView txtDateFrom, txtDateTo;
    ImageView imgDown;
    ImageButton btnCancel, btnExport, btnClearName, btnClearDateBegin, btnClearDateLast;
    List<String> nameList;
    ExpandableListView lvSalary;
    TextView txtSum;
    List<String> expandableListTitle;
    HashMap<String, List<WorkedDate>> expandableListDetail;
    salary_adapter salaryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_declaration);

        initWidget();
        setAutoCompleteTextView();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(txtDateFrom);
            }
        });
        txtDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(txtDateTo);
            }
        });
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!alert_EmptyEditText()){
                    setExpandableListView();
                    int sum;
                    if(!txtDateTo.getText().toString().equals("")){
                        sum = MainActivity.dbManager.SumSalary(edtName.getText().toString(), txtDateFrom.getText().toString() , txtDateTo.getText().toString());
                    }
                    else{
                        sum = MainActivity.dbManager.SumSalary(edtName.getText().toString(), txtDateFrom.getText().toString() , txtDateFrom.getText().toString());
                    }

                    txtSum.setText(String.valueOf(sum));
                }
            }
        });

        btnClearName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtName.setText("");
            }
        });
        btnClearDateBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDateFrom.setText("");
            }
        });
        btnClearDateLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDateTo.setText("");
            }
        });
    }

    private boolean alert_EmptyEditText(){
        String alert ="";
        if(edtName.getText().toString().equals("")){
            alert+= "Name";
        }
        if(txtDateFrom.getText().toString().equals("")){
            alert += ", Date";
        }
        if(!alert.equals("")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Alert!!!");
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.setMessage(alert + " is/are still emptied!!!");
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            return true;
        }
        return false;
    }
    private void initWidget(){
        edtName = findViewById(R.id.multiAutoCompTextView_Name);
        txtDateFrom = findViewById(R.id.textView_DateFrom);
        txtDateTo = findViewById(R.id.textView_DateTo);
        imgDown = findViewById(R.id.imageView_Down);
        btnCancel = findViewById(R.id.button_Cancel);
        txtSum = findViewById(R.id.textView_SumSalary);
        lvSalary = findViewById(R.id.expandListView_Salary);
        btnExport =findViewById(R.id.imageButton_Export);
        btnClearName = findViewById(R.id.imageButton_ClearName);
        btnClearDateBegin = findViewById(R.id.imageButton_ClearDateFrom);
        btnClearDateLast = findViewById(R.id.imageButton_ClearDateTo);
    }

    private void setAutoCompleteTextView(){
        nameList = MainActivity.dbManager.getAllNameEmployee();
        nameList.add("All");
        String[] nameArray =nameList.toArray(new String[MainActivity.dbManager.countAllEmployee()+1]);

        ArrayAdapter<String> adapter  = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameArray);
        edtName.setAdapter(adapter);
    }
    private void selectDate(final TextView txtDate){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog =  new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                txtDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void setExpandableListView(){
        String name = edtName.getText().toString(),
                dateFrom = txtDateFrom.getText().toString(),
                dateTo = txtDateTo.getText().toString();


        if(!dateFrom.equals("")){
            if(!dateTo.equals("")){
                expandableListDetail = MainActivity.dbManager.getEmployeeWorkedOnDate(name, dateFrom, dateTo);
            }
            else{
                expandableListDetail = MainActivity.dbManager.getEmployeeWorkedOnDate(name, dateFrom, null);
            }
            expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
            salaryAdapter = new salary_adapter(this, expandableListTitle, expandableListDetail);
            lvSalary.setAdapter(salaryAdapter);
        }

    }
}
