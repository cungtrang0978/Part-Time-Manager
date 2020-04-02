package com.example.part_timemanager.mainActivity.employeeList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.part_timemanager.R;
import com.example.part_timemanager.adapter.employee_adapter;
import com.example.part_timemanager.data.DBManager;
import com.example.part_timemanager.mainActivity.MainActivity;
import com.example.part_timemanager.model.Employee;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateNewEmployee extends AppCompatActivity {
    private ImageButton btnCancel, btnSave, btnCamera, btnGallery;
    private EditText edtName, edtPhone, edtAddress;
    private ImageView imvFirst, imvSecond;
    private int REQUEST_CODE_CAMERA_FIRST_IMAGE =111;
    private int REQUEST_CODE_CAMERA_SECOND_IMAGE =222;
    private int REQUEST_CODE_GALLERY_FIRST_IMAGE = 333;
    private int REQUEST_CODE_GALLERY_SECOND_IMAGE = 444;
    private int REQUEST_CODE_CAMERA;
    private int REQUEST_CODE_GALLERY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_employee);

        initWidget();

//        MainActivity.dbManager = new DBManager(this);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Employee employee = createEmployee();
                if(employee!=null){
                    MainActivity.dbManager.addEmployee(employee);
                }

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        imvFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCamera.setVisibility(View.VISIBLE);
                btnGallery.setVisibility(View.VISIBLE);
                REQUEST_CODE_CAMERA  = REQUEST_CODE_CAMERA_FIRST_IMAGE;
                REQUEST_CODE_GALLERY = REQUEST_CODE_GALLERY_FIRST_IMAGE;
            }

        });

        imvSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCamera.setVisibility(View.VISIBLE);
                btnGallery.setVisibility(View.VISIBLE);
                REQUEST_CODE_CAMERA  = REQUEST_CODE_CAMERA_SECOND_IMAGE;
                REQUEST_CODE_GALLERY = REQUEST_CODE_GALLERY_SECOND_IMAGE;
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCamera.setVisibility(View.INVISIBLE);
                btnGallery.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);


            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCamera.setVisibility(View.INVISIBLE);
                btnGallery.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });

    }

    private Employee createEmployee() {

        String name = edtName.getText()+"".trim();
        String address =edtAddress.getText()+"".trim();
        String phoneNumber = edtPhone.getText()+"".trim();
        byte[] image1 =convertBitmap_byte(imvFirst);
        byte[] image2 =convertBitmap_byte(imvSecond);

        Employee employee = new Employee(name,address,phoneNumber,image1, image2);


        return employee;
    }
    private byte[] convertBitmap_byte(ImageView imageView){

        //Chuyển data imageView -> byte[]
        byte[] image;
        BitmapDrawable bitmapDrawable = (BitmapDrawable)    imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        int widthBitmap =bitmap.getWidth(), heightBitmap = bitmap.getHeight();
        int scaleWidth = 450;
        int scaleHeight = scaleWidth  * heightBitmap / widthBitmap;

        bitmap = Bitmap.createScaledBitmap(bitmap, scaleWidth,scaleHeight,true);
        ByteArrayOutputStream byteArray =  new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,10, byteArray);

        image  = byteArray.toByteArray();
        return image;

    }
    private void initWidget() {
        btnCancel = findViewById(R.id.button_Cancel);
        btnSave = findViewById(R.id.button_Create);
        edtName = findViewById(R.id.editText_Name);
        edtPhone = findViewById(R.id.editText_PhoneNumber);
        edtAddress = findViewById(R.id.editText_Address);
        imvFirst = findViewById(R.id.imageView_FirstImage);
        imvSecond = findViewById(R.id.imageView_SecondImage);
        btnCamera = findViewById(R.id.imageButton_Camera);
        btnGallery = findViewById(R.id.imageButton_Gallery);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && data!= null) {

            if(requestCode==REQUEST_CODE_CAMERA_FIRST_IMAGE){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imvFirst.setImageBitmap(bitmap);
            }

            else if(requestCode==REQUEST_CODE_CAMERA_SECOND_IMAGE){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imvSecond.setImageBitmap(bitmap);
            }

            else {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);

                    if (requestCode == REQUEST_CODE_GALLERY_FIRST_IMAGE) {
                        Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream);
                        imvFirst.setImageBitmap(bitmap1);
                    }
                    if (requestCode == REQUEST_CODE_GALLERY_SECOND_IMAGE) {
                        Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream);
                        imvSecond.setImageBitmap(bitmap1);
                    }
                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
