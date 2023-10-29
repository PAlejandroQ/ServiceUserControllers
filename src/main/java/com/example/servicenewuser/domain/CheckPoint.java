package com.example.servicenewuser.domain;

import com.example.servicenewuser.Constants;

import java.awt.*;

public class CheckPoint {


    private Integer checkpointId;
    private Integer userId;
    private Integer sectorId;
    private Point point_gps;
    private Constants.StateUser stateUser;
    private Long checkPointDate;

    public CheckPoint(Integer checkpointId, Integer userId, Integer sectorId, Point point_gps, Constants.StateUser stateUser, Long checkPointDate) {
        this.checkpointId = checkpointId;
        this.sectorId = sectorId;
        this.userId = userId;
        this.point_gps = point_gps;
        this.stateUser = stateUser;
        this.checkPointDate = checkPointDate;
    }

    public Integer getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(Integer checkpointId) {
        this.checkpointId = checkpointId;
    }

    public Integer getSectorId() {
        return sectorId;
    }

    public void setSectorId(Integer sectorId) {
        this.sectorId = sectorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Point getPoint_gps() {
        return point_gps;
    }

    public void setPoint_gps(Point point_gps) {
        this.point_gps = point_gps;
    }

    public Constants.StateUser getState() {
        return stateUser;
    }

    public void setState(Constants.StateUser state) {
        this.stateUser = state;
    }

    public Long getCheckPointDate() {
        return checkPointDate;
    }

    public void setCheckPointDate(Long checkPointDate) {
        this.checkPointDate = checkPointDate;
    }
}
