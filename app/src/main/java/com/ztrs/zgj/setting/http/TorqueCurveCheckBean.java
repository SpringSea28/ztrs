package com.ztrs.zgj.setting.http;

public class TorqueCurveCheckBean {

    int code;
    String msg;
    Data Data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return Data;
    }

    public void setData(Data data) {
        Data = data;
    }

    public static class Data{
        int Code;
        String Url;
        String Ver;
        String Md5;

        public int getCode() {
            return Code;
        }

        public void setCode(int code) {
            Code = code;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String url) {
            this.Url = url;
        }

        public String getVer() {
            return Ver;
        }

        public void setVer(String ver) {
            Ver = ver;
        }

        public String getMd5() {
            return Md5;
        }

        public void setMd5(String md5) {
            Md5 = md5;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "Code=" + Code +
                    ", Url='" + Url + '\'' +
                    ", Ver='" + Ver + '\'' +
                    ", Md5='" + Md5 + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TorqueCurveCheckBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", Data=" + Data +
                '}';
    }
}
