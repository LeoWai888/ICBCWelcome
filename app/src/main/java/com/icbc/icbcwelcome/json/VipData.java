package com.icbc.icbcwelcome.json;

public class VipData {

    /**
     * ISVIP : 1
     * TITLE : 广州分行领导
     * USERDEPT : 广东省分行
     * USERID : 000779538
     * USERLEVEL : 99
     * USERNAME : 王智勇
     * VIPTIME:yyyy-mm-dd hh24:mm:ss
     */

    private int ISVIP;
    private String TITLE;
    private String USERDEPT;
    private String USERID;
    private int USERLEVEL;
    private String USERNAME;
    private String VIPTIME;

    public int getISVIP() {
        return ISVIP;
    }

    public void setISVIP(int ISVIP) {
        this.ISVIP = ISVIP;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getUSERDEPT() {
        return USERDEPT;
    }

    public void setUSERDEPT(String USERDEPT) {
        this.USERDEPT = USERDEPT;
    }

    public String getUSERID() {
        return USERID;
    }

    public String getVIPTIME(){
        return VIPTIME;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public int getUSERLEVEL() {
        return USERLEVEL;
    }

    public void setUSERLEVEL(int USERLEVEL) {
        this.USERLEVEL = USERLEVEL;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public void setVIPTIME(String VIPTIME){
        this.VIPTIME = VIPTIME;
    }

}
