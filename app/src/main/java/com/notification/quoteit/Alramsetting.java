package com.notification.quoteit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gb.quoteit.R;
import com.kyleduo.switchbutton.SwitchButton;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class Alramsetting extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener {

    TextView mtimeTextView;
    TextView mam_pmTextView;
    SwitchButton noti_on_off;
    LinearLayout timeLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alramsetting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View includeView = findViewById(R.id.alramid);

        mtimeTextView = (TextView) includeView.findViewById(R.id.time);
        mam_pmTextView = (TextView) includeView.findViewById(R.id.ampm);
        noti_on_off=(SwitchButton)includeView.findViewById(R.id.noti_on_off);
        timeLinearLayout= (LinearLayout) includeView.findViewById(R.id.lineartime);


        noti_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                timeLinearLayout.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
            }
        });






        timeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        Alramsetting.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );

                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        int temp=0;
        String hour,minutes;
        if (hourOfDay>12)
            temp=hourOfDay-12;
        else
        temp=hourOfDay;




        hour = (temp < 10) ? "0"+temp : ""+temp;
        minutes = (minute < 10) ? "0"+minute : ""+minute;

        mtimeTextView.setText(hour+ ":" + minutes);

        String am_pm = (hourOfDay < 12) ? "am" : "pm";
        mam_pmTextView.setText(am_pm);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Calendar timeOff9 = Calendar.getInstance();
        timeOff9.set(Calendar.HOUR_OF_DAY, hourOfDay);
        timeOff9.set(Calendar.MINUTE, minute);
        timeOff9.set(Calendar.SECOND, second);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeOff9.getTimeInMillis(), pendingIntent);

    }
}
