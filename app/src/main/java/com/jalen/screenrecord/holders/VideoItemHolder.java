package com.jalen.screenrecord.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jalen.screenrecord.R;

/**
 * Created by joyce on 2015/10/16.
 */
public class VideoItemHolder extends RecyclerView.ViewHolder {
    TextView tvVideoName;
    ImageView ivVideoThumb;
    Button btnShare;
    Button btnDelete;

    public VideoItemHolder(View itemView) {
        super(itemView);

        tvVideoName = (TextView) itemView.findViewById(R.id.tv_video_name);
        ivVideoThumb = (ImageView) itemView.findViewById(R.id.iv_video_thumb);
        btnShare = (Button) itemView.findViewById(R.id.btn_share);
        btnDelete = (Button) itemView.findViewById(R.id.btn_delete);
    }

    /**
     * Item点击事件监听器
     */
    public interface OnVideoItemClickListener {
        /**
         * 缩略图被点击
         */
        void onImageClick();
    }
}
