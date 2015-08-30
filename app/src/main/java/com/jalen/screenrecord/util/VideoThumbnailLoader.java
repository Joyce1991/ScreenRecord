package com.jalen.screenrecord.util;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 实现图片异步加载
 * <div>参考资料：<a href="http://blog.csdn.net/geniusxiaoyu/article/details/7470163"> Android异步加载网络图片</a></div>
 * Created by xxx on 2015/8/30.
 */
public class VideoThumbnailLoader {
    private static VideoThumbnailLoader mInstance;
    private static final String tag = "VideoThumbnailLoader";
    /**
     * 缓存加载过的图片资源
     */
    private Map<String, SoftReference<Bitmap>> caches;

    private List<LoadTask> mTaskQueue;

    public static VideoThumbnailLoader getInstance(){
        if (mInstance == null){
            mInstance = new VideoThumbnailLoader();
        }
        return mInstance;
    }
    private VideoThumbnailLoader(){
        caches = new HashMap<>();
        mTaskQueue = new ArrayList<>();
        // 启动图片下载线程
        isRunning = true;
        runnable = new LoadRunnable();
        new Thread(runnable).start();
    }

    /**
     * 显示图片
     * @param imageView 显示图片的view
     * @param videoPath 图片源
     * @param resId 加载过程中显示的图片
     */
    public void displayThumbnail(ImageView imageView, String videoPath, int resId) {
        imageView.setTag(videoPath);
        Bitmap bitmap = loadImage(videoPath, getImageCallback(imageView, resId));

        if (bitmap == null){
            imageView.setImageResource(resId);
        }else {
            imageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap loadImage(String videoPath, ImageCallback callback) {
        // 判断缓存中是否已经存在该图片
        if (caches .containsKey(videoPath)){
            SoftReference<Bitmap> softReference = caches.get(videoPath);
            Bitmap bitmap = softReference.get();
            if (bitmap == null){
                caches.remove(videoPath);
            }else {
                Log.d(tag, "从缓存中获取了该图片：" + videoPath);
                return bitmap;
            }
        }else {
            // 如果缓存中不存在该图片，则创建图片解析任务
            LoadTask task = new LoadTask();
            task.path = videoPath;
            task.callback = callback;
            Log.d(tag, "创建了新LoadTask，" + videoPath);
            if (!mTaskQueue.contains(task)){
                mTaskQueue.add(task);
                Log.d(tag, "添加新的任务：" + videoPath);
                // 唤醒任务下载队列
                synchronized (runnable) {
                    runnable.notify();
                }
            }
        }
        // 缓存中没有图片则返回null
        return null;
    }

    //回调接口
    public interface ImageCallback{
        void loadImage(String path, Bitmap bitmap);
    }

    /**
     * 任务类
     */
    class LoadTask{
        /**
         * 视频文件路径
         */
        String path;
        /**
         * 加载出来的图片
         */
        Bitmap bitmap;
        /**
         * 回调对象
         */
        ImageCallback callback;

        @Override
        public boolean equals(Object o) {
            // 根据path值来判断两个LoadTask是否是同一个
            LoadTask task = (LoadTask)o;
            return task.path.equals(path);
        }
    }

    private LoadRunnable runnable;
    private boolean isRunning = false;
    class LoadRunnable implements Runnable {
        @Override
        public void run() {
            while (isRunning){
                while (mTaskQueue.size() > 0){
                    LoadTask task = mTaskQueue.remove(0);
                    // 将解析出来的图片添加到缓存中
                    task.bitmap = ImageUtil.getVideoThumbnail(task.path);
                    Log.d(tag, "解析出图片：" + task.path);
                    caches.put(task.path, new SoftReference<Bitmap>(task.bitmap));
                    if(handler != null){
                        // 创建消息对象，并将完成的任务添加到消息对象中
                        Message msg = handler.obtainMessage();
                        msg.obj = task;
                        // 发送消息回主线程
                        handler.sendMessage(msg);
                    }
                }

                //如果队列为空,则令线程等待
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     *
     * @param imageView
     * @param resId 图片加载完成前显示的图片资源ID
     * @return
     */
    private ImageCallback getImageCallback(final ImageView imageView, final int resId){
        return new ImageCallback() {
            @Override
            public void loadImage(String path, Bitmap bitmap) {
                if(path.equals(imageView.getTag().toString())){
                    imageView.setImageBitmap(bitmap);
                }else{
                    imageView.setImageResource(resId);
                }
            }
        };
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // 子线程中返回的下载完成的任务
            LoadTask task = (LoadTask)msg.obj;
            // 调用callback对象的loadImage方法，并将图片路径和图片回传给adapter
            task.callback.loadImage(task.path, task.bitmap);
        }

    };
}
