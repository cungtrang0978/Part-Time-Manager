package com.example.part_timemanager.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.part_timemanager.R;
import com.example.part_timemanager.model.Employee;

import java.util.List;

public class part_timer_adapter extends ArrayAdapter<Employee> {
    private Context context;
    private int resource;
    private List<Employee> employeeList;
    private boolean invisibleImage = false;

    public part_timer_adapter(@NonNull Context context, int resource, @NonNull List<Employee> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.employeeList = objects;
    }
    public part_timer_adapter(@NonNull Context context, int resource, @NonNull List<Employee> objects, boolean invisibleImage) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.employeeList = objects;
        this.invisibleImage = invisibleImage;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.part_timer_information,parent, false);
            viewHolder  =  new ViewHolder();
            viewHolder.tvId = convertView.findViewById(R.id.textView_Id);
            viewHolder.tvName = convertView.findViewById(R.id.textView_Name);
            viewHolder.img = convertView.findViewById(R.id.imageView_FirstImage);
            viewHolder.btnDelete = convertView.findViewById(R.id.imageButton_Delete);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Employee employee = employeeList.get(position);
        viewHolder.tvId.setText(employee.getmId()+"");
        viewHolder.tvName.setText(employee.getmName());
        if (invisibleImage){
            viewHolder.img.setVisibility(View.INVISIBLE);
            viewHolder.btnDelete.setVisibility(View.VISIBLE);
            /*viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    employeeList.remove(position);
                    notifyDataSetChanged();
                }
            });*/
        }
        else{
            viewHolder.img.setVisibility(View.VISIBLE);
            viewHolder.img.setImageBitmap(convertByte_Bitmap(employee.getmImage_1()));
            viewHolder.btnDelete.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class ViewHolder{
        private TextView tvId;
        private TextView tvName;
        private ImageView img;
        private ImageButton btnDelete;
    }

    private Bitmap convertByte_Bitmap(byte[] image){
        // chuyển byte[] ->bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        return bitmap;
    }
}
