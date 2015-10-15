package com.jalen.screenrecord.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jalen.screenrecord.R;
import com.jalen.screenrecord.activity.VideoPlayer;
import com.jalen.screenrecord.bean.VideoBean;
import com.jalen.screenrecord.util.VideoThumbnailLoader;

import java.util.List;

/**
 * 视频列表数据适配器
 * Created by xxx on 2015/8/28.
 */
public class VideoAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<VideoBean> mData;

    public VideoAdapter(Context context, List<VideoBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.mData == null ? 0 : this.mData.size();
    }

   /* @Override
    public int getCount() {
        if (mData == null){
            return 0;
        }else {
            return mData.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_record_video, parent, false);
            holder = new ItemViewHolder(convertView);
        }else {
            holder = (ItemViewHolder) convertView.getTag();
        }

        VideoBean videoBean = mData.get(position);
        holder.tvVideoName.setText(videoBean.getVideoName());
        // 加载视频缩略图
        VideoThumbnailLoader.getInstance().displayThumbnail(holder.ivVideoThumb, videoBean.getVideoPath(),R.drawable.ic_movie_bg);
        holder.ivVideoThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoBean videoBean = (VideoBean) mData.get(position);
                Intent intent2player = new Intent(mContext, VideoPlayer.class);
                intent2player.putExtra(VideoPlayer.EXTRA_VIDEO_PATH, mData.get(position).getVideoPath());
                mContext.startActivity(intent2player);
            }
        });
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 视频文件分享
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 视频文件删除
            }
        });

        return convertView;
    }


    public List<VideoBean> getData() {
        return mData;
    }

    public void setmData(List<VideoBean> mData) {
        this.mData = mData;
    }

    class ItemViewHolder {
        TextView tvVideoName;
        ImageView ivVideoThumb;
        Button btnShare;
        Button btnDelete;

        ItemViewHolder(View view){
            tvVideoName = (TextView) view.findViewById(R.id.tv_video_name);
            ivVideoThumb = (ImageView) view.findViewById(R.id.iv_video_thumb);
            btnShare = (Button) view.findViewById(R.id.btn_share);
            btnDelete = (Button) view.findViewById(R.id.btn_delete);

            view.setTag(this);
        }

    }*/
}
