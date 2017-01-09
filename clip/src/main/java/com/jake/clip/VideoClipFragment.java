package com.jake.clip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * @author jake
 * @since 2016/12/30 下午3:04
 */

public class VideoClipFragment extends Fragment implements View.OnClickListener {
    private Button mBtnChooseFile;
    private Button mBtnStart;
    private TextView evPath;
    private ProgressBar progressBar;
    private TextView tvResult;
    private Handler uiHandler;
    private Handler threadHandler;
    private HandlerThread handlerThread;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        handlerThread = new HandlerThread("work");
        handlerThread.start();
        threadHandler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                handlerThreadMessage(msg);
                return false;
            }
        });
        uiHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                handleUiMessage(msg);
                return false;
            }
        });
        return inflater.inflate(R.layout.fragment_videoclip, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (threadHandler != null) {
            threadHandler.getLooper().quit();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setListener();
        evPath.setText(getVideoPathFromCache());
        String text = VideoClipJni.sayHello("jake");
        VideoClipJni.helloJni("jake say ,hello jni!");
        tvResult.setText(text);
    }

    private void setListener() {
        mBtnChooseFile.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
    }

    private void findViews(View view) {
        tvResult = (TextView) view.findViewById(R.id.tv_hello);
        mBtnChooseFile = (Button) view.findViewById(R.id.btn_choose);
        mBtnStart = (Button) view.findViewById(R.id.btn_start);
        evPath = (TextView) view.findViewById(R.id.ev_path);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tag", "onActivityResult");
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String path = getPath(getActivity(), uri);
            cacheVideoPath(path);
            evPath.setText(path);
        }
    }

    private String getVideoPathFromCache() {
        return getActivity().getSharedPreferences("video", Context.MODE_PRIVATE).getString("video_path", null);
    }

    private void cacheVideoPath(String path) {
        SharedPreferences sp = getActivity().getSharedPreferences("video", Context.MODE_PRIVATE);
        sp.edit().putString("video_path", path).apply();
    }

    private String getPath(Context context, Uri uri) {
        String[] filePathColumn = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String videoPath = cursor.getString(1);
        cursor.close();
        return videoPath;
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnChooseFile) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
            startActivityForResult(intent, 1);
        } else if (v == mBtnStart) {
            String videoPath = evPath.getText().toString();
            if (TextUtils.isEmpty(videoPath)) {
                Toast.makeText(v.getContext(), "请选择视频文件", Toast.LENGTH_SHORT).show();
                return;
            }
            uiHandler.sendEmptyMessage(MSG_SHOW_PB);
            Message threadMsg = threadHandler.obtainMessage();
            threadMsg.what = MSG_THREAD_EXTRACT_VIDEO;
            threadMsg.obj = videoPath;
            threadMsg.sendToTarget();

        }
    }

    private String getVideoName(String path) {
        return path.substring(path.lastIndexOf("/"));
    }

    private static final int MSG_SHOW_PB = 0x1;
    private static final int MSG_HIDE_PB = 0x2;
    private static final int MSG_SHOW_RESULT = 0x3;
    private static final int MSG_THREAD_EXTRACT_VIDEO = 0x1;

    private void handleUiMessage(Message msg) {
        switch (msg.what) {
            case MSG_SHOW_PB:
                mBtnStart.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case MSG_HIDE_PB:
                mBtnStart.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
                break;
            case MSG_SHOW_RESULT:
                String result = (String) msg.obj;
                tvResult.setText(result);
                uiHandler.sendEmptyMessage(MSG_HIDE_PB);
                break;
        }
    }

    private void handlerThreadMessage(Message msg) {
        switch (msg.what) {
            case MSG_THREAD_EXTRACT_VIDEO:
                String videoPath = (String) msg.obj;
                File picPath = new File(Environment.getExternalStorageDirectory(), getVideoName(videoPath));
                if (!picPath.exists()) {
                    picPath.mkdirs();
                }
                String path= picPath.getAbsolutePath();
                Log.d("ndk-log","   path="+path);
                String result = VideoClipJni.videoToPicture(videoPath,path);
                Message message = uiHandler.obtainMessage();
                message.obj = result;
                message.what = MSG_SHOW_RESULT;
                message.sendToTarget();
                break;
        }
    }
}
