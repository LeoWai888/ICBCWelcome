package com.icbc.icbcwelcome.config;

import android.content.Context;
import android.util.Log;

import com.icbc.icbcwelcome.json.FestivalListBean;
import com.icbc.icbcwelcome.util.checks.ShapeRevealSample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class bgPrama {
    public List<FestivalListBean> festivalBgList=new ArrayList<>();
    private Context mContext;
    private FestivalListBean listBeans;

    public bgPrama(Context context)
    {
        this.mContext=context;
    }

    public List<FestivalListBean> getFestivalBgList() {
        return festivalBgList;
    }

    public void setFestivalBgList(List<FestivalListBean> festivalBgList) {
        this.festivalBgList = festivalBgList;
    }

    public FestivalListBean bgPramaSetting(String fileAbsolutePath)
    {
        File file=new File(fileAbsolutePath);
        File[] subFile=file.listFiles();
        //默认设置
        String MVPath=null;
        String fontsPath="fonts/birthday.ttf";
        String txtColor="white";

        for (int iFileLength=0;iFileLength<subFile.length;iFileLength++)
        {
            if (!subFile[iFileLength].isDirectory())
            {   //遍历文件夹下的视频文件
                String fileName=subFile[iFileLength].getName();

                if (isVideo(fileName))
                {
                    MVPath=fileAbsolutePath+fileName;  //默认设置

                    if (fileName.indexOf("_")!=-1)  //包含"_"
                    {
                        //命名按照"字体_颜色_视频名.格式"，按"_"分割设置字体和颜色
                        String[] all=fileName.split("_");
                        fontsPath="fonts/"+all[0]+".ttf";
                        txtColor=all[1];
                    }
                    else{
                        //命名不按"字体_颜色_视频名.格式"，赋予默认字体和颜色

                    }
                    FestivalListBean BgListBean_ = new FestivalListBean(MVPath, fontsPath, txtColor);
                    festivalBgList.add(BgListBean_);
                }
            }
        }
        //随机抽选
        Random random=new Random();
        int num=random.nextInt(festivalBgList.size());
        if (num>=0)
        {
            MVPath=festivalBgList.get(num).getMVPath();
            fontsPath= festivalBgList.get(num).getFontsPath();
            txtColor=festivalBgList.get(num).getTxtColor();
        }
        listBeans=new FestivalListBean(MVPath,fontsPath,txtColor);
        return listBeans;
    }

    //判断是否是视频文件
    public boolean isVideo(String PathName)
    {
        String str=PathName.substring(PathName.indexOf("."));   //获取PathName中"."之后的字符串
        //判断是否为视频格式
        if (str.equals(".mp4")||str.equals(".MP4")||str.equals(".mov")||str.equals(".MOV")
        ||str.equals(".wmv")||str.equals(".WMV")||str.equals(".avi")||str.equals(".AVI")
        ||str.equals(".amv")||str.equals(".AMV")||str.equals(".mtv")||str.equals(".MTV")
        ||str.equals(".dat")||str.equals(".DAT")||str.equals(".dmv")||str.equals(".DMV")
        ||str.equals(".flv")||str.equals(".FLV"))
        {
            return true;
        }
        return false;
    }

}
