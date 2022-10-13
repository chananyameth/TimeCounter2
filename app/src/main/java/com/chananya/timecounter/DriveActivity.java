package com.chananya.timecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.text.*;
import android.app.Activity;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.SeekBar;
import android.content.SharedPreferences;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import android.media.SoundPool;
import android.view.View;
import android.widget.CompoundButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DriveActivity extends AppCompatActivity {

    private Timer _timer = new Timer();

    private double current_time = 0;
    private HashMap<String, Object> drive_info = new HashMap<>();
    private double GMToffset = 0;
    private HashMap<String, Object> temp_m = new HashMap<>();
    private double index = 0;
    private double sound_pool = 0;
    private double temp = 0;
    private String temp_s = "";

    private ArrayList<HashMap<String, Object>> drives_info_list = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> temp_lm = new ArrayList<>();

    private LinearLayout linear1;
    private EditText edittext_import;
    private Button stop_button;
    private Switch switch1;
    private TextView date_tv;
    private TextView textview1;
    private TextView start_time_tv;
    private TextView textview3;
    private TextView current_time_tv;
    private LinearLayout linear2;
    private EditText accompany_et;
    private TextView textview4;
    private SeekBar seekbar1;
    private TextView textview5;

    private SharedPreferences f;
    private Calendar start = Calendar.getInstance();
    private Calendar finish = Calendar.getInstance();
    private TimerTask t;
    private SoundPool reminder;
    private TimerTask t1;
    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.drive);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        linear1 = (LinearLayout) findViewById(R.id.linear1);
        edittext_import = (EditText) findViewById(R.id.edittext_import);
        stop_button = (Button) findViewById(R.id.stop_button);
        switch1 = (Switch) findViewById(R.id.switch1);
        date_tv = (TextView) findViewById(R.id.date_tv);
        textview1 = (TextView) findViewById(R.id.textview1);
        start_time_tv = (TextView) findViewById(R.id.start_time_tv);
        textview3 = (TextView) findViewById(R.id.textview3);
        current_time_tv = (TextView) findViewById(R.id.current_time_tv);
        linear2 = (LinearLayout) findViewById(R.id.linear2);
        accompany_et = (EditText) findViewById(R.id.accompany_et);
        textview4 = (TextView) findViewById(R.id.textview4);
        seekbar1 = (SeekBar) findViewById(R.id.seekbar1);
        textview5 = (TextView) findViewById(R.id.textview5);
        f = getSharedPreferences("data", Activity.MODE_PRIVATE);

        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                _stop();
            }
        });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
                final boolean _isChecked = _param2;
                if (_isChecked) {
                    SketchwareUtil.showMessage(getApplicationContext(), "הכנס תבנית Json תקנית בלבד!\n(ניתן לראות את השדות המתאימים ע''י ייצוא התוכן הקיים)");
                    SketchwareUtil.showMessage(getApplicationContext(), "הכנס תבנית Json תקנית בלבד!\n(ניתן לראות את השדות המתאימים ע''י ייצוא התוכן הקיים)");
                    edittext_import.setVisibility(View.VISIBLE);
                    linear1.setVisibility(View.GONE);
                }
                else {
                    linear1.setVisibility(View.VISIBLE);
                    edittext_import.setVisibility(View.GONE);
                }
            }
        });

        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(DriveActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        start.set(Calendar.YEAR, year);
                        start.set(Calendar.MONTH, monthOfYear);
                        start.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        date_tv.setText(new SimpleDateFormat("dd/MM/yyyy").format(start.getTime()));

                    }
                }, start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        start_time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(DriveActivity.this, new TimePickerDialog.OnTimeSetListener() { @Override public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    start.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    start.set(Calendar.MINUTE, minute);

                    start.set(Calendar.SECOND, 0);

                    start_time_tv.setText(new SimpleDateFormat("HH:mm:ss").format(start.getTime()));

                } }, start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), true); timePickerDialog.show();
            }
        });

        current_time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (stop_button.getText().toString().equals("בוצע")) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(DriveActivity.this, new TimePickerDialog.OnTimeSetListener() { @Override public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        current_time = hourOfDay*3600000+minute*60000;
                        _millis_to_hour_view(current_time, current_time_tv);
                    } }, (int)current_time/3600000, (int)(current_time%3600000)/60000, true); timePickerDialog.show();
                }
            }
        });

        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged (SeekBar _param1, int _param2, boolean _param3) {
                final int _progressValue = _param2;

            }

            @Override
            public void onStartTrackingTouch(SeekBar _param1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar _param2) {
                if (seekbar1.getProgress() == 0) {
                    SketchwareUtil.showMessage(getApplicationContext(), "רק עירוני");
                }
                else {
                    if (seekbar1.getProgress() == 10) {
                        SketchwareUtil.showMessage(getApplicationContext(), "רק בין עירוני");
                    }
                    else {
                        SketchwareUtil.showMessage(getApplicationContext(), "בין עירוני ".concat(String.valueOf((long)(10 - seekbar1.getProgress())).concat(":".concat(String.valueOf((long)(seekbar1.getProgress())).concat(" עירוני")))));
                    }
                }
            }
        });
    }
    private void initializeLogic() {
        setTitle("מונה שעות");
        if(TimeZone.getTimeZone("Israel").inDaylightTime( new Date() ))
        {
            GMToffset = 10800000;
        }
        else
            GMToffset = 7200000;
        edittext_import.setVisibility(View.GONE);
        drives_info_list = new Gson().fromJson(f.getString("drives", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        drive_info = new HashMap<>();
        if (getIntent().getStringExtra("position").equals("-1")) {
            start = Calendar.getInstance();
            date_tv.setText(new SimpleDateFormat("dd/MM/yyyy").format(start.getTime()));
            start_time_tv.setText(new SimpleDateFormat("HH:mm:ss").format(start.getTime()));
            current_time_tv.setText("00:00:00");
            current_time = 0;
            t = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            current_time = current_time + 1000;
                            _millis_to_hour_view(current_time, current_time_tv);
                        }
                    });
                }
            };
            _timer.scheduleAtFixedRate(t, (int)(1000), (int)(1000));
            _save_drive(false);
            t1 = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _save_drive(true);
                        }
                    });
                }
            };
            _timer.scheduleAtFixedRate(t1, (int)(30000), (int)(30000));
            SketchwareUtil.showMessage(getApplicationContext(), "לא לשכוח לגלות שלט 'נהג חדש'!");


        }
        else {
            drive_info = drives_info_list.get((int)Double.parseDouble(getIntent().getStringExtra("position")));
            start.setTimeInMillis((long)(Double.parseDouble(drive_info.get("start").toString())));
            date_tv.setText(new SimpleDateFormat("dd/MM/yyyy").format(start.getTime()));
            start_time_tv.setText(new SimpleDateFormat("HH:mm:ss").format(start.getTime()));
            current_time = Double.parseDouble(drive_info.get("length").toString());
            _millis_to_hour_view(current_time, current_time_tv);
            seekbar1.setProgress((int)Double.parseDouble(drive_info.get("area").toString()));
            accompany_et.setText(drive_info.get("accompany").toString());
            stop_button.setText("בוצע");
            date_tv.setEnabled(true);
            start_time_tv.setEnabled(true);
            current_time_tv.setEnabled(true);
        }
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
    public void onBackPressed() {
        //do nothing
    }
    private void _stop () {
        try{
            t.cancel();
            t1.cancel();
        }catch(Exception e){}
        _save_drive(true);
        if (!stop_button.getText().toString().equals("בוצע")) {
            SketchwareUtil.showMessage(getApplicationContext(), "לא לשכוח לכסות שלט 'נהג חדש'!");


        }
        finish();
    }


    private void _millis_to_hour_view (final double _millis, final TextView _view) {
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


    private void _save_drive (final boolean _exist) {
        if (!switch1.isChecked()) {
            drive_info.put("date", new SimpleDateFormat("dd/MM/yyyy").format(start.getTime()));
            drive_info.put("start", String.valueOf((long)(start.getTimeInMillis())));
            drive_info.put("length", String.valueOf((long)(Math.min(current_time, 86399999))));
            drive_info.put("area", String.valueOf((long)(seekbar1.getProgress())));
            drive_info.put("accompany", accompany_et.getText().toString());
            if (getIntent().getStringExtra("position").equals("-1")) {
                // for auto-saving
                if (_exist) {
                    drives_info_list.remove((int)(0));
                    drives_info_list.add((int)0, drive_info);
                }
                else {
                    drives_info_list.add((int)0, drive_info);
                }
            }
            else {
                drives_info_list.remove((int)(Double.parseDouble(getIntent().getStringExtra("position"))));
                drives_info_list.add((int)Double.parseDouble(getIntent().getStringExtra("position")), drive_info);
            }
        }
        else {
            if (!edittext_import.getText().toString().equals("")) {
                try{
                    drives_info_list.remove((int)(Double.parseDouble(getIntent().getStringExtra("position"))));
                    temp_lm = new Gson().fromJson(edittext_import.getText().toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
                    index = 0;
                    for(int _repeat48 = 0; _repeat48 < (int)(temp_lm.size()); _repeat48++) {
                        temp_m = temp_lm.get((int)index);
                        drives_info_list.add(temp_m);
                        index++;
                    }
                } catch (Exception e) {
                    SketchwareUtil.showMessage(getApplicationContext(), "קרתה בעיה בטעינת הנתונים. בדוק שאכן הכנסת מחרוזת נתונים תקנית.");
                    SketchwareUtil.showMessage(getApplicationContext(), "קרתה בעיה בטעינת הנתונים. בדוק שאכן הכנסת מחרוזת נתונים תקנית.");
                }
            }
        }
        f.edit().putString("drives", new Gson().toJson(drives_info_list)).commit();
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
