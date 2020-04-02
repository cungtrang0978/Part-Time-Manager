package com.example.part_timemanager.mainActivity.partyList;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.part_timemanager.R;
import com.example.part_timemanager.adapter.party_adapter;
import com.example.part_timemanager.mainActivity.MainActivity;
import com.example.part_timemanager.model.Party;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PartyList extends AppCompatActivity {

    ImageButton btnBack, btnCreate, btnSearch, btnExpand, btnFilter;
    EditText edtSearch;
    ListView lvParty;
    List<Party> partyList;
    party_adapter partyAdapter;
    RelativeLayout layoutFilter;
    TextView edtDateFrom;
    TextView edtDateTo;
    int REQUEST_CODE_DATE =1313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);

        initWidget();

        partyList = MainActivity.dbManager.getAllParties();
        setAdapter();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PartyList.this, CreateNewParty.class);
                intent.putExtra("choseDate", MainActivity.getCurrentDate());
                startActivityForResult(intent, REQUEST_CODE_DATE);


            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partyList.clear();
                partyList.addAll(MainActivity.dbManager.getParties_byName(edtSearch.getText().toString()));
                setAdapter();
            }
        });

        if(layoutFilter.getVisibility()==View.VISIBLE){
            btnExpand.setBackgroundResource(R.drawable.icons8_collapse_arrow_32);
        }
        else if(layoutFilter.getVisibility()==View.GONE){
                btnExpand.setBackgroundResource(R.drawable.icons8_expand_arrow_32);
            }
        btnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutFilter.getVisibility()==View.VISIBLE){
                    btnExpand.setBackgroundResource(R.drawable.icons8_expand_arrow_32);
                    layoutFilter.setVisibility(View.GONE);
                }
                else
                if(layoutFilter.getVisibility()==View.GONE){
                    btnExpand.setBackgroundResource(R.drawable.icons8_collapse_arrow_32);
                    layoutFilter.setVisibility(View.VISIBLE);
                }
            }
        });

        edtDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(edtDateFrom);
            }
        });
        edtDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(edtDateTo);
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtDateFrom.getText().toString().equals("") || edtDateTo.getText().toString().equals("")){
                    Toast.makeText(PartyList.this, "You have to fill Date-From and Date-To!", Toast.LENGTH_SHORT).show();
                }
                else{
                    partyList.clear();
                    partyList.addAll(MainActivity.dbManager.getParties_Date_to_Date(edtDateFrom.getText().toString(),edtDateTo.getText().toString()));
                    setAdapter();
                }
            }
        });

    }

    private void initWidget() {
        btnBack = findViewById(R.id.imageButton_Back);
        btnCreate = findViewById(R.id.imageButton_createParty);
        btnSearch = findViewById(R.id.imageButton_SearchName_Party);
        edtSearch = findViewById(R.id.editText_SearchName);
        lvParty = findViewById(R.id.listView_Party);
        btnFilter = findViewById(R.id.imageButton_FilterSeach);
        btnExpand = findViewById(R.id.imageButton_Expand);
        layoutFilter = findViewById(R.id.layout_filter);
        edtDateFrom = findViewById(R.id.editText_DateFrom);
        edtDateTo = findViewById(R.id.editText_DateTo);
    }

    private void setAdapter() {
        if(partyAdapter==null){
            partyAdapter = new party_adapter(this , R.layout.party_information,partyList, true);
            lvParty.setAdapter(partyAdapter);
        }
        else {
            partyAdapter.notifyDataSetChanged();
        }
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

}
