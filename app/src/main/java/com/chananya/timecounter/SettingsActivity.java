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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.content.ClipData;
import android.content.ClipboardManager;


public class SettingsActivity extends AppCompatActivity {

    private LinearLayout linear_all;
    private TextView textview1;
    private ImageView about_iv;
    private TextView textview2;
    private ImageView copy_iv;
    private TextView textview3;
    private ImageView delete_all_iv;

    private AlertDialog.Builder d;
    private SharedPreferences f;
    private AlertDialog.Builder d1;
    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.settings);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        linear_all = (LinearLayout) findViewById(R.id.linear_all);
        textview1 = (TextView) findViewById(R.id.textview1);
        about_iv = (ImageView) findViewById(R.id.about_iv);
        textview2 = (TextView) findViewById(R.id.textview2);
        copy_iv = (ImageView) findViewById(R.id.copy_iv);
        textview3 = (TextView) findViewById(R.id.textview3);
        delete_all_iv = (ImageView) findViewById(R.id.delete_all_iv);
        d = new AlertDialog.Builder(this);
        f = getSharedPreferences("data", Activity.MODE_PRIVATE);
        d1 = new AlertDialog.Builder(this);

        about_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                d1.setTitle("אודות");
                d1.setMessage("כל הזכויות שמורות לחנניה © chananya@g.jct.ac.il\n\nניתן להפיץ באופן חופשי (ללא תשלום)!\n\nעובד באזור הזמן של ישראל בלבד!\n\n• שימו לב, רשומות שיכללו נסיעה בזמן המעבר בין שעון קיץ לחורף ולהפך לא יחושבו במונים למעלה.");
                d1.setPositiveButton("סבבה", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });
                d1.create().show();
            }
        });

        copy_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                ((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", f.getString("drives", "")));
                SketchwareUtil.showMessage(getApplicationContext(), "כל המידע הועתק!");
            }
        });

        delete_all_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                d.setTitle("מחיקה");
                d.setMessage("למחוק את כל המידע הקיים?");
                d.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        f.edit().putString("drives", "[]").commit();
                        SketchwareUtil.showMessage(getApplicationContext(), "כל המידע התאפס ונמחק!");
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
        setTitle("הגדרות");
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
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
