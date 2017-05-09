package com.example.administrator.musicplayer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/6.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<MusicBean> list;
    public MyAdapter(MusicListFragment musicListFragment,List<MusicBean> list){
        this.context=musicListFragment.getActivity();
        this.list=list;
    }
    public int getCount(){
        return list.size();
    }
    public Object getItem(int i){
        return list.get(i);
    }
    public long getItemId(int i){
        return i;
    }
    public View getView(int i, View view, ViewGroup viewGroup){
        //一个装载信息的类
        ViewHolder holder=null;
        if(view==null){
            holder=new ViewHolder();
            //引入布局
            view=view.inflate(context,R.layout.item_music,null);
            //实例化对象
            holder.Song=(TextView)view.findViewById(R.id.item_Song);
            holder.Singer=(TextView)view.findViewById(R.id.item_Singer);
            holder.Duration=(TextView)view.findViewById(R.id.item_Duration);
            holder.Position=(TextView)view.findViewById(R.id.item_Position);
            view.setTag(holder);
        }else {
            holder=(ViewHolder)view.getTag();
        }
        //给控件一个个地赋值
        holder.Song.setText(list.get(i).Song.toString());
        holder.Singer.setText(list.get(i).Singer.toString());
        int Duration=list.get(i).Duration;
        String time=MusicQuerry.formatTime(Duration);
        holder.Duration.setText(time);
        holder.Position.setText(i+1+"");
        return view;
    }
    class ViewHolder{
        TextView Song;
        TextView Singer;
        TextView Duration;
        TextView Position;
    }
}
