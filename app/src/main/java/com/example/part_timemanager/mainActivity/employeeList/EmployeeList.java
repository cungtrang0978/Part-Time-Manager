package com.example.part_timemanager.mainActivity.employeeList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.part_timemanager.R;
import com.example.part_timemanager.adapter.employee_adapter;
import com.example.part_timemanager.data.DBManager;
import com.example.part_timemanager.mainActivity.MainActivity;
import com.example.part_timemanager.model.Employee;

import java.util.List;

public class EmployeeList extends AppCompatActivity {

    EditText edtSearchName;
    ImageButton btnSeachName, btnAdd, btnBack, btnTop, btnBottom;
    ListView lv_Employee;
    List<Employee> list_Employee;
    employee_adapter emp_adapter;
    int REQUEST_CODE_LISTVIEW = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        initWidget();
//        MainActivity.dbManager = new DBManager(this);


        list_Employee = MainActivity.dbManager.getAllEmployees();
        setAdapter();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(EmployeeList.this, CreateNewEmployee.class);
                startActivityForResult(intent,REQUEST_CODE_LISTVIEW);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv_Employee.setSelection(emp_adapter.getCount());
            }
        });
        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv_Employee.setSelection(0);
            }
        });

        btnSeachName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.dbManager.existedSearchName(edtSearchName)){
                    list_Employee.clear();
                    list_Employee.addAll(MainActivity.dbManager.getEmployees_bySearchName(edtSearchName));
                    setAdapter();
                }
                else{
                    Toast.makeText(EmployeeList.this, "The name of Employee List does not contain " + '"' + edtSearchName.getText().toString() + '"', Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_LISTVIEW && resultCode ==  RESULT_OK){
            list_Employee.clear();
            list_Employee.addAll(MainActivity.dbManager.getAllEmployees());
            setAdapter();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void setAdapter() {
        if(emp_adapter==null){
            emp_adapter = new employee_adapter(this , R.layout.employee_information, list_Employee);
            lv_Employee.setAdapter(emp_adapter);
        }
        else {
            emp_adapter.notifyDataSetChanged();
            lv_Employee.setSelection(emp_adapter.getCount());
        }
    }

    private void initWidget() {
        edtSearchName = findViewById(R.id.editText_SearchName);
        btnSeachName = findViewById(R.id.imageButton_SearchName);
        btnAdd = findViewById(R.id.imageButton_CreateEmp);
        lv_Employee = findViewById(R.id.listView_Employee);
        btnBack = findViewById(R.id.imageButton_Back);
        btnTop = findViewById(R.id.imageButton_TopList);
        btnBottom = findViewById(R.id.imageButton_BottomList);

    }


}
