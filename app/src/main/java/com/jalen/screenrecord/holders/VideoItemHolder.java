package com.jalen.screenrecord.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jalen.screenrecord.R;
import com.jalen.screenrecord.bean.VideoBean;

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

    public TextView getTvVideoName() {
        return tvVideoName;
    }

    public ImageView getIvVideoThumb() {
        return ivVideoThumb;
    }

    public Button getBtnShare() {
        return btnShare;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

}
