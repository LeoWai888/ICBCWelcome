package com.icbc.icbcwelcome.json;

import java.util.List;

public class PicData {

    private List<PicDataBean> picData;

    public List<PicDataBean> getPicData() {
        return picData;
    }

    public void setPicData(List<PicDataBean> picData) {
        this.picData = picData;
    }

    public static class PicDataBean {
        /**
         * fileName : 1.jpg
         * displayTime : 5
         * displayOrder : 1
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
