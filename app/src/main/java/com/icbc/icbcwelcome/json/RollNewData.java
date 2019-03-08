package com.icbc.icbcwelcome.json;

public class RollNewData {
    /**
     * rollMsgSendTime : 2019-02-25 08:59:05
     * rollDisTime : 4
     * rollMsg : 我是滚动信息！
     */

    private String rollMsgSendTime;
    private int rollDisTime;
    private String rollMsg;

    public String getRollMsgSendTime() {
        return rollMsgSendTime;
    }

    public void setRollMsgSendTime(String rollMsgSendTime) {
        this.rollMsgSendTime = rollMsgSendTime;
    }

    public int getRollDisTime() {
        return rollDisTime;
    }

    public void setRollDisTime(int rollDisTime) {
        this.rollDisTime = rollDisTime;
    }

    public String getRollMsg() {
        return rollMsg;
    }

    public void setRollMsg(String rollMsg) {
        this.rollMsg = rollMsg;
    }
}
