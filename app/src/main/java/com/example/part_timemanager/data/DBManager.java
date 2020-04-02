package com.example.part_timemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.part_timemanager.model.Employee;
import com.example.part_timemanager.model.Party;
import com.example.part_timemanager.model.WorkedDate;

import java.sql.ResultSet;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_part_time_manager";

    private static final String EMP_TABLE = "employee";
    private static final String EMP_ID = "id";
    private static final String EMP_NAME = "name";
    private static final String EMP_ADDRESS = "address";
    private static final String EMP_PHONE_NUMBER = "numberPhone";
    private static final String EMP_IMAGE_1 = "imageFirst";
    private static final String EMP_IMAGE_2 = "imageSecond";

    private static final String PARTY_TABLE = "party";
    private static final String PARTY_ID = "id";
    private static final String PARTY_NAME = "name";
    private static final String PARTY_DATE = "date";
    private static final String PARTY_TIME_BEGIN = "timeBegin";
    private static final String PARTY_TIME_END = "timeEnd";
    private static final String PARTY_FLOOR = "floor";
    private static final String PARTY_LOCATION = "location";
    private static final String PARTY_HOURLY_WAGE = "hourlyWage";
    private static final String PARTY_NUM = "numberOfEmps";

    private static final String EMP_PARTY_TABLE = "emp_party";
    private static final String EMP_PARTY_ID_EMP = "idEmp";
    private static final String EMP_PARTY_ID_PARTY = "idParty";
    private static final String EMP_PARTY_TIME_END = "timeEnd";


    private static int VERSION = 3;
    private Context context;

    private String query_create_employeeTable = "CREATE TABLE " + EMP_TABLE + " (" +
                    EMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EMP_NAME + " TEXT, " +
                    EMP_ADDRESS + " TEXT, " +
                    EMP_PHONE_NUMBER + " TEXT, " +
                    EMP_IMAGE_1 + " BLOB, " +
                    EMP_IMAGE_2 + " BLOB)",
            query_create_partyTable = "CREATE TABLE " + PARTY_TABLE + " (" +
                    PARTY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PARTY_NAME + " TEXT, " +
                    PARTY_DATE + " TEXT, " +
                    PARTY_TIME_BEGIN + " REAL, " +
                    PARTY_TIME_END + " REAL, " +
                    PARTY_FLOOR + " TEXT, " +
                    PARTY_LOCATION + " TEXT, " +
                    PARTY_HOURLY_WAGE + " INTEGER, " +
                    PARTY_NUM + " INTEGER)",
            query_create_EmpParty_Table = "CREATE TABLE " + EMP_PARTY_TABLE + " (" +
                    EMP_PARTY_ID_EMP + " INTEGER, " +
                    EMP_PARTY_ID_PARTY + " INTEGER, " +
                    EMP_PARTY_TIME_END + " REAL, " +
                    "FOREIGN KEY(" +EMP_PARTY_ID_EMP+ ") REFERENCES " + EMP_TABLE + "(" + EMP_ID +"), " +
                    "FOREIGN KEY(" +EMP_PARTY_ID_PARTY+ ") REFERENCES " + PARTY_TABLE + "(" + PARTY_ID +"))";

    public DBManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query_create_partyTable);
        db.execSQL(query_create_employeeTable);
        db.execSQL(query_create_EmpParty_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion<3 && newVersion<4){
            db.execSQL(query_create_EmpParty_Table);
        }
    }

    public void addParty(Party party) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PARTY_NAME, party.getmName());
        values.put(PARTY_DATE, party.getmDate());
        values.put(PARTY_TIME_BEGIN, party.getmTimeBegin());
        if(party.getmTimeEnd()!=null){
            values.put(PARTY_TIME_END, party.getmTimeEnd());
        }
        values.put(PARTY_FLOOR, party.getmFloor());
        values.put(PARTY_LOCATION, party.getmLocation());
        values.put(PARTY_HOURLY_WAGE, party.getmHourlyWage());
        values.put(PARTY_NUM, party.getmNumOfEmps());

        db.insert(PARTY_TABLE, null, values);
        db.close();
    }

    public void addEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EMP_NAME, employee.getmName());
        values.put(EMP_ADDRESS, employee.getmAddress());
        values.put(EMP_PHONE_NUMBER, employee.getmPhoneNumber());
        values.put(EMP_IMAGE_1, employee.getmImage_1());
        values.put(EMP_IMAGE_2, employee.getmImage_2());

        db.insert(EMP_TABLE, null, values);
        db.close();

    }

    public void addEmployee_Party(int empId, int partyId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EMP_PARTY_ID_EMP, empId);
        values.put(EMP_PARTY_ID_PARTY, partyId);

        db.insert(EMP_PARTY_TABLE, null, values);
        db.close();

    }

    //another way
    public void insertEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO " + EMP_TABLE + " VALUES(null, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, employee.getmName());
        statement.bindString(2, employee.getmAddress());
        statement.bindString(3, employee.getmPhoneNumber());
        statement.bindBlob(4, employee.getmImage_1());
        statement.bindBlob(5, employee.getmImage_2());

        statement.executeInsert();
    }

    public List<Party> getAllParties() {
        List<Party> partyList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PARTY_TABLE + " ORDER BY " + PARTY_DATE +" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Party party = new Party();
                party.setmId(cursor.getInt(0));
                party.setmName(cursor.getString(1));
                party.setmDate(cursor.getString(2));
                party.setmTimeBegin(cursor.getFloat(3));
                party.setmTimeEnd(cursor.getFloat(4));
                party.setmFloor(cursor.getString(5));
                party.setmLocation(cursor.getString(6));
                party.setmHourlyWage(cursor.getInt(7));
                party.setmNumOfEmps(cursor.getInt(8));

                partyList.add(party);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return partyList;
    }

    public List<Party> getParties_byDate(String date){
        List<Party>  partyList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PARTY_TABLE + " WHERE " + PARTY_DATE + " = " + '"' + date + '"';
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                Party party = new Party();
                party.setmId(cursor.getInt(0));
                party.setmName(cursor.getString(1));
                party.setmDate(cursor.getString(2));
                party.setmTimeBegin(cursor.getFloat(3));

                if(Float.toString(cursor.getFloat(4)).equals("null")){
                    party.setmTimeEnd(null);
                }
                else{
                    party.setmTimeEnd(cursor.getFloat(4));
                }
//                party.setmTimeEnd(cursor.getFloat(4));
                party.setmFloor(cursor.getString(5));
                party.setmLocation(cursor.getString(6));
                party.setmHourlyWage(cursor.getInt(7));
                party.setmNumOfEmps(cursor.getInt(8));

                partyList.add(party);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return partyList;
    }

    public List<Party> getParties_Date_to_Date(String dateFrom, String dateTo){
        List<Party>  partyList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PARTY_TABLE + " WHERE " + PARTY_DATE + " >= " + '"' + dateFrom + '"' + " and " +PARTY_DATE+ "<=" +'"' + dateTo + '"'
                + " ORDER BY " + PARTY_DATE + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                Party party = new Party();
                party.setmId(cursor.getInt(0));
                party.setmName(cursor.getString(1));
                party.setmDate(cursor.getString(2));
                party.setmTimeBegin(cursor.getFloat(3));

                if(Float.toString(cursor.getFloat(4)).equals("null")){
                    party.setmTimeEnd(null);
                }
                else{
                    party.setmTimeEnd(cursor.getFloat(4));
                }
//                party.setmTimeEnd(cursor.getFloat(4));
                party.setmFloor(cursor.getString(5));
                party.setmLocation(cursor.getString(6));
                party.setmHourlyWage(cursor.getInt(7));
                party.setmNumOfEmps(cursor.getInt(8));

                partyList.add(party);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return partyList;
    }
    public List<Party> getParties_byName(String nameParty){
        List<Party>  partyList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PARTY_TABLE + " WHERE " + PARTY_NAME + " LIKE " + '"' +"%" + nameParty + "%" + '"'
                + " ORDER BY " + PARTY_DATE + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                Party party = new Party();
                party.setmId(cursor.getInt(0));
                party.setmName(cursor.getString(1));
                party.setmDate(cursor.getString(2));
                party.setmTimeBegin(cursor.getFloat(3));

                if(Float.toString(cursor.getFloat(4)).equals("null")){
                    party.setmTimeEnd(null);
                }
                else{
                    party.setmTimeEnd(cursor.getFloat(4));
                }
//                party.setmTimeEnd(cursor.getFloat(4));
                party.setmFloor(cursor.getString(5));
                party.setmLocation(cursor.getString(6));
                party.setmHourlyWage(cursor.getInt(7));
                party.setmNumOfEmps(cursor.getInt(8));

                partyList.add(party);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return partyList;
    }


    public List<Employee> getAllEmployees() {
        List<Employee> list_employee = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + EMP_TABLE ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Employee employee = new Employee();
                employee.setmId(cursor.getInt(0));
                employee.setmName(cursor.getString(1));
                employee.setmAddress(cursor.getString(2));
                employee.setmPhoneNumber(cursor.getString(3));
                employee.setmImage_1(cursor.getBlob(4));
                employee.setmImage_2(cursor.getBlob(5));
                list_employee.add(employee);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list_employee;
    }

    public List<Employee> getEmployees_bySearchName(EditText editText) {
        List<Employee> employeeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + EMP_TABLE
                + " WHERE " + EMP_NAME + " LIKE '%" + editText.getText().toString().trim() + "%'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Employee employee = new Employee();
                employee.setmId(cursor.getInt(0));
                employee.setmName(cursor.getString(1));
                employee.setmAddress(cursor.getString(2));
                employee.setmPhoneNumber(cursor.getString(3));
                employee.setmImage_1(cursor.getBlob(4));
                employee.setmImage_2(cursor.getBlob(5));
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return employeeList;
    }
    public int getId_Last_Added_Party(){
//        String selectQuery  = "SELECT " + PARTY_ID + " FROM "+ PARTY_TABLE + " ORDER BY " + PARTY_ID + " DESC LIMIT 1";
        String selectQuery = "SELECT * FROM " + PARTY_TABLE + " ORDER BY " + PARTY_ID +" ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int last;

        if(cursor.moveToFirst()){
            do{
                if (cursor.isLast()){
                    return cursor.getInt(0);
                }
            }while (cursor.moveToNext());
        }

        cursor.close();
        return  0;

    }


    public  List<Employee> getEmployees_addedPartTimer(int[] position, int length_position) //
    {
        List<Employee> employeeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + EMP_TABLE
                + " WHERE " + EMP_ID + " = " + position[0] ;
        for (int i=1; i< length_position; i++){
            selectQuery += " OR " + EMP_ID + " = " + position[i] ;
        }

        //Toast.makeText(context, selectQuery, Toast.LENGTH_LONG).show();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Employee employee = new Employee();
                employee.setmId(cursor.getInt(0));
                employee.setmName(cursor.getString(1));
                employee.setmAddress(cursor.getString(2));
                employee.setmPhoneNumber(cursor.getString(3));
                employee.setmImage_1(cursor.getBlob(4));
                employee.setmImage_2(cursor.getBlob(5));
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return employeeList;
    }

    public List<String> getAllNameEmployee(){
        List<String> nameList = new ArrayList<>();
        String selectQuery = "SELECT " + EMP_NAME + " FROM " + EMP_TABLE;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                nameList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return nameList;
    }

    public boolean existedSearchName(EditText editText) {
        String selectQuery = "SELECT * FROM " + EMP_TABLE
                + " WHERE " + EMP_NAME + " LIKE '%" + editText.getText().toString().trim() + "%'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
            return true;
        return false;
    }

    public String convertDate_String(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }

    public boolean existedPartyDate(String date){
        String selectQuery = "SELECT * FROM " + PARTY_TABLE
                + " WHERE " + PARTY_DATE + " = " + '"' + date + '"';
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor =database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            return  true;
        }
        return false;
    }

    public int countRow_existedPartTimersOfParty(int idParty){
        String selectQuery = "SELECT * FROM " + EMP_PARTY_TABLE
                + " WHERE " + EMP_PARTY_ID_PARTY + "=" + idParty;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        return cursor.getCount();
    }



    public int countAllEmployee(){
        String selectQuery = "SELECT " + EMP_NAME + " FROM " + EMP_TABLE;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        return cursor.getCount();
    }

    public List<WorkedDate> getData(String nameEmp, String dateBegin, String dateLast){
        List<WorkedDate> workedDateList = new ArrayList<>();

        String selectQuery = new String();
        if(dateBegin!=null){
            if(dateLast!=null){
                selectQuery= "SELECT PAR.date, PAR.timeBegin, PAR.timeEnd, PAR.hourlyWage*(PAR.timeEnd-PAR.timeBegin) AS salaryOfDate"
                        +" FROM  party AS PAR INNER JOIN employee AS EMP INNER JOIN emp_party AS EPT"
                        +" ON EMP.id = EPT.idEmp AND PAR.id = EPT.idParty"
                        +" WHERE EMP.NAME = "+'"'+nameEmp+'"'+" AND PAR.date >="+'"'+dateBegin+'"'+" AND PAR.date <="+'"'+dateLast+'"';
            }
            else{
                selectQuery = "SELECT PAR.date, PAR.timeBegin, PAR.timeEnd, PAR.hourlyWage*(PAR.timeEnd-PAR.timeBegin) AS salaryOfDate"
                        +" FROM  party AS PAR INNER JOIN employee AS EMP INNER JOIN emp_party AS EPT"
                        +" ON EMP.id = EPT.idEmp AND PAR.id = EPT.idParty"
                        +" WHERE EMP.NAME = "+'"'+nameEmp+'"'+" AND PAR.date ="+'"'+dateBegin+'"';
            }
        }

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                WorkedDate workedDate = new WorkedDate();
                workedDate.setmDate(cursor.getString(0));
                workedDate.setmTimeBegin(cursor.getFloat(1));
                workedDate.setmTimeEnd(cursor.getFloat(2));
                workedDate.setSalary(cursor.getInt(3));
                workedDateList.add(workedDate);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return  workedDateList;
    }

    public HashMap<String, List<WorkedDate>> getEmployeeWorkedOnDate(String name, String dateBegin, String dateLast) {
        HashMap<String, List<WorkedDate>> hashMap = new HashMap<>();
        List<WorkedDate> workedDateList = new ArrayList<>();
        if (name.equals("All")) {
            String selectQuery = new String();
            if (dateBegin != null) {
                if (dateLast != null) {
                    selectQuery = "SELECT EMP.name\n" +
                            "FROM  party AS PAR INNER JOIN employee AS EMP INNER JOIN emp_party AS EPT \n" +
                            "ON EMP.id = EPT.idEmp AND PAR.id = EPT.idParty\n" +
                            "WHERE PAR.date >= \"" + dateBegin + "\" and PAR.date <= \"" + dateLast + "\" \n" +
                            "GROUP BY EMP.name\n" +
                            "ORDER BY EMP.name";
                } else {
                    selectQuery = "SELECT EMP.name\n" +
                            "FROM  party AS PAR INNER JOIN employee AS EMP INNER JOIN emp_party AS EPT \n" +
                            "ON EMP.id = EPT.idEmp AND PAR.id = EPT.idParty\n" +
                            "WHERE PAR.date = \"" + dateBegin + "\" \n" +
                            "GROUP BY EMP.name\n" +
                            "ORDER BY EMP.name";
                }
            }

            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    workedDateList = getData(cursor.getString(0), dateBegin, dateLast);
                    hashMap.put(cursor.getString(0), workedDateList);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            if(Is_WorkedOnDate(name,dateBegin,dateLast)){
                workedDateList = getData(name, dateBegin, dateLast);
                hashMap.put(name, workedDateList);
            }
        }
        return hashMap;
    }
    public boolean Is_WorkedOnDate(String name, String dateBegin, String dateLast) {
        String selectQuery = new String();
        if (dateBegin != null) {
            if (dateLast != null) {
                selectQuery = "SELECT EMP.name\n" +
                        "FROM  party AS PAR INNER JOIN employee AS EMP INNER JOIN emp_party AS EPT \n" +
                        "ON EMP.id = EPT.idEmp AND PAR.id = EPT.idParty\n" +
                        "WHERE PAR.date >= \"" + dateBegin + "\" and PAR.date <= \"" + dateLast + "\" \n" +
                        "GROUP BY EMP.name";
            } else {
                selectQuery = "SELECT EMP.name\n" +
                        "FROM  party AS PAR INNER JOIN employee AS EMP INNER JOIN emp_party AS EPT \n" +
                        "ON EMP.id = EPT.idEmp AND PAR.id = EPT.idParty\n" +
                        "WHERE PAR.date = \"" + dateBegin + "\" \n" +
                        "GROUP BY EMP.name";
            }
        }

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if(cursor.getString(0).equals(name)){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return false;
    }

    public int SumSalary(String name, String dateBegin, String dateEnd){
        String selectQuery = new String();
        if(name.equals("All")){
            selectQuery ="SELECT Sum((PAR.timeEnd-PAR.timeBegin)*PAR.hourlyWage) AS Sum\n" +
                    "FROM  party AS PAR INNER JOIN employee AS EMP INNER JOIN emp_party AS EPT ON EMP.id = EPT.idEmp AND PAR.id = EPT.idParty\n" +
                    "WHERE PAR.date >= \""+ dateBegin +"\" and PAR.date <= \""+dateEnd +"\"";
        }
        else{
            selectQuery ="SELECT Sum((PAR.timeEnd-PAR.timeBegin)*PAR.hourlyWage) AS Sum\n" +
                    "FROM  party AS PAR INNER JOIN employee AS EMP INNER JOIN emp_party AS EPT ON EMP.id = EPT.idEmp AND PAR.id = EPT.idParty\n" +
                    "WHERE PAR.date >= \""+ dateBegin +"\" and PAR.date <= \""+dateEnd +"\" and EMP.name = \"" +name+ '"';
        }
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            return cursor.getInt(0);
        }

        return -1;
    }
}