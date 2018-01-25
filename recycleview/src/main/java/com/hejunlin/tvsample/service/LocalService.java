package com.hejunlin.tvsample.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.hejunlin.tvsample.LogUtils;

public class LocalService extends Service {

    private MyBinder binder;
    private MyServiceConnection conn;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return binder;
    }
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        if(binder ==null){
            binder = new MyBinder();
        }
        conn = new MyServiceConnection();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                LogUtils.e(11111);
//            }
//        },500);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    SystemClock.sleep(500);
                    LogUtils.e(11111);
                }

            }
        }).start();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), conn, Context.BIND_IMPORTANT);
        
        PendingIntent contentIntent = PendingIntent.getService(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("XXX")
        .setContentIntent(contentIntent)
        .setContentTitle("我是XXX，我怕谁!")
        .setAutoCancel(true)
        .setContentText("哈哈")
        .setWhen( System.currentTimeMillis());
        
        //把service设置为前台运行，避免手机系统自动杀掉改服务。
        startForeground(startId, builder.build());
        return START_STICKY;
    }
    

    class MyBinder extends IMyAidlInterface.Stub{

        @Override
        public String getProcessName() throws RemoteException {
            // TODO Auto-generated method stub
            return "LocalService";
        }
        
    }
    
    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
         LogUtils.e( "建立连接成功！");
            
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
           LogUtils.e( "RemoteService服务被干掉了~~~~断开连接！");
            Toast.makeText(LocalService.this, "断开连接", Toast.LENGTH_SHORT).show();

            //启动被干掉的
            LocalService.this.startService(new Intent(LocalService.this, RemoteService.class));
            LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), conn, Context.BIND_IMPORTANT);
        }
        
    }
    

}
