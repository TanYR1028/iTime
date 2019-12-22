package com.example.itime;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.itime.data.FileDataSource;
import com.example.itime.data.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static final int CONTEXT_MENU_DELETE = 1;
    public static final int CONTEXT_MENU_UPDATE = CONTEXT_MENU_DELETE + 1;
    public static final int REQUEST_CODE_UPDATE_SCHEDULE = 902;
    public static final int REQUEST_CODE_NEW_SCHEDULE = 901;
    private ListView listViewSchedules;
    private ArrayList<Schedule> listSchedules = new ArrayList<>();
    private ScheduleAdapter scheduleAdapter;
    private FileDataSource fileDataSource;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitData();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditScheduleActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_SCHEDULE);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_sign, R.id.nav_widget,
                R.id.nav_color, R.id.nav_prime, R.id.nav_setting, R.id.nav_about, R.id.nav_help)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        listViewSchedules = (ListView) this.findViewById(R.id.listview);
        scheduleAdapter = new ScheduleAdapter(MainActivity.this, R.layout.list_view_schedule, listSchedules);
        listViewSchedules.setAdapter(scheduleAdapter);
        this.registerForContextMenu(listViewSchedules);
        listViewSchedules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Schedule schedule = listSchedules.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("photo", schedule.getCoverResourceId());
                bundle.putString("message_title", schedule.getTitle());
                bundle.putString("message_deadline", schedule.getDeadline());
                bundle.putString("message_time", schedule.getDdl_time());
                bundle.putString("message_remark",schedule.getRemark());
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this, Countdown.class);
                startActivity(intent);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fileDataSource.save();
    }
    //修改主题颜色——没啥用
    public void changeThemeColor(int color){
        if(color==0)
            Toast.makeText(this, "颜色为空", Toast.LENGTH_SHORT).show();
        else {
            toolbar.setBackgroundColor(color);
            fab.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == findViewById(R.id.listview)) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(listSchedules.get(info.position).getTitle());
            //添加内容
            menu.add(0, CONTEXT_MENU_DELETE, 0, "删除");
            menu.add(0, CONTEXT_MENU_UPDATE, 0, "更新");
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case CONTEXT_MENU_UPDATE: {
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Schedule schedule = listSchedules.get(menuInfo.position);

                Intent intent = new Intent(MainActivity.this, EditScheduleActivity.class);
                intent.putExtra("edit_position", menuInfo.position);
                intent.putExtra("schedule_title", schedule.getTitle());
                intent.putExtra("schedule_remark", schedule.getRemark());
                //startActivityForResult()方法是主活动MainActivity用来启动EditScheduleActivity的
                startActivityForResult(intent, REQUEST_CODE_UPDATE_SCHEDULE);
                break;
            }
            case CONTEXT_MENU_DELETE: {
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                final int itemPosition = menuInfo.position;
                new android.app.AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("询问")
                        .setMessage("你确定要删除这条吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listSchedules.remove(itemPosition);
                                scheduleAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create().show();
                break;
            }

        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_NEW_SCHEDULE:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("edit_position", 0);
                    String title = data.getStringExtra("schedule_title");
                    String remark = data.getStringExtra("schedule_remark");
                    String deadline = data.getStringExtra("schedule_date");
                    String time = data.getStringExtra("schedule_time");

                    getListSchedules().add(position, new Schedule(title,R.drawable.windwill, remark, deadline, time));
                    //通知适配器已改变
                    scheduleAdapter.notifyDataSetChanged();

                    Toast.makeText(this, "新建成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_UPDATE_SCHEDULE:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("edit_position", 0);
                    String title = data.getStringExtra("schedule_title");
                    String remark = data.getStringExtra("schedule_remark");
                    String deadline = data.getStringExtra("schedule_date");
                    String time = data.getStringExtra("schedule_time");
                    Schedule schedule = getListSchedules().get(position);

                    schedule.setTitle(title);
                    schedule.setRemark(remark);
                    schedule.setDeadline(deadline);
                    schedule.setDdl_time(time);
                    //通知适配器已改变
                    scheduleAdapter.notifyDataSetChanged();

                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public List<Schedule> getListSchedules() {
        return listSchedules;
    }

    private void InitData() {
        fileDataSource = new FileDataSource(this);
        listSchedules = fileDataSource.load();

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
                return "已经\n" + guoqu / 1000 + "秒";
            else if (guoqu < 60 * 60 * 1000) {
                Long guoquM = guoqu/ (1000 * 60);
                return "已经\n" + guoquM + "分";
            } else if (guoqu < 60 * 60 * 24 * 1000) {
                Long guoquH = guoqu / (1000 * 60 * 60);

                return "已经\n" + guoquH + "时" ;
            } else { //(timeLong<60*60*24*1000*7)
                Long guoquD = guoqu/ (1000 * 60 * 60 * 24);
                return "已经\n" + guoquD + "天" ;
            }
        }
        else {
            if (timeLong < 60 * 1000)
                return "只剩\n" + timeLong / 1000 + "秒";
            else if (timeLong < 60 * 60 * 1000) {
                Long timeLongM = timeLong / (1000 * 60);
                return "只剩\n" + timeLongM + "分";
            } else if (timeLong < 60 * 60 * 24 * 1000) {
                Long timeLongH = timeLong / (1000 * 60 * 60);

                return "只剩\n" + timeLongH + "时" ;
            } else { //(timeLong<60*60*24*1000*7)
                Long timeLongD = timeLong / (1000 * 60 * 60 * 24);
                return "只剩\n" + timeLongD + "天" ;
            }
        }
    }

    public class ScheduleAdapter extends ArrayAdapter<Schedule> {

        private int resourceId;

        public ScheduleAdapter(Context context, int resource, List<Schedule> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater mInflater = LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(this.resourceId, null);

            ImageView scheduleImage = item.findViewById(R.id.image_view_schedule);
            final TextView scheduleTianshu=item.findViewById(R.id.text_time);
            TextView scheduleTitle = item.findViewById(R.id.text_view_schedule);

            final Schedule schedule_item = this.getItem(position);
            scheduleImage.setImageResource(schedule_item.getCoverResourceId());
            scheduleTitle.setText(schedule_item.getTitle() + "\n" + schedule_item.getDeadline() + "\n" + schedule_item.getRemark());
            final String str=schedule_item.getDeadline()+" "+schedule_item.getDdl_time();
            // scheduleTianshu.setText(ShengYuShiJian(schedule_item.getDeadline()));
            new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO Auto-generated method stub
                    String shijian=ShengYuShiJian(str);
                    scheduleTianshu.setText(shijian);
                }
                @Override
                public void onFinish() {

                }
            }.start();
            return item;
        }
    }

}
