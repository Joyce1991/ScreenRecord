package com.jalen.screenrecord.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.jalen.screenrecord.R;
import com.jalen.screenrecord.activity.VideoPlayer;
import com.jalen.screenrecord.bean.VideoBean;
import com.jalen.screenrecord.holders.VideoItemHolder;
import com.jalen.screenrecord.util.VideoThumbnailLoader;
import com.jalen.screenrecord.util.ViewUtils;

import java.util.List;

/**
 * 视频列表数据适配器
 * Created by xxx on 2015/8/28.
 */
public class VideoAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private final static int ANIM_LIST_ENTER_DURATION = 700;
    private static final int ANIMATED_ITEMS_COUNT = 3;  // 限定只有几个item进入时是有动画的

    private List<VideoBean> mData;
    private int lastAnimatedPosition = -1;
    private OnVideoItemClickListener listener;

    public VideoAdapter(List<VideoBean> data) {
        this.mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_record_video, parent, false);

        VideoItemHolder itemHolder = new VideoItemHolder(itemView);
        itemHolder.getIvVideoThumb().setOnClickListener(this);
        itemHolder.getBtnDelete().setOnClickListener(this);
        itemHolder.getBtnShare().setOnClickListener(this);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 运行item动画
        runEnterAnimation(holder.itemView, position);


        // 让itemView中的子View和position关联
        VideoItemHolder itemHolder = (VideoItemHolder) holder;
        itemHolder.getBtnShare().setTag(position);
        itemHolder.getBtnDelete().setTag(position);
        itemHolder.getIvVideoThumb().setTag(position);

        // 填充数据到UI
        VideoBean itemData = mData.get(position);
        itemHolder.getTvVideoName().setText(itemData.getVideoName());
        VideoThumbnailLoader.getInstance().displayThumbnail(itemHolder.getIvVideoThumb(), itemData.getVideoPath(), R.drawable.ic_movie_bg);
    }

    @Override
    public int getItemCount() {
        return this.mData == null ? 0 : this.mData.size();
    }

    /**
     * 运行itemView的入动画
     * @param view
     * @param position
     */
    private void runEnterAnimation (View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY (ViewUtils.getScreenHeight(view.getContext()));
            view.animate ()
                    .translationY (0)
                    .setInterpolator (new DecelerateInterpolator(3.f))
                    .setDuration (ANIM_LIST_ENTER_DURATION)
                    .start ();
        }
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        VideoBean itemData = mData.get(position);
        switch (v.getId()) {
            case R.id.iv_video_thumb:
                // 点击缩略图，播放视频
                listener.onImageClick(v, itemData);
                break;
            case R.id.btn_delete:
                // 点击分享，分享该视频
                listener.onShareClick(v, itemData);
                break;
            case R.id.btn_share:
                // 点击删除， 删除该文件
                listener.onDeleteClick(v, itemData);
                break;
        }
    }

    public List<VideoBean> getData() {
        return mData;
    }

    public void setmData(List<VideoBean> mData) {
        this.mData = mData;
    }

    public void setOnVideoItemClickListener (OnVideoItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Item点击事件监听器
     */
    public interface OnVideoItemClickListener {
        /**
         * 缩略图被点击
         * @param view 缩略图ImageView
         * @param videoBean item对象
         */
        void onImageClick(View view, VideoBean videoBean);

        /**
         * 点击删除按钮
         * @param view 删除按钮
         * @param videoBean item对象
         */
        void onDeleteClick(View view, VideoBean videoBean);

        /**
         * 点击分享按钮
         * @param view 分享按钮
         * @param videoBean item对象
         */
        void onShareClick(View view, VideoBean videoBean);
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
