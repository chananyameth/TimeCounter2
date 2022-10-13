package com.chananya.timecounter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {


    private HashMap<String, Object> drive_info = new HashMap<>();
    private double index = 0;
    private double day_sum_time = 0;
    private double night_sum_time = 0;
    private double temp = 0;
    private boolean isSelectionMode = false;
    private double length = 0;
    private double GMToffset = 0;
    private String temp_s = "";
    private HashMap<String, Object> tempp = new HashMap<>();
    private double area = 0;
    private double inCity = 0;
    private double outCity = 0;
    private double startHour = 0;

    private ArrayList<HashMap<String, Object>> drives_info_list = new ArrayList<>();
    private ArrayList<Double> checkedItems = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> tempppp = new ArrayList<>();

    private LinearLayout all_l;
    private LinearLayout fab_l;
    private LinearLayout top_l;
    private LinearLayout counters_0_l;
    private LinearLayout linear1;
    private ListView drives_listview;
    private Button manual_input_button;
    private Button start_new_button;
    private TextView night_counter_tv;
    private TextView day_counter_tv;
    private LinearLayout linear3;
    private LinearLayout linear4;
    private ImageView imageview1;
    private TextView in_city_tv;
    private ImageView imageview7;
    private ImageView imageview6;
    private TextView out_city_tv;
    private ImageView imageview8;
    private ImageView delete_imageview;

    private SharedPreferences f;
    private Calendar c1 = Calendar.getInstance();
    private Intent i = new Intent();
    private AlertDialog.Builder d;
    private AlertDialog.Builder d1;
    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        all_l = (LinearLayout) findViewById(R.id.all_l);
        fab_l = (LinearLayout) findViewById(R.id.fab_l);
        top_l = (LinearLayout) findViewById(R.id.top_l);
        counters_0_l = (LinearLayout) findViewById(R.id.counters_0_l);
        linear1 = (LinearLayout) findViewById(R.id.linear1);
        drives_listview = (ListView) findViewById(R.id.drives_listview);
        manual_input_button = (Button) findViewById(R.id.manual_input_button);
        start_new_button = (Button) findViewById(R.id.start_new_button);
        night_counter_tv = (TextView) findViewById(R.id.night_counter_tv);
        day_counter_tv = (TextView) findViewById(R.id.day_counter_tv);
        linear3 = (LinearLayout) findViewById(R.id.linear3);
        linear4 = (LinearLayout) findViewById(R.id.linear4);
        imageview1 = (ImageView) findViewById(R.id.imageview1);
        in_city_tv = (TextView) findViewById(R.id.in_city_tv);
        imageview7 = (ImageView) findViewById(R.id.imageview7);
        imageview6 = (ImageView) findViewById(R.id.imageview6);
        out_city_tv = (TextView) findViewById(R.id.out_city_tv);
        imageview8 = (ImageView) findViewById(R.id.imageview8);
        delete_imageview = (ImageView) findViewById(R.id.delete_imageview);
        f = getSharedPreferences("data", Activity.MODE_PRIVATE);
        d = new AlertDialog.Builder(this);
        d1 = new AlertDialog.Builder(this);

        drives_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;
                if (isSelectionMode) {
                    if (checkedItems.contains(Double.parseDouble(String.valueOf(_position)))) {
                        index = 0;
                        for(int _repeat27 = 0; _repeat27 < (int)(checkedItems.size()); _repeat27++) {
                            if (Double.parseDouble(String.valueOf(_position)) == checkedItems.get((int)(index)).doubleValue()) {
                                checkedItems.remove((int)(index));
                                break;
                            }
                            index++;
                        }
                    }
                    else {
                        checkedItems.add(Double.valueOf(_position));
                    }
                    ((BaseAdapter)drives_listview.getAdapter()).notifyDataSetChanged();
                }
                else {
                    i.putExtra("position", String.valueOf((long)(_position)));
                    i.setClass(getApplicationContext(), DriveActivity.class);
                    startActivity(i);
                }
            }
        });

        drives_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;
                isSelectionMode = true;
                ((BaseAdapter)drives_listview.getAdapter()).notifyDataSetChanged();
                start_new_button.setEnabled(false);
                manual_input_button.setEnabled(false);
                delete_imageview.setVisibility(View.VISIBLE);
                if (checkedItems.contains(Double.parseDouble(String.valueOf(_position)))) {
                    index = 0;
                    for(int _repeat26 = 0; _repeat26 < (int)(checkedItems.size()); _repeat26++) {
                        if (Double.parseDouble(String.valueOf(_position)) == checkedItems.get((int)(index)).doubleValue()) {
                            checkedItems.remove((int)(index));
                            break;
                        }
                        index++;
                    }
                }
                else {
                    checkedItems.add(Double.valueOf(_position));
                }
                ((BaseAdapter)drives_listview.getAdapter()).notifyDataSetChanged();
                return true;
            }
        });

        manual_input_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                c1 = Calendar.getInstance();
                drive_info = new HashMap<>();
                drive_info.put("date", new SimpleDateFormat("dd/MM/yyyy").format(c1.getTime()));
                drive_info.put("start", String.valueOf((long)(c1.getTimeInMillis())));
                drive_info.put("length", "0");
                drive_info.put("area", "5");
                drive_info.put("accompany", "");
                drives_info_list.add((int)0, drive_info);
                f.edit().putString("drives", new Gson().toJson(drives_info_list)).commit();
                i.putExtra("position", String.valueOf((long)(0)));
                i.setClass(getApplicationContext(), DriveActivity.class);
                startActivity(i);
            }
        });

        start_new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                i.putExtra("position", "-1");
                i.setClass(getApplicationContext(), DriveActivity.class);
                startActivity(i);
            }
        });

        delete_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                d.setTitle("מחיקה");
                d.setMessage("למחוק את הרשומות הנבחרות?");
                d.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        isSelectionMode = false;
                        start_new_button.setEnabled(true);
                        manual_input_button.setEnabled(true);
                        delete_imageview.setVisibility(View.GONE);
                        index = drives_info_list.size() - 1;
                        temp = drives_info_list.size();
                        for(int _repeat17 = 0; _repeat17 < (int)(temp); _repeat17++) {
                            if (checkedItems.contains(index)) {
                                drives_info_list.remove((int)(index));
                            }
                            index--;
                        }
                        checkedItems.clear();
                        f.edit().putString("drives", new Gson().toJson(drives_info_list)).commit();
                        ((BaseAdapter)drives_listview.getAdapter()).notifyDataSetChanged();
                        _update_sum_tvs();
                    }
                });
                d.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });
                d.create().show();
            }
        });
    }
    private void initializeLogic() {
        if(TimeZone.getTimeZone("Israel").inDaylightTime( new Date() ))
        {
            GMToffset = 10800000;
        }
        else
            GMToffset = 7200000;
        if (f.getString("drives", "").equals("")) {
            f.edit().putString("drives", "[]").commit();
        }
        delete_imageview.setVisibility(View.GONE);
        if (!f.getString("with area", "").equals("yes")) {
            f.edit().putString("with area", "yes").commit();
            drives_info_list = new Gson().fromJson(f.getString("drives", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
            index = 0;
            for(int _repeat44 = 0; _repeat44 < (int)(drives_info_list.size()); _repeat44++) {
                drives_info_list.get((int)index).put("area", "5");
                index++;
            }
            f.edit().putString("drives", new Gson().toJson(drives_info_list)).commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        menu.add(0, 0, 0, "הגדרות");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case 0:
                _openSettings();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        drives_info_list = new Gson().fromJson(f.getString("drives", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        drives_listview.setAdapter(new Drives_listviewAdapter(drives_info_list));
        _update_sum_tvs();
    }

    @Override
    public void onBackPressed() {
        if (isSelectionMode) {
            isSelectionMode = false;
            checkedItems.clear();
            start_new_button.setEnabled(true);
            manual_input_button.setEnabled(true);
            delete_imageview.setVisibility(View.GONE);
            ((BaseAdapter)drives_listview.getAdapter()).notifyDataSetChanged();
        }
        else {
            finish();
        }
    }
    private void _update_sum_tvs () {
        day_sum_time = 0;
        night_sum_time = 0;
        inCity = 0;
        outCity = 0;
        index = 0;
        for(int _repeat13 = 0; _repeat13 < (int)(drives_info_list.size()); _repeat13++) {
            temp = Double.parseDouble(drives_info_list.get((int)index).get("start").toString());
            if(TimeZone.getTimeZone("Israel").inDaylightTime( new Date((long)temp) ))
            {
                GMToffset = 10800000;
            }
            else
            {
                GMToffset = 7200000;
            }
            startHour = ((Double.parseDouble(drives_info_list.get((int)index).get("start").toString()) + GMToffset) / 3600000) % 24;
            length = Double.parseDouble(drives_info_list.get((int)index).get("length").toString());
            //check if the drive is both at daylight-saving and don't-daylight-saving, and if so, skip this drive
            if(TimeZone.getTimeZone("Israel").inDaylightTime(new Date((long)temp)) != TimeZone.getTimeZone("Israel").inDaylightTime(new Date((long)(temp + length)))) {
                d1.setTitle("אזהרה!");
                d1.setMessage("נסיעה מספר ".concat(String.valueOf((long)(drives_info_list.size() - index)).concat(" נמשכת גם בשעון קיץ, וגם בשעון חורף. בגלל האורך הבעייתי של הנסיעה הנסיעה הזאת לא נמנית במונים למעלה. כדי שנסיעה זאת תימנה גם כן, אנא שנו את התאריך, השעה או אורך הנסיעה.\n\n is both at daylight-saving-time and don't-daylight-saving-time, so, due the unabsolute length of the drive we didn't count it to the counters above. you may change the date or the length of that drive so we'll count it.")));
                d1.setPositiveButton("בסדר", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });
                d1.create().show();
                index++;
                continue; }
            area = Double.parseDouble(drives_info_list.get((int)index).get("area").toString());
            if ((startHour > 6) && (startHour < 21)) {
                //starts at day
                if (((length / 3600000) % 24) > (21 - startHour)) {
                    //continues to night
                    if (((length / 3600000) % 24) > (30 - startHour)) {
                        //continues to day again
                        day_sum_time = day_sum_time + (length - (9 * 3600000));
                        night_sum_time = night_sum_time + (9 * 3600000);
                    }
                    else {
                        day_sum_time = day_sum_time + ((21 - startHour) * 3600000);
                        night_sum_time = night_sum_time + (length - ((21 - startHour) * 3600000));
                    }
                }
                else {
                    day_sum_time = day_sum_time + length;
                }
            }
            else {
                //starts at night
                if (((length / 3600000) % 24) > ((30 - startHour) % 24)) {
                    //continues to day
                    if (((length / 3600000) % 24) > ((45 - startHour) % 24)) {
                        //continues to night again
                        night_sum_time = night_sum_time + (length - (15 * 3600000));
                        day_sum_time = day_sum_time + (15 * 3600000);
                    }
                    else {
                        night_sum_time = night_sum_time + (((30 - startHour) % 24) * 3600000);
                        day_sum_time = day_sum_time + (length - (((30 - startHour) % 24) * 3600000));
                    }
                }
                else {
                    night_sum_time = night_sum_time + length;
                }
            }
            //adding the time to the correct area
            inCity = inCity + ((length / 10) * (10 - area));
            outCity = outCity + ((length / 10) * area);
            index++;
        }
        _millis_to_hour_View(day_sum_time + night_sum_time, day_counter_tv);
        setTitle("מונה שעות - ".concat(day_counter_tv.getText().toString().concat(" /50")));
        _millis_to_hour_View(day_sum_time, day_counter_tv);
        _millis_to_hour_View(night_sum_time, night_counter_tv);
        night_counter_tv.setText(night_counter_tv.getText().toString().concat("\n/15"));
        _millis_to_hour_View(inCity, in_city_tv);
        in_city_tv.setText(in_city_tv.getText().toString().concat("\n/20"));
        _millis_to_hour_View(outCity, out_city_tv);
        out_city_tv.setText(out_city_tv.getText().toString().concat("\n/15"));
    }


    private void _millis_to_hour_View (final double _millis, final TextView _view) {
        temp = _millis / 1000;
        temp_s = "";
        if ((temp / 3600) < 10) {
            temp_s = "0".concat(String.valueOf((long)(temp / 3600)).concat(":"));
        }
        else {
            temp_s = String.valueOf((long)(temp / 3600)).concat(":");
        }
        temp = temp % 3600;
        if ((temp / 60) < 10) {
            temp_s = temp_s.concat("0".concat(String.valueOf((long)(temp / 60)).concat(":")));
        }
        else {
            temp_s = temp_s.concat(String.valueOf((long)(temp / 60)).concat(":"));
        }
        temp = temp % 60;
        if (temp < 10) {
            temp_s = temp_s.concat("0".concat(String.valueOf((long)(temp))));
        }
        else {
            temp_s = temp_s.concat(String.valueOf((long)(temp)));
        }
        _view.setText(temp_s);
    }


    private void _openSettings () {
        i.setClass(getApplicationContext(), SettingsActivity.class);
        startActivity(i);
    }


    public class Drives_listviewAdapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;
        public Drives_listviewAdapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }
        @Override
        public View getView(final int _position, View _view, ViewGroup _viewGroup) {
            LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _v = _view;
            if (_v == null) {
                _v = _inflater.inflate(R.layout.drive_item, null);
            }

            final LinearLayout all_linear = (LinearLayout) _v.findViewById(R.id.all_linear);
            final LinearLayout times_l = (LinearLayout) _v.findViewById(R.id.times_l);
            final LinearLayout tvs_l = (LinearLayout) _v.findViewById(R.id.tvs_l);
            final LinearLayout date_accompany_l = (LinearLayout) _v.findViewById(R.id.date_accompany_l);
            final TextView sum_tv = (TextView) _v.findViewById(R.id.sum_tv);
            final TextView record_no_tv = (TextView) _v.findViewById(R.id.record_no_tv);
            final TextView start_time_tv = (TextView) _v.findViewById(R.id.start_time_tv);
            final TextView finish_time_tv = (TextView) _v.findViewById(R.id.finish_time_tv);
            final TextView textview1 = (TextView) _v.findViewById(R.id.textview1);
            final TextView textview2 = (TextView) _v.findViewById(R.id.textview2);
            final TextView date_tv = (TextView) _v.findViewById(R.id.date_tv);
            final TextView accompanied_tv = (TextView) _v.findViewById(R.id.accompanied_tv);

            drive_info = drives_info_list.get((int)_position);
            if (checkedItems.contains(Double.parseDouble(String.valueOf(_position)))) {
                all_linear.setBackgroundColor(0xFF90CAF9);
            }
            else {
                all_linear.setBackgroundColor(Color.TRANSPARENT);
            }
            c1.setTimeInMillis((long)(Double.parseDouble(drive_info.get("start").toString())));
            start_time_tv.setText(new SimpleDateFormat("HH:mm:ss").format(c1.getTime()));
            c1.setTimeInMillis((long)(Double.parseDouble(drive_info.get("start").toString()) + Double.parseDouble(drive_info.get("length").toString())));
            finish_time_tv.setText(new SimpleDateFormat("HH:mm:ss").format(c1.getTime()));
            date_tv.setText(drive_info.get("date").toString());
            accompanied_tv.setText(drive_info.get("accompany").toString());
            _millis_to_hour_View(Double.parseDouble(drive_info.get("length").toString()), sum_tv);
            record_no_tv.setText(String.valueOf((long)(_data.size() - _position)));

            return _v;
        }
    }

    @Deprecated
    public void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    public int getLocationX(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[0];
    }

    @Deprecated
    public int getLocationY(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[1];
    }

    @Deprecated
    public int getRandom(int _min, int _max) {
        Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    @Deprecated
    public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
        ArrayList<Double> _result = new ArrayList<Double>();
        SparseBooleanArray _arr = _list.getCheckedItemPositions();
        for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
            if (_arr.valueAt(_iIdx))
                _result.add((double)_arr.keyAt(_iIdx));
        }
        return _result;
    }

    @Deprecated
    public float getDip(int _input){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels(){
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels(){
        return getResources().getDisplayMetrics().heightPixels;
    }
}
