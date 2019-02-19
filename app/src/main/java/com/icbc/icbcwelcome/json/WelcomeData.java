package com.icbc.icbcwelcome.json;

import java.util.List;

public class WelcomeData {

    /**
     * picData : [{"fileName":"测试图片4.jpg","displayTime":5,"displayOrder":4},{"fileName":"测试图片3.jpg","displayTime":5,"displayOrder":3},{"fileName":"测试图片2.jpg","displayTime":10,"displayOrder":2},{"fileName":"测试图片1.jpg","displayTime":11,"displayOrder":1}]
     * welcomeMsg : 欢迎XXX光临
     * welcomeTime : 30
     */

    private String welcomeMsg = null;
    private int welcomeTime = 0;
    private List<PicDataBean> picData = null;

    public String getWelcomeMsg() {
        return welcomeMsg;
    }

    public void setWelcomeMsg(String welcomeMsg) {
        this.welcomeMsg = welcomeMsg;
    }

    public int getWelcomeTime() {
        return welcomeTime;
    }

    public void setWelcomeTime(int welcomeTime) {
        this.welcomeTime = welcomeTime;
    }

    public List<PicDataBean> getPicData() {
        return picData;
    }

    public void setPicData(List<PicDataBean> picData) {
        this.picData = picData;
    }

    public static class PicDataBean {
        /**
         * fileName : 测试图片4.jpg
         * displayTime : 5
         * displayOrder : 4
         */

        private String fileName;
        private int displayTime;
        private int displayOrder;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public int getDisplayTime() {
            return displayTime;
        }

        public void setDisplayTime(int displayTime) {
            this.displayTime = displayTime;
        }

        public int getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(int displayOrder) {
            this.displayOrder = displayOrder;
        }
    }
}
