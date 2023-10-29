package com.example.servicenewuser.services;

import com.example.servicenewuser.domain.CheckPoint;
import com.example.servicenewuser.domain.Sector;
import com.example.servicenewuser.exceptions.EtBadRequestException;
import com.example.servicenewuser.exceptions.EtResourceNotFoundException;
import com.example.servicenewuser.repositories.CheckPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

@Service
@Transactional
public class CheckPointServiceImpl implements CheckPointService{
    @Autowired
    CheckPointRepository checkPointRepository;
    @Override
    public List<CheckPoint> getLastCheckPoint() {
        return null;
    }

    @Override
    public List<CheckPoint> getHistoricCheckPoint() {
        return  null;
    }

    @Override
    public CheckPoint getLastCheckPointByUser(int sectorId) throws EtResourceNotFoundException {
//        return CheckPointRepository.findLastCurrentByUserId(userId);
        return null;
    }

    @Override
    public List<CheckPoint> getHistoricCheckPointByUser(int sectorId) throws EtResourceNotFoundException {
//        return checkPointRepository.findAllHistoricByUserId(userId);
        return null;
    }

    @Override
    public void addCheckPoint(int sectorId, Point sectorGps) throws EtBadRequestException {
//    CheckPoint newCheckPoint = new CheckPoint();
//    newCheckPoint.setSectorId(sectorId);
//    newCheckPoint
    }

    @Override
    public void deleteOldHistoricCheckPoint(long beforeDate) {

    }
}
