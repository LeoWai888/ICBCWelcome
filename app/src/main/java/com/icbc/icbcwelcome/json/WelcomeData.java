package com.icbc.icbcwelcome.json;

import java.util.List;

public class WelcomeData {

    /**
     * picData : [{"fileName":"测试图片4.jpg","displayTime":5,playType:"1","displayOrder":4},{"fileName":"测试图片3.jpg","displayTime":5,playType:"1","displayOrder":3},{"fileName":"测试图片2.jpg","displayTime":10,playType:"1","displayOrder":2},{"fileName":"测试图片1.jpg","displayTime":11,"displayOrder":1}]
     * welcomeMsg : 欢迎XXX光临
     * welcomeTime : 30
     */

    private String welcomeMsg = null;
    private int welcomeTime = 0;
    private String rollMsg = null;
    private int rollDisTime = 0;
    private String rollMsgSendTime = null;
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


    public String getRollMsg() {
        return rollMsg;
    }

    public void setRollMsg(String rollMsg) {
        this.rollMsg = rollMsg;
    }

    public int getRollDisTime() {
        return rollDisTime;
    }

    public void setRollDisTime(int rollDisTime) {
        this.rollDisTime = rollDisTime;
    }

    public String getRollMsgSendTime() {
        return rollMsgSendTime;
    }

    public void setRollMsgSendTime(String rollMsgSendTime) {
        this.rollMsgSendTime = rollMsgSendTime;
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
        private String playType;   //playTime:0是日常播放、1是插播、2是只播
        private String startTime;   //具体时间到分钟  2019-05-23 00：00,日常播放时间默认为00：00
        private String endTime;     //具体时间到分钟  2019-05-23 00：00,日常播放时间默认为00：00
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

        public String getPlayType() {
            return playType;
        }

        public void setPlayType(String playType) {
            this.playType = playType;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(int displayOrder) {
            this.displayOrder = displayOrder;
        }
    }
}
