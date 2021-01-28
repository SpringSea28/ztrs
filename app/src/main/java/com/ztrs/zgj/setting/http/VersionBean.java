package com.ztrs.zgj.setting.http;

import java.util.List;

public class VersionBean {

    private List<UpdatesBean> updates;

    public List<UpdatesBean> getUpdates() {
        return updates;
    }

    public void setUpdates(List<UpdatesBean> updates) {
        this.updates = updates;
    }

    public static class UpdatesBean {
        /**
         * project : CGenie
         * versionCode : 45
         * versionName : 1.4.5
         * file : /CGenie/CGenie_V1.4.5_03281521.apk
         * description : 优化UI
         * md5 : 3a3c04661be71008c8c84cda70cb2deb
         */

        private String project;
        private String versionCode;
        private String versionName;
        private String file;
        private String description;
        private String md5;

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }
    }
}
