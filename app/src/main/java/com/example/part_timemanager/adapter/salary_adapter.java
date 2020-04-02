package com.example.part_timemanager.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.part_timemanager.R;
import com.example.part_timemanager.mainActivity.MainActivity;
import com.example.part_timemanager.model.WorkedDate;

import java.util.HashMap;
import java.util.List;

public class salary_adapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<WorkedDate>> expandableListDetail;

    public salary_adapter(Context context, List<String> expandableListTitle, HashMap<String, List<WorkedDate>> expandableListDetail){
        this.context = context;
        this.expandableListTitle=expandableListTitle;
        this.expandableListDetail=expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.textView_TitleGroup);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final WorkedDate workedDate = (WorkedDate) getChild(groupPosition, childPosition);
        ViewHolder viewHolder;
        if (convertView == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.row_worked_date, null);
            convertView = LayoutInflater.from(context).inflate(R.layout.row_worked_date, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvDate = convertView.findViewById(R.id.textView_Date);
            viewHolder.tvTimeBegin = convertView.findViewById(R.id.textView_TimeBegin);
            viewHolder.tvTimeEnd = convertView.findViewById(R.id.textView_TimeEnd);
            viewHolder.tvSalary = convertView.findViewById(R.id.textView_Salary);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tvDate.setText(workedDate.getmDate());
        viewHolder.tvTimeBegin.setText(Float.toString(workedDate.getmTimeBegin()));
        viewHolder.tvTimeEnd.setText(Float.toString(workedDate.getmTimeEnd()));
        viewHolder.tvSalary.setText(Integer.toString(workedDate.getSalary()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ViewHolder{
        private TextView tvDate;
        private TextView tvTimeBegin;
        private TextView tvTimeEnd;
        private TextView tvSalary;
    }
}
