package com.icbc.icbcwelcome.json;

public class FestivalListBean {
    private String MVPath;
    private String fontsPath;
    private String txtColor;

    public FestivalListBean(String MVPath,String fontsPath,String txtColor)
    {
        this.MVPath=MVPath;
        this.fontsPath=fontsPath;
        this.txtColor=txtColor;
    }

    public String getMVPath() {
        return MVPath;
    }

    public void setMVPath(String MVPath) {
        this.MVPath = MVPath;
    }

    public String getFontsPath() {
        return fontsPath;
    }

    public void setFontsPath(String fontsPath) {
        this.fontsPath = fontsPath;
    }

    public String getTxtColor() {
        return txtColor;
    }

    public void setTxtColor(String txtColor) {
        this.txtColor = txtColor;
    }
}

