package com.example.part_timemanager.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.part_timemanager.R;
import com.example.part_timemanager.model.Employee;

import java.util.List;

public class employee_adapter extends ArrayAdapter<Employee> {
    private  Context context;
    private int resource;
    private List<Employee> employeeList;

    public employee_adapter(@NonNull Context context, int resource, @NonNull List<Employee> objects) {
        super(context, resource, objects);
        this.context =context;
        this.employeeList=objects;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.employee_information, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvId = convertView.findViewById(R.id.textView_Id_Info);
            viewHolder.tvName = convertView.findViewById(R.id.textView_Name_Info);
            viewHolder.tvAddress = convertView.findViewById(R.id.textView_Address_Info);
            viewHolder.tvPhoneNumber = convertView.findViewById(R.id.textView_PhoneNumber_Info);
            viewHolder.imvFirst = convertView.findViewById(R.id.imageView_FirstImage);
            viewHolder.imvSecond = convertView.findViewById(R.id.imageView_SecondImage);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Employee employee = employeeList.get(position);
        viewHolder.tvId.setText(employee.getmId()+"");
        viewHolder.tvName.setText(employee.getmName()+"");
        viewHolder.tvAddress.setText(employee.getmAddress()+"");
        viewHolder.tvPhoneNumber.setText(employee.getmPhoneNumber()+"");

        viewHolder.imvFirst.setImageBitmap(convertByte_Bitmap(employee.getmImage_1()));
        viewHolder.imvSecond.setImageBitmap(convertByte_Bitmap(employee.getmImage_2()));

        return convertView;
    }
    private Bitmap convertByte_Bitmap(byte[] image){
        // chuyển byte[] ->bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        return bitmap;
    }

    public class ViewHolder {
        private TextView tvId;
        private TextView tvName;
        private TextView tvAddress;
        private TextView tvPhoneNumber;
        private ImageView imvFirst;
        private ImageView imvSecond;
    }
}
