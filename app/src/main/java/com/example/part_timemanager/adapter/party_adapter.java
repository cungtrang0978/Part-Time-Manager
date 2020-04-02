package com.example.part_timemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.part_timemanager.R;
import com.example.part_timemanager.mainActivity.MainActivity;
import com.example.part_timemanager.model.Party;

import java.math.BigDecimal;
import java.util.List;

public class party_adapter extends ArrayAdapter<Party> {

    private  Context context;
    private int resource;
    private List<Party> partyList;
    private boolean viewDate;

    public party_adapter(@NonNull Context context, int resource, @NonNull List<Party> objects) {
        super(context, resource, objects);
        this.context =context;
        this.resource =resource;
        this.partyList =objects;
    }

    public party_adapter(@NonNull Context context, int resource, @NonNull List<Party> objects, boolean viewDate) {
        super(context, resource, objects);
        this.context =context;
        this.resource =resource;
        this.partyList =objects;
        this.viewDate = viewDate;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.party_information,parent,false);
            viewHolder  = new ViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.textView_Name_partyInfo);
            viewHolder.tvDate = convertView.findViewById(R.id.textView_Date_partyInfo);
            viewHolder.tvTimeBegin = convertView.findViewById(R.id.textView_TimeBegin_partyInfo);
            viewHolder.tvTimeEnd = convertView.findViewById(R.id.textView_TimeEnd_partyInfo);
            viewHolder.tvFloor = convertView.findViewById(R.id.textView_Floor_partyInfo);
            viewHolder.tvLocation = convertView.findViewById(R.id.textView_Location_partyInfo);
            viewHolder.tvHourlyWage = convertView. findViewById(R.id.textView_HourlyWage_partyInfo);
            viewHolder.tvNumEmps = convertView.findViewById(R.id.textView_NumOfEmps_partyInfo);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Party party = partyList.get(position);
        viewHolder.tvName.setText(party.getmName());
        viewHolder.tvDate.setText(party.getmDate());
        viewHolder.tvTimeBegin.setText(convertTime100_To60(party.getmTimeBegin()));

        if(party.getmTimeEnd()!=0){
            viewHolder.tvTimeEnd.setText(" - " + convertTime100_To60(party.getmTimeEnd()));
        }
        else{
            viewHolder.tvTimeEnd.setText(" - ?");
        }

        viewHolder.tvFloor.setText("On: " + party.getmFloor());
        viewHolder.tvLocation.setText(party.getmLocation());
        viewHolder.tvHourlyWage.setText(String.format("%,d", party.getmHourlyWage()) +"/h");

        String numberEmp = "(" + MainActivity.dbManager.countRow_existedPartTimersOfParty(party.getmId()) + "/" + party.getmNumOfEmps() + ')';
        viewHolder.tvNumEmps.setText(numberEmp);

        if (viewDate){
            viewHolder.tvDate.setVisibility(View.VISIBLE);
        }
        return convertView;
    }


    public class ViewHolder{
        private TextView tvName;
        private TextView tvDate;
        private TextView tvTimeBegin;
        private TextView tvTimeEnd;
        private TextView tvFloor;
        private TextView tvLocation;
        private TextView tvHourlyWage;
        private TextView tvNumEmps;

    }
    public static String convertTime100_To60(Float time ){
        String timeString  =  Float.toString(time);
        BigDecimal timeBD = new BigDecimal(timeString);
        String hhStr = timeString.split("\\.")[0];
        BigDecimal output = new BigDecimal(Float.toString(Integer.parseInt(hhStr)));
        output = output.add((timeBD.subtract(output).multiply(BigDecimal.valueOf(60))).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_EVEN));

        return output.toString().replace(".",":");
    }


}
