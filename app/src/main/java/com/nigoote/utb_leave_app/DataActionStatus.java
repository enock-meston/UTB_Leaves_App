package com.nigoote.utb_leave_app;

public class DataActionStatus {
    private String aid;
    private String title;
    private String sp;
    private String hr, dvcf, dvca, vc;

    public DataActionStatus(String title, String sp, String hr, String dvcf, String dvca, String vc) {

        this.title = title;
        this.sp = sp;
        this.hr = hr;
        this.dvcf = dvcf;
        this.dvca = dvca;
        this.vc = vc;
    }

    public String getTitle() {
        return title;
    }

    public String getSp() {
        return sp;
    }

    public String getHr() {
        return hr;
    }

    public String getDvcf() {
        return dvcf;
    }

    public String getDvca() {
        return dvca;
    }

    public String getVc() {
        return vc;
    }
}
