package com.example.part_timemanager.mainActivity.partyList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.TextView;

import com.example.part_timemanager.R;
import com.example.part_timemanager.adapter.part_timer_adapter;
import com.example.part_timemanager.data.DBManager;
import com.example.part_timemanager.mainActivity.MainActivity;
import com.example.part_timemanager.model.Employee;
import com.example.part_timemanager.model.Party;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;

public class CreateNewParty extends AppCompatActivity {

    Button btnOnOff, btnClearAll;
    ImageButton btnCancel, btnSave, btnAdd_PartTimer;
    AutoCompleteTextView edtFloor, edtHourlyWage;
    TextView edtTimeBegin, edtTimeEnd, edtDate , label;
    EditText edtPartyName, edtLoca, edtNum;
    Switch switchNameParty;
    GridView gridView_AddPartTimer;
    part_timer_adapter partTimerAdapter, partTimerAdapter_dialog;
    List<Employee> employeeList, employeeList_dialog;
    int[] added_idEmp = new int[100];
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_party);
        
        initWidget();
        setAutoCompleteView();
//        MainActivity.dbManager =  new DBManager(this);

        String name = edtFloor.getText().toString();
        //getSupportActionBar().setTitle("CreateNewParty");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedAllFilled()){
                    Party party = createParty();
                    if(party!=null && party.getmNumOfEmps() >= count){
                        if(party.getmTimeEnd()==null || party.getmTimeEnd() > party.getmTimeBegin()){
                            MainActivity.dbManager.addParty(party);
                            for (int i=0; i<count; i++){
                                MainActivity.dbManager.addEmployee_Party(employeeList.get(i).getmId(), MainActivity.dbManager.getId_Last_Added_Party());
                            }
                            Intent intent = new Intent();
                            intent.putExtra("dateString", edtDate.getText().toString());
                            setResult(RESULT_OK, intent);
                            finish();
                        }   
                        else{
                            if(party.getmTimeEnd() <= party.getmTimeBegin()){
                                AlertDialog_Time();
                            }
                        }
                    }
                    else if(party.getmNumOfEmps() < count){
                        int newNumber = party.getmNumOfEmps();
                        AlertDialog_Number(newNumber, count);
                    }
                }
            }
        });
        edtPartyName.setText(name);

        Intent choseDate = getIntent();
        edtDate.setText(choseDate.getStringExtra("choseDate"));

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(edtDate);
            }
        });

        edtFloor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!switchNameParty.isChecked()){
                    String name = edtFloor.getText().toString();
                    edtPartyName.setText(name);
                    v.setTag(edtPartyName);
                }
            }
        });

        edtPartyName.setEnabled(false);

        switchNameParty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edtPartyName.setEnabled(true);
                }
                else{
                    edtPartyName.setEnabled(false);
                    String name = edtFloor.getText()+"";
                    edtPartyName.setText(name);
                    edtFloor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            String name = edtFloor.getText()+"";
                            edtPartyName.setText(name);
                            v.setTag(edtPartyName);
                        }
                    });
                }
            }
        });

        edtTimeBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime(edtTimeBegin);
            }
        });

        edtTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime(edtTimeEnd);
            }
        });
        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gridView_AddPartTimer.getVisibility()==View.VISIBLE){
                    gridView_AddPartTimer.setVisibility(View.INVISIBLE);
                    btnAdd_PartTimer.setVisibility(View.INVISIBLE);
                    btnClearAll.setVisibility(View.INVISIBLE);
                }
                else{
                    if(!edtNum.getText().toString().equals("")){
                        gridView_AddPartTimer.setVisibility(View.VISIBLE);
                        btnAdd_PartTimer.setVisibility(View.VISIBLE);
                        btnClearAll.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(CreateNewParty.this, "You have to fill the Number of Part-Timer firstly", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        employeeList = MainActivity.dbManager.getEmployees_addedPartTimer(added_idEmp, count);

        btnAdd_PartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count < Integer.parseInt(edtNum.getText().toString())){
                    Dialog_Add_PartTimer();
                }
                else {
                    Toast.makeText(CreateNewParty.this, "Part-timers's party has been enough (" +count+ "/" + edtNum.getText().toString()+ ")\nYou can't add anymore.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                added_idEmp = new int[100];
                count=0;
                employeeList.clear();
                setAdapter(gridView_AddPartTimer);
            }
        });



        /*edtNum.addTextChangedListener(new TextWatcher() {
            public int _count = count;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!edtNum.getText().toString().equals("")){
                    int newNumber = Integer.parseInt(edtNum.getText().toString());
                    if(_count > newNumber){
                        Toast.makeText(CreateNewParty.this, "The number of added Part-timer"+ count +
                                " is bigger than the required number"+ newNumber +"\nYou have to edit the required number!!!", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    private Party createParty() {
        Party party;

        String name = edtPartyName.getText().toString().trim();
        String date = edtDate.getText().toString();
        Float timeBegin = convertTime60_To100(edtTimeBegin.getText().toString().trim());
        Float timeEnd;
        String floor = edtFloor.getText().toString().trim();
        String location = edtLoca.getText().toString().trim();
        Integer hourlyWage = Integer.parseInt(edtHourlyWage.getText().toString().trim());
        Integer number_Emps = Integer.parseInt(edtNum.getText().toString().trim());

        if (edtTimeEnd.getText().toString().equals("")){
            party = new Party(name, date, timeBegin, floor, location, hourlyWage, number_Emps);
        }
        else{
            timeEnd = convertTime60_To100(edtTimeEnd.getText().toString().trim());
            party = new Party(name, date, timeBegin, timeEnd, floor, location, hourlyWage, number_Emps);
        }
        return party;
    }

    private void initWidget() {
        btnCancel =             findViewById(R.id.button_Cancel);
        btnSave =               findViewById(R.id.button_Create);
        edtTimeBegin =          findViewById(R.id.editText_TimeBegin);
        edtTimeEnd =            findViewById(R.id.editText_TimeEnd);
        edtFloor =              findViewById(R.id.editText_Floor);
        edtHourlyWage =         findViewById(R.id.editText_HourlyWage);
        edtPartyName =          findViewById(R.id.editText_partyName);
        edtDate =               findViewById(R.id.editText_Date);
        edtLoca =               findViewById(R.id.editText_Location);
        edtNum =                findViewById(R.id.editText_NumberOfEmps);
        switchNameParty =       findViewById(R.id.switch_partyName);
        gridView_AddPartTimer = findViewById(R.id.gridView_add_PartTimer);
        btnAdd_PartTimer =      findViewById(R.id.imageButton_add_PartTimer);
        label =                 findViewById(R.id.textView_Label);
        btnOnOff =              findViewById(R.id.button_OnOff_AddPartTimer);
        btnClearAll =           findViewById(R.id.button_ClearAllAdded);
    }

    private void setAutoCompleteView(){
        String[] list_floor = getResources().getStringArray(R.array.floor),
                list_hourlyWage = getResources().getStringArray(R.array.hourlyWage);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list_floor);
        edtFloor.setAdapter(adapter);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list_hourlyWage);
        edtHourlyWage.setAdapter(adapter);
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

    private void selectTime(final TextView edtext){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(0, 0, 0, hourOfDay, minute);
                edtext.setText(simpleDateFormat.format(calendar.getTime()));

                String timeend = edtTimeEnd.getText().toString();
                if (timeend.startsWith("00")){
                    edtTimeEnd.setText(String.valueOf(24).concat(timeend.substring(2)));
                }
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void Dialog_Add_PartTimer(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_parttimer);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //set No Tittle
        dialog.setCanceledOnTouchOutside(false); // set not touch outside

        //Set Display dialog.
/*        DisplayMetrics metrics = getResources().getDisplayMetrics();(
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);*/

        //initWidget
        ListView lvPartTimer = dialog.findViewById(R.id.listView_Add_PartTimer);
        ImageButton btnCancel_Dialog = dialog.findViewById(R.id.imageButton_Cancel);

        employeeList_dialog = MainActivity.dbManager.getAllEmployees();
        setAdapter(lvPartTimer);

        lvPartTimer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean overlap = false;

                for (int i = 0; i<= count; i++){
                    if (employeeList_dialog.get(position).getmId() == added_idEmp[i]){
                        overlap =true;
                        dialog.cancel();
                        break;
                    }
                }
                if (!overlap){
                    added_idEmp[count++] = employeeList_dialog.get(position).getmId();
//                employeeList_dialog
                    dialog.cancel();
                    employeeList.clear();

                    employeeList.addAll(MainActivity.dbManager.getEmployees_addedPartTimer(added_idEmp, count));
                    setAdapter(gridView_AddPartTimer);
                    registerForContextMenu(gridView_AddPartTimer);
                }
            }
        });
        btnCancel_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show(); //this is the last, and it's very important. Dont forget it
        partTimerAdapter_dialog = null;
    }

    public static String convertTime100_To60(float time ){
        String timeString  =  Float.toString(time);
        BigDecimal timeBD = new BigDecimal(timeString);
        String hhStr = timeString.split("\\.")[0];
        BigDecimal output = new BigDecimal(Float.toString(Integer.parseInt(hhStr)));
        output = output.add((timeBD.subtract(output).multiply(BigDecimal.valueOf(60))).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_EVEN));
        return output.toString().replace(".",":");
    }

    public static float convertTime60_To100(String time)
    {
        time = time.replace(":",".");
        BigDecimal inputBD = new BigDecimal(time);
        String hhStr = time.split("\\.")[0];
        BigDecimal output = new BigDecimal(Float.toString(Integer.parseInt(hhStr)));
        output = output.add((inputBD.subtract(output).divide(BigDecimal.valueOf(60), 10, BigDecimal.ROUND_HALF_EVEN)).multiply(BigDecimal.valueOf(100)));
        return Float.parseFloat(output.toString());
    }
    private void setAdapter(GridView gridView) {
        if(partTimerAdapter==null){
            partTimerAdapter = new part_timer_adapter(this , R.layout.part_timer_information, employeeList, true);
            gridView.setAdapter(partTimerAdapter);
        }
        else {
            partTimerAdapter.notifyDataSetChanged();
        }
    }
    private void setAdapter(ListView listView){
        if(partTimerAdapter_dialog==null){
            partTimerAdapter_dialog = new part_timer_adapter(this , R.layout.part_timer_information, employeeList_dialog);
            listView.setAdapter(partTimerAdapter_dialog);
        }
        else {
            partTimerAdapter_dialog.notifyDataSetChanged();
        }
    }
    private boolean checkedAllFilled(){
        boolean filled = true; //checked edtext has filled or not_filled
        String alert ="You have to fill: \n";

        if(edtDate.getText().toString().equals("")){
            alert += "Date\n";
            filled = false;
        }
        if(edtTimeBegin.getText().toString().equals("")){
            alert += "Begin Time\n";
            filled= false;
        }
        if(edtFloor.getText().toString().equals("")){
            alert += "Floor\n";
            filled= false;
        }
        if(edtHourlyWage.getText().toString().equals("")){
            alert += "Hourly Wage\n";
            filled= false;
        }
        if(edtNum.getText().toString().equals("")){
            alert += "Number of Part-timer\n";
            filled= false;
        }
        if (!filled){
            Toast.makeText(this, alert, Toast.LENGTH_LONG).show();
        }
        return filled;
    }

    private void AlertDialog_Time(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Alert!!!");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("The end time must be greater than the begin time!");
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
    private void AlertDialog_Number(int newNumber, int count){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Alert!!!");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("The number of added Part-timer ("+ count +
                ") must be smaller than the required number ("+ newNumber +")");
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v.getId()==R.id.gridView_add_PartTimer){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_added_part_timer, menu);
            menu.setHeaderTitle("Select Action");
        }

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo infor = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.option_delete:
                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);

        }

    }
}
