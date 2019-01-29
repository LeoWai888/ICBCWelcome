package com.icbc.icbcwelcome.util;

import android.content.Context;
import android.widget.VideoView;

import java.io.File;

public class Vedio {

    VideoView videoView ;

    public Vedio(File file) {

        File file1=new File(String.valueOf(file));

        videoView.setVideoPath(file1.getPath());
    }
   // File file=new File("./mnt/sda/sda1/Awel/test","icbc.mp4");



}
