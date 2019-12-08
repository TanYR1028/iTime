package com.example.itime;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

/**
 * Created by 谭小二 on 2019/11/20.
 */

public class EditScheduleActivity extends AppCompatActivity {
    private Button buttonCancel,buttonOk;
    private EditText editText_biaoti,editText_beizhu;
    private LinearLayout linearLayoutPicture,linearLayoutCycle,linearLayoutDate,linearLayouTitle;
    private TextView cycleView,dateView,timeView;
    private int year, month, day, hour, minute;
    //在TextView上显示的字符
    private StringBuffer date, time;
    private int insertPosition;
    private Context context;
    private String now_riqi,now_time;
    private SelectPictureManager selectPictureManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);
        ActionBar actionBar = getSupportActionBar();
        //隐藏标题栏
        if (actionBar != null) {
            actionBar.hide();
        }
        buttonCancel=(Button)findViewById(R.id.button_cancel);
        buttonOk=(Button)findViewById(R.id.button_ok);

        context=this;
        date = new StringBuffer();
        time = new StringBuffer();

        initDateTime();
        initDateView();
        now_riqi=date.append(String.valueOf(year)).append("年").append(String.valueOf(month+1)).append("月").append(day).append("日").toString();
        now_time=time.append(String.valueOf(hour)).append("时").append(String.valueOf(minute)).append("分").toString();
        linearLayouTitle=(LinearLayout)findViewById(R.id.linearLayout_set_title);
        editText_beizhu=(EditText)findViewById(R.id.edit_text_remark);
        editText_biaoti=(EditText)findViewById(R.id.edit_text_title);
        insertPosition=getIntent().getIntExtra("edit_position",0);

        linearLayoutCycle=(LinearLayout)findViewById(R.id.linearLayout_set_cycle);
        cycleView = (TextView) this.findViewById(R.id.text_view_cycleInstruction);
        linearLayoutCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCycle();
            }
        });

        linearLayoutPicture=(LinearLayout)findViewById(R.id.linearLayout_set_image);
        linearLayoutPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSelectPictureManager();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_biaoti.getText().toString().equals(""))
                    Toast.makeText(EditScheduleActivity.this, "标题不能为空", Toast.LENGTH_LONG).show();
                else {
                    Intent intent = new Intent();
                    intent.putExtra("edit_position", insertPosition);
                    intent.putExtra("schedule_title", editText_biaoti.getText().toString());
                    intent.putExtra("schedule_remark", editText_beizhu.getText().toString());

                    if(dateView.getText().toString().equals("长按使用日期计算器")) {
                        intent.putExtra("schedule_date", now_riqi);
                        intent.putExtra("schedule_time",now_time);
                    }
                    else {
                        intent.putExtra("schedule_date", dateView.getText().toString());
                        if(timeView.getText().toString().equals(""))
                            intent.putExtra("schedule_time",now_time);
                       else
                           intent.putExtra("schedule_time",timeView.getText().toString());
                    }
                    setResult(RESULT_OK, intent);
                    EditScheduleActivity.this.finish();
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditScheduleActivity.this.finish();
            }
        });
    }

private void setCycle(){
        //点击重复设置显示对话框
        final String[] itemsCycle = new String[]{"每周", "每月", "每年", "自定义","无"};
        new AlertDialog.Builder(context).setTitle("周期")
                .setItems(itemsCycle, new DialogInterface.OnClickListener() {//设计点击事件
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0)
                            cycleView.setText("每周");
                        else if(i==1)
                            cycleView.setText("每月");
                        else if(i==2)
                            cycleView.setText("每年");
                        else if(i==3){
                            final EditText cycle_editText = new EditText(context);
                            cycle_editText.setFocusable(true);
                            cycle_editText.setHint("输入周期（天）");
                            cycle_editText.setInputType( InputType.TYPE_CLASS_NUMBER);
                            new AlertDialog.Builder(context).setTitle("周期")
                                    .setView(cycle_editText)
                                    .setNegativeButton("取消",null)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(cycle_editText.getText().toString().equals(""))
                                                cycleView.setText("无");
                                            else
                                                cycleView.setText(cycle_editText.getText()+"天");
                                        }
                                    }).show();
                        }
                        else
                            cycleView.setText("无");

                    }
                })
                .create().show();
    }
    //参考网址https://www.jianshu.com/p/0b1f7a805450
    private void initDateTime(){
        //初始化控件
        linearLayoutDate = (LinearLayout) findViewById(R.id.linearLayout_set_date);
        dateView = (TextView) findViewById(R.id.text_view_dateInstruction);
        timeView = (TextView) findViewById(R.id.text_view_timeInstruction);
        linearLayoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (time.length() > 0) { //清除上次记录的日期
                            time.delete(0, time.length());
                        }
                        timeView.setText(time.append(String.valueOf(hour)).append("时").append(String.valueOf(minute)).append("分"));
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog2 = builder2.create();
                View dialogView2 = View.inflate(context, R.layout.dialog_time, null);
                TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);
                timePicker.setIs24HourView(true); //设置24小时制
                //时间改变监听事件
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        EditScheduleActivity.this.hour = hourOfDay;
                        EditScheduleActivity.this.minute = minute;
                    }
                });
                dialog2.setTitle("设置时间");
                dialog2.setView(dialogView2);
                dialog2.show();


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        dateView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month+1)).append("月").append(day).append("日"));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(context, R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

                dialog.setTitle("设置日期");
                dialog.setView(dialogView);
                dialog.show();

                //初始化日期监听事件
                datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        EditScheduleActivity.this.year = year;
                        EditScheduleActivity.this.month = monthOfYear;
                        EditScheduleActivity.this.day = dayOfMonth;
                    }
                });
        }
        });
    }


    /**
     * 获取当前的日期和时间
     */
    private void initDateView() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }
    void initSelectPictureManager() {
        selectPictureManager = new SelectPictureManager(this);
        selectPictureManager.setPictureSelectListner(new SelectPictureManager.PictureSelectListner() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPictureSelect(String imagePath) {
                if(imagePath != null){
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    linearLayouTitle.setBackground( new BitmapDrawable(getResources(),bitmap));
                }else {
                    Toast.makeText(context,"获取图片失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void throwError(Exception e) {
                e.printStackTrace();
            }
        });
        selectPictureManager.setNeedCrop(true);//需要裁剪
        selectPictureManager.setOutPutSize(400, 400);//输入尺寸
        selectPictureManager.setContinuous(true);//设置连拍
        selectPictureManager.showSelectPicturePopupWindow(this.getWindow().getDecorView());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectPictureManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        selectPictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}