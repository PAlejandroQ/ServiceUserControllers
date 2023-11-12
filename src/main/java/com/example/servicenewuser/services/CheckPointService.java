package com.example.servicenewuser.services;

import com.example.servicenewuser.domain.CheckPoint;
import com.example.servicenewuser.exceptions.EtBadRequestException;
import com.example.servicenewuser.exceptions.EtResourceNotFoundException;

import java.util.List;

public interface CheckPointService {

    List<CheckPoint> getLastCheckPoint();
    List<CheckPoint> getHistoricCheckPoint(long min, long max);

    List<CheckPoint> getHistoricCheckPoint();


    CheckPoint getLastCheckPointByUser(int userId) throws EtResourceNotFoundException;

    List<CheckPoint> getHistoricCheckPointByUser(int userId) throws EtResourceNotFoundException;

    Integer addCheckPoint(CheckPoint newCheckPoint) throws EtBadRequestException;


    void deleteOldHistoricCheckPoint(long beforeDate);

}
