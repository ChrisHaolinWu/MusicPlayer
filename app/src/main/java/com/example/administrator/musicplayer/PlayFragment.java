package com.example.administrator.musicplayer;

import com.example.administrator.musicplayer.R.drawable;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/5/6.
 */

public class PlayFragment extends Fragment {
    private String Song; //音乐名
    private String Singer; //歌手名
    private TextView txtPlaying; //正在播放的歌曲
    public boolean isSeekBarChanging; //是否改变进度条
    public int currentPosition; //音乐正在播放的位置
    public SeekBar seekBar; //进度条
    public int play_mode = 0;//播放模式0为列表循环播放 1为随机播放
    private List<MusicBean> list;//音乐列表的获取
    public int listSize;//列表内项的数目
    public int playPosition; //获取在音乐列表所点击的第几首歌
    public int isPlaying = 0;//判断是否在播放0不是 1为是
    public Button btnStart; //开始按钮
    public Button btnLast; //上一首按钮
    public Button btnNext;//下一首按钮
    public Button btnPlayMode;//播放模式按钮
    public MediaPlayer mediaPlayer = new MediaPlayer();//new一个mediaPlayer实例

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.play, container, false);
        //实例化按钮以及设定监听器
        btnStart = (Button) view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new ifPlayButton());
        btnLast = (Button) view.findViewById(R.id.btnLast);
        btnLast.setOnClickListener(new lastSong());
        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new nextSong());
        btnPlayMode = (Button) view.findViewById(R.id.btnPlayMode);
        btnPlayMode.setOnClickListener(new SwitchModeButton());
        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new changeSeekbar());
        txtPlaying = (TextView) view.findViewById(R.id.txtPlaying);
        //获取音乐列表
        list = new ArrayList<>();
        list = MusicQuerry.getMusicData(this.getActivity());
        listSize = list.size();
        //初始化播放模式按钮的样式
        if (play_mode == 0) {
            Resources resources = getResources();
            Drawable btnDrawable = resources.getDrawable(drawable.loop);
            btnPlayMode.setBackground(btnDrawable);
        } else {
            Resources resources = getResources();
            Drawable btnDrawable = resources.getDrawable(drawable.random);
            btnPlayMode.setBackground(btnDrawable);
        }
        //初始化暂停播放按钮的样式
        if (isPlaying == 0) {
            Resources resources = getResources();
            Drawable btnDrawable = resources.getDrawable(drawable.play);
            btnStart.setBackground(btnDrawable);
        } else {
            Resources resources = getResources();
            Drawable btnDrawable = resources.getDrawable(drawable.pause);
            btnStart.setBackground(btnDrawable);
        }
        //获取到在我的音乐列表所点击的歌曲在列表的位置
        playPosition = getArguments().getInt("MusicPosition");
        System.out.println(playPosition);
        System.out.println(list.get(playPosition).Song);
        return view;
    }

    class lastSong implements View.OnClickListener {
        public void onClick(View view) {
            //判断当前播放模式再选择切歌的方式
            if (mediaPlayer.isPlaying()) {
                if (play_mode == 0) {
                    //列表循环模式上一首
                    Loop_Mode_LastSong();
                    //播放音乐进度变为0
                    currentPosition = 0;
                } else {
                    //随机模式的切歌
                    Random_Mode_Switch();
                    currentPosition = 0;
                }
                System.out.println(playPosition);
                System.out.println(list.get(playPosition).Song);
                //显示正在播放的歌曲和歌手
                Song = list.get(playPosition).Song;
                Singer = list.get(playPosition).Singer;
                txtPlaying.setText("当前播放歌曲：" + Song + "-----" + Singer);
            }
        }
    }

    class nextSong implements View.OnClickListener {
        public void onClick(View view) {
            if (mediaPlayer.isPlaying()) {
                if (play_mode == 0) {
                    Loop_Mode_NextSong();
                    currentPosition = 0;
                } else {
                    Random_Mode_Switch();
                    currentPosition = 0;
                }
                System.out.println(playPosition);
                System.out.println(list.get(playPosition).Song);
                Song = list.get(playPosition).Song;
                Singer = list.get(playPosition).Singer;
                txtPlaying.setText("当前播放歌曲：" + Song + "-----" + Singer);
            }
        }
    }

    class ifPlayButton implements View.OnClickListener {
        public void onClick(View view) {
            //判断是否在播放 改变播放暂停按钮图标样式
            if (mediaPlayer.isPlaying()) {
                pause();
                //获取当前暂停的音乐进度
                currentPosition=mediaPlayer.getCurrentPosition();
            } else {
                mediaPlayer.start();
                Resources resources = getResources();
                Drawable btnDrawable = resources.getDrawable(drawable.pause);
                btnStart.setBackground(btnDrawable);
                isPlaying = 1;
            }
        }
    }

    class SwitchModeButton implements View.OnClickListener {
        public void onClick(View view) {
            //判断播放模式，改变图标样式 改变播放模式
            if (play_mode == 0) {
                RandomMode();
                Resources resources = getResources();
                Drawable btnDrawable = resources.getDrawable(drawable.random);
                btnPlayMode.setBackground(btnDrawable);
                play_mode = 1;
            } else {
                LoopMode();
                Resources resources = getResources();
                Drawable btnDrawable = resources.getDrawable(drawable.loop);
                btnPlayMode.setBackground(btnDrawable);
                play_mode = 0;
            }
        }
    }
    //随机播放模式
    public void RandomMode() {
        //监听音乐播放结束函数
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playPosition = (int) (Math.random() * (listSize - 1));
                System.out.println(playPosition);
                System.out.println(list.get(playPosition).Song);
                playMusic(list.get(playPosition).Path);
            }
        });
    }
    //随机播放模式
    public void LoopMode() {
        //监听音乐播放结束函数
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (playPosition == (listSize - 1)) {
                    playPosition = 0;
                    System.out.println(playPosition);
                    System.out.println(list.get(playPosition).Song);
                    playMusic(list.get(playPosition).Path);
                } else {
                    playPosition++;
                    System.out.println(playPosition);
                    System.out.println(list.get(playPosition).Song);
                    playMusic(list.get(playPosition).Path);
                }
            }
        });
    }
    //随机播放模式的切歌函数
    public void Random_Mode_Switch() {
        playPosition = (int) (Math.random() * (listSize - 1));
        Song = list.get(playPosition).Song;
        Singer = list.get(playPosition).Singer;
        txtPlaying.setText("当前播放歌曲：" + Song + "-----" + Singer);
        playMusic(list.get(playPosition).Path);
    }
    //列表循环下一首方法
    public void Loop_Mode_NextSong() {
        if (playPosition == (list.size() - 1)) {
            playPosition = 0;
            playMusic(list.get(playPosition).Path);
        } else {
            playPosition++;
            playMusic(list.get(playPosition).Path);
        }
    }
    //列表循环上一首方法
    public void Loop_Mode_LastSong() {
        if (playPosition <= 0) {
            playPosition = list.size() - 1;
            playMusic(list.get(playPosition).Path);
        } else {
            playPosition--;
            playMusic(list.get(playPosition).Path);
        }
    }
    //暂停
    public void pause() {
        mediaPlayer.pause();
        Resources resources = getResources();
        Drawable btnDrawable = resources.getDrawable(R.drawable.play);
        btnStart.setBackground(btnDrawable);
        isPlaying = 0;
    }
    //播放音乐的方法
    public void playMusic(String Path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(Path);
            mediaPlayer.prepareAsync();
            //音频准备完毕会响应此方法
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    Song = list.get(playPosition).Song;
                    Singer = list.get(playPosition).Singer;
                    txtPlaying.setText("当前播放歌曲：" + Song + "-----" + Singer);
                    //拖动进度条时使音乐前进到进度条所在的进度
                    mediaPlayer.seekTo(currentPosition);
                    //音乐长度设置进度条最大值
                    seekBar.setMax(list.get(playPosition).Duration);
                    Resources resources = getResources();
                    Drawable btnDrawable = resources.getDrawable(drawable.pause);
                    btnStart.setBackground(btnDrawable);
                    isPlaying = 1;
                    //定时器定时更新进度条
                    Timer timer = new Timer();
                    //设置任务0秒后立刻执行这个方法，调用后每隔50毫秒就调用一次run()方法
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!isSeekBarChanging) {
                                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            }
                        }
                    }, 0, 50);
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    //进度条监听器
    public class changeSeekbar implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }
        //触摸时暂停计时器
        public void onStartTrackingTouch(SeekBar seekBar) {
             isSeekBarChanging=true;
        }
        //滑动结束后设置值
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }
}

