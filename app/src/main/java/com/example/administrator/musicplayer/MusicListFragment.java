package com.example.administrator.musicplayer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/6.
 */

public class MusicListFragment extends Fragment {
    String MusicPath;//音乐路径
    public int listSize;//音乐列表大小
    public int playPosition;//位置
    private ListView MusicList;//列表
    private List<MusicBean> list;//音乐表
    private MyAdapter adapter;//装载的适配器
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.musiclist,container,false);
        MusicList=(ListView)view.findViewById(R.id.MusicList) ;
        initView();//构造listview的方法
        return view;
    }
    private void initView(){
        list=new ArrayList<>();
        list=MusicQuerry.getMusicData(this.getActivity());
        adapter=new MyAdapter(this,list);
        MusicList.setAdapter(adapter);
        //列表点击监听器
        MusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //PlayFragment playFragment=new PlayFragment();
                MusicPlayer mp=(MusicPlayer)getActivity();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                playPosition=i;
                listSize=list.size();
                MusicPath=list.get(i).Path;
                //打包传数据给播放界面
                Bundle bundle=new Bundle();
                bundle.putInt("MusicPosition",playPosition);
                mp.playFragment.setArguments(bundle);
                //点击后显示去播放界面的按钮
                Button btnPlaying=(Button)getActivity().findViewById(R.id.btnPlaying);
                btnPlaying.setVisibility(View.VISIBLE);
                transaction.replace(R.id.main,mp.playFragment);
                transaction.commit();
                int currentPosition=0;
                //调用playFragment的palyMusic方法播放
                mp.playFragment.playMusic(list.get(i).Path);
            }
        });
    }
}
