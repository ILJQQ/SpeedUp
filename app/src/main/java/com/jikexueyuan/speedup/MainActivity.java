package com.jikexueyuan.speedup;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        调用内存清理方法
        clearMemory(MainActivity.this);
//        关闭应用
        finish();
    }

//    调用系统管理功能清除内存
    private void clearMemory(Context context){
//        实例化获取系统服务对象
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        建立一个用于装载正在运行应用的List
        List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();

//        获取内存清理前可用内存
        long beforMem = getAvailMemory(context);

//        用于进程计数
        int count = 0;
//        当正在运行进程不为空时清理进程
        if (infoList != null) {
//            用循环遍历每个正在运行的进程并逐一关闭
            for (int i = 0; i < infoList.size(); i++) {
                ActivityManager.RunningAppProcessInfo appProcessInfo = infoList.get(i);
                if (appProcessInfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    String[] pkgList = appProcessInfo.pkgList;
                    for (int j = 0; j < pkgList.length; j++) {
                        am.killBackgroundProcesses(pkgList[j]);
                        count++;
                    }
                }

            }
        }
//        获取清理进程后系统空余内存
        long afterMem = getAvailMemory(context);

//        Toast出清理信息
        Toast.makeText(context,"清除了 " + count + " 个进程, " + "共释放了 " + (afterMem - beforMem)
                + "M 内存",Toast.LENGTH_LONG).show();
    }

//    获取空余内存
    private long getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem / (1024 * 1024);
    }
}
