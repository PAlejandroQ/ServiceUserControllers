package com.example.servicenewuser.services;

import com.example.servicenewuser.domain.CheckPoint;
import com.example.servicenewuser.domain.Sector;
import com.example.servicenewuser.exceptions.EtBadRequestException;
import com.example.servicenewuser.exceptions.EtResourceNotFoundException;

import java.awt.*;
import java.util.List;

public interface CheckPointService {

    List<CheckPoint> getLastCheckPoint();
    List<CheckPoint> getHistoricCheckPoint();

    CheckPoint getLastCheckPointByUser(int sectorId) throws EtResourceNotFoundException;

    List<CheckPoint> getHistoricCheckPointByUser(int sectorId) throws EtResourceNotFoundException;

    void addCheckPoint(int sectorId, Point sectorGps) throws EtBadRequestException;


    void deleteOldHistoricCheckPoint(long beforeDate);

}
