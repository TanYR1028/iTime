package com.example.itime;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//参考网址https://www.jianshu.com/p/1d765ae956cd
public class Countdown extends AppCompatActivity {
private Button bt_edit,bt_back;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("photo");
        String message1 = bundle.getString("message_title");
        final String message2 = bundle.getString("message_deadline");
       final String message3=bundle.getString("message_time");
       String message4=bundle.getString("message_remark");
         final String ddl=message2+" "+message3;
        ImageView Iv = (ImageView) findViewById(R.id.count_img);
        Iv.setImageResource(id);
        TextView biaoti = (TextView) findViewById(R.id.count_biaoti);
        TextView deadline = (TextView) findViewById(R.id.count_deadline);
        final TextView daojishi;
        daojishi = (TextView) findViewById(R.id.text_view_countdown);

        biaoti.setText(message1);
        deadline.setText(ddl+" "+getWeek(message2));

         new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                String shijian=ShengYuShiJian(ddl);
                daojishi.setText(shijian);
            }
            @Override
            public void onFinish() {
            }
        }.start();

    }
    //得到当天是星期几
    public static String getWeek(String time) {
        Calendar calendar = Calendar.getInstance();
        int weekIndex = calendar.get(Calendar.DAY_OF_WEEK);

        String week = "";
        switch (weekIndex) {
            case 1:
                week = "星期日";break;
            case 2:
                week = "星期一";break;
            case 3:
                week = "星期二";break;
            case 4:
                week = "星期三";break;
            case 5:
                week = "星期四";break;
            case 6:
                week = "星期五";break;
            case 7:
                week = "星期六";break;
        }
        return week;

    }

    private String ShengYuShiJian(String endTime) {
        Date nowDate = new Date(System.currentTimeMillis());//当前时间
        long nowDateLong = nowDate.getTime();
       String endTimeStr = endTime + "00秒";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        Date EndDate = null;
        try {
            EndDate = simpleDateFormat.parse(endTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long enddateLong = EndDate.getTime();//结束时间
        long timeLong = enddateLong - nowDateLong;//剩余时间

        if (timeLong <= 0) {
            long guoqu = nowDateLong - enddateLong;
            if (guoqu < 60 * 1000)
                return guoqu / 1000 + "秒";
            else if (guoqu < 60 * 60 * 1000) {
                Long guoquM = guoqu / (1000 * 60);
                Long guoquS = guoqu % (1000 * 60);
                return guoquM + "分" + guoquS/ 1000 + "秒";
            } else if (guoqu < 60 * 60 * 24 * 1000) {
                Long timeLongH = guoqu / (1000 * 60 * 60);
                Long timeLongM = guoqu % (1000 * 60 * 60);
                Long timeLongS = guoqu % (1000 * 60);
                return timeLongH + "小时" + timeLongM / (1000 * 60) + "分" + timeLongS / 1000 + "秒";
            } else { //(timeLong<60*60*24*1000*7)
                Long timeLongD = guoqu / (1000 * 60 * 60 * 24);
                Long timeLongH = guoqu % (1000 * 60 * 60 * 24);
                Long timeLongM =guoqu % (1000 * 60 * 60);
                Long timeLongS = guoqu % (1000 * 60);
                return timeLongD + "天" + timeLongH / (1000 * 60 * 60) + "小时" + timeLongM / (1000 * 60) + "分" + timeLongS / 1000 + "秒";
            }

        } else {
            if (timeLong < 60 * 1000)
                return timeLong / 1000 + "秒";
            else if (timeLong < 60 * 60 * 1000) {
                Long timeLongM = timeLong / (1000 * 60);
                Long timeLongS = timeLong % (1000 * 60);
                return timeLongM + "分" + timeLongS / 1000 + "秒";
            } else if (timeLong < 60 * 60 * 24 * 1000) {
                Long timeLongH = timeLong / (1000 * 60 * 60);
                Long timeLongM = timeLong % (1000 * 60 * 60);
                Long timeLongS = timeLong % (1000 * 60);
                return timeLongH + "小时" + timeLongM / (1000 * 60) + "分" + timeLongS / 1000 + "秒";
            } else { //(timeLong<60*60*24*1000*7)
                Long timeLongD = timeLong / (1000 * 60 * 60 * 24);
                Long timeLongH = timeLong % (1000 * 60 * 60 * 24);
                Long timeLongM = timeLong % (1000 * 60 * 60);
                Long timeLongS = timeLong % (1000 * 60);
                return timeLongD + "天" + timeLongH / (1000 * 60 * 60) + "小时" + timeLongM / (1000 * 60) + "分" + timeLongS / 1000 + "秒";
            }
        }
    }
}
