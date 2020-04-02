package com.example.part_timemanager.mainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.part_timemanager.adapter.employee_adapter;
import com.example.part_timemanager.adapter.party_adapter;
import com.example.part_timemanager.data.DBManager;
import com.example.part_timemanager.mainActivity.employeeList.CreateNewEmployee;
import com.example.part_timemanager.mainActivity.employeeList.EmployeeList;
import com.example.part_timemanager.mainActivity.partyList.CreateNewParty;
import com.example.part_timemanager.R;
import com.example.part_timemanager.mainActivity.partyList.PartyList;
import com.example.part_timemanager.mainActivity.salary.SalaryDeclaration;
import com.example.part_timemanager.model.Party;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    ListView lvParty;
    public  static CalendarView calendarView;
    String choseDate;
    ImageButton  btnMenu;
    public static DBManager dbManager;
    private int REQUEST_CODE_LISTVIEW = 123;
    List<Party> partyList;
    party_adapter partyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);
        initWidget();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu();
            }
        });

//        Date date = new Date();
//        date.setTime(calendarView.getDate());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        choseDate = dateFormat.format(calendarView.getDate());
        choseDate = getCurrentDate();

        partyList =  dbManager.getParties_byDate(choseDate);
        setAdapter();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String stringMonth = Integer.toString(month+1),
                        stringDay =Integer.toString(dayOfMonth);

                if (stringMonth.length()<2){
                    stringMonth = "0" + stringMonth;
                }
                if (stringDay.length()<2){
                    stringDay = "0" + stringDay;
                }
                choseDate = stringDay + "/" + stringMonth + "/" + year;

                partyList.clear();
                if (dbManager.existedPartyDate(choseDate)) {
                    partyList.addAll(dbManager.getParties_byDate(choseDate));
                }
                setAdapter();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this,  CreateNewParty.class);
                intent.putExtra("choseDate", choseDate);
                startActivityForResult(intent, REQUEST_CODE_LISTVIEW);
            }
        });
    }

    public  static String convertDate_String(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        String stringDay = Integer.toString(dayOfMonth);
        String stringMonth = Integer.toString(month);

        if (stringMonth.length()<2){
            stringMonth = "0" + stringMonth;
        }
        if (stringDay.length()<2){
            stringDay = "0" + stringDay;
        }
        return stringDay +"/"+ stringMonth +"/"+ year;
    }

    public static String getCurrentDate() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        String stringDay = Integer.toString(dayOfMonth);
        String stringMonth = Integer.toString(month);

        if (stringMonth.length()<2){
            stringMonth = "0" + stringMonth;
        }
        if (stringDay.length()<2){
            stringDay = "0" + stringDay;
        }
        return stringDay +"/"+ stringMonth +"/"+ year;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_LISTVIEW && resultCode ==  RESULT_OK && data.getStringExtra("dateString").equals(choseDate)){

            partyList.clear();
//            partyList = dbManager.getParties_byDate(data.getStringExtra("dateString"));
            partyList.addAll(dbManager.getParties_byDate(data.getStringExtra("dateString")));
            setAdapter();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initWidget() {
        btnAdd = findViewById(R.id.button_addParty);
        lvParty = findViewById(R.id.listView_Party);
        calendarView = findViewById(R.id.calenderView);
        btnMenu = findViewById(R.id.imageButton_Menu);
    }

    private void ShowMenu(){
        final PopupMenu popupMenu = new PopupMenu(this, btnMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_demo,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch(item.getItemId()){
                    case R.id.menuExportSalary:
                        intent = new Intent(MainActivity.this, SalaryDeclaration.class);
                        startActivity(intent);
                        break;
                    case R.id.menuEmployeeList:
                        intent = new Intent(MainActivity.this, EmployeeList.class);
                        startActivity(intent);
                        break;
                    case R.id.menuPartyList:
                        intent = new Intent(MainActivity.this, PartyList.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    private void setAdapter() {
        if(partyAdapter==null){
            partyAdapter = new party_adapter(this , R.layout.party_information,partyList);
            lvParty.setAdapter(partyAdapter);
        }
        else {
            partyAdapter.notifyDataSetChanged();
        }
    }

//    private void setAdapter(boolean setLastSelection) {
//        if(partyAdapter==null){
//            partyAdapter = new party_adapter(this , R.layout.party_information,partyList);
//            lvParty.setAdapter(partyAdapter);
//        }
//        else {
//            partyAdapter.notifyDataSetChanged();
//            lvParty.setSelection(partyAdapter.getCount());
//        }
//    }
}
