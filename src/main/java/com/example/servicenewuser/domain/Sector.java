package com.example.servicenewuser.domain;


import java.awt.*;

public class Sector {
    public Integer getSectorId() {
        return sectorId;
    }

    public void setSectorId(Integer sectorId) {
        this.sectorId = sectorId;
    }


    public Point getCenter_gps() {
        return center_gps;
    }

    public void setCenter_gps(Point center_gps) {
        this.center_gps = center_gps;
    }

    public Long getReport_date() {
        return report_date;
    }

    public void setReport_date(Long report_date) {
        this.report_date = report_date;
    }

    public Sector(Integer sectorId, Point center_gps, Long report_date) {
        this.sectorId = sectorId;
        this.center_gps = center_gps;
        this.report_date = report_date;
    }

    private Integer sectorId;
    private Point center_gps;
    private Long report_date;

}
