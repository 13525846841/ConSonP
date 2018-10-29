package com.yksj.consultation.son.smallone.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import com.yksj.consultation.son.smallone.event.TalkEvent;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created by HEKL on 16/5/11.
 * Used for
 */

public class MusicService extends Service {
    public static final int CREATE = 0;
    public static final int START = 1;
    public static final int PAUSR = 2;
    public static final int STOP = 3;
    private MediaPlayer player;//声明一个MediaPlayer对象

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO 自动生成的方法存根
        return null;
    }

    //创建服务
    @Override
    public void onCreate() {
        // 当player对象为空时

        super.onCreate();
    }

    //销毁服务
    @Override
    public void onDestroy() {
        //当对象不为空时
        if (player != null) {
            player.stop();//停止播放
            player.release();//释放资源
            player = null;//把player对象设置为null
        }
        super.onDestroy();
    }
    String url;
    //开始服务
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO 自动生成的方法存根
        if (intent==null||intent.getExtras()==null){
            return super.onStartCommand(intent, flags, startId);
        }
        Bundle b = intent.getExtras();//获取到从MainActivity类中传递过来的Bundle对象
        int op = b.getInt("msg");//再获取到MainActivity类中op的值
        switch (op) {
            case 0://当op为1时，即点击播放按钮时
                if (player == null) {
                    url=b.getString("Url");
                    try {
                        player = MediaPlayer.create(MusicService.this, Uri
                                .parse(b.getString("Url")));//实例化对象，通过播放本机服务器上的一首音乐
                        player.setLooping(false);//设置不循环播放
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                TalkEvent talkEvent = new TalkEvent();
                                talkEvent.setWhat(TalkEvent.STOPAN);
//                                talkEvent.setObject(suGroup);
                                EventBus.getDefault().post(talkEvent);
                            }
                        });
                        player.start();


                    }catch (Exception e){

                    }
                } else {
                    if(!url.equals(b.getString("Url"))){
                        url=b.getString("Url");

                        try {
                            player.reset();
                            player.setDataSource(MusicService.this ,Uri
                                    .parse(b.getString("Url")));

                            player.setLooping(false);//设置不循环播放
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else{

                        //player.seekTo(0);
                        play();
                    }

                }
                break;
            case 1://当op为1时，即点击播放按钮时
                play();//调用play()方法
                break;
            case 2://当op为2时，即点击暂停按钮时
                pause();//调用pause()方法
                break;
            case 3://当op为3时，即点击停止按钮时
                stop();//调用stop()方法
                break;
            default:
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    //停止播放音乐方法
    private void stop() {
        // 当player对象不为空时
        if (player != null) {
            player.seekTo(0);//设置从头开始
            player.stop();//停止播放
            try {
                player.prepare();//预加载音乐
            } catch (IllegalStateException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            } catch (IOException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }
    }

    //暂停播放音乐方法
    private void pause() {
        // 当player对象正在播放时并且player对象不为空时
        if (player.isPlaying() && player != null) {
            player.pause();//暂停播放音乐
        }
    }

    //播放音乐方法
    private void play() {
        // 当player对象不为空并且player不是正在播放时
        if (player != null && !player.isPlaying()) {
            player.start();//开始播放音乐
        }else if(player.isPlaying()){
            player.seekTo(0);

        }
    }

    public static void Play(Context context, String url, int event) {
        Intent intent = new Intent(context, MusicService.class);//实例化一个Intent对象


        Bundle bundle = new Bundle();//实例化一个Bundle对象
        bundle.putInt("msg", event);//把op的值放入到bundle对象中
        bundle.putString("Url", url);//把op的值放入到bundle对象中
        intent.putExtras(bundle);//再把bundle对象放入intent对象中
        context.startService(intent);//开启这个服务
    }
    public static void stop(Context context) {
        Intent intent = new Intent(context, MusicService.class);//实例化一个Intent对象


        Bundle bundle = new Bundle();//实例化一个Bundle对象
        bundle.putInt("msg", 3);//把op的值放入到bundle对象中
        bundle.putString("Url", "");//把op的值放入到bundle对象中
        intent.putExtras(bundle);//再把bundle对象放入intent对象中
        context.startService(intent);//开启这个服务
    }
}
