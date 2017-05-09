package com.example.administrator.musicplayer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MusicPlayer extends AppCompatActivity{
    public int isPlaying=0;
    private TextView txt1;//欢迎界面的字
    private Button btnPlaying;//正在播放按钮
    private Button btnMusicList;//我的音乐按钮
    //先实例化连个fragment
    MusicListFragment musicListFragment=new MusicListFragment();
    PlayFragment playFragment=new PlayFragment();
    //实例fragmentManger对象
    FragmentManager fragmentManager=getFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        btnPlaying=(Button)findViewById(R.id.btnPlaying);
        btnMusicList=(Button)findViewById(R.id.btnMusicList);
        btnPlaying.setVisibility(View.GONE);
        btnPlaying.setOnClickListener(new toPlayFragment());
        btnMusicList.setOnClickListener(new toMusiclistFragment());
        txt1=(TextView) findViewById(R.id.txt1);
    }
    class toPlayFragment implements OnClickListener{
        public void onClick(View view){
            //PlayFragment playFragment=new PlayFragment();
            //FragmentManager fragmentManager=getFragmentManager();
            //fragment布局间的转换
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            transaction.replace(R.id.main,playFragment);
            transaction.commit();
        }
    }
    class toMusiclistFragment implements OnClickListener{
        public void onClick(View view){
            //MusicListFragment musicListFragment=new MusicListFragment();
            //FragmentManager fragmentManager=getFragmentManager();
            txt1.setText("");
            //fragment布局间的转换
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            transaction.replace(R.id.main,musicListFragment);
            transaction.commit();
        }
    }
}
