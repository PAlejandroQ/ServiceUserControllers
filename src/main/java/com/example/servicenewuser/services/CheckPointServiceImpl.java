package com.example.servicenewuser.services;

import com.example.servicenewuser.domain.CheckPoint;
import com.example.servicenewuser.exceptions.EtBadRequestException;
import com.example.servicenewuser.exceptions.EtResourceNotFoundException;
import com.example.servicenewuser.repositories.CheckPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CheckPointServiceImpl implements CheckPointService{
    @Autowired
    CheckPointRepository checkPointRepository;
    @Override
    public List<CheckPoint> getLastCheckPoint() {
        return checkPointRepository.findAllCurrent();
    }

    @Override
    public List<CheckPoint> getHistoricCheckPoint() {
        return checkPointRepository.findAllHistoric();
    }

    @Override
    public List<CheckPoint> getHistoricCheckPoint(long min, long max) {
        return  checkPointRepository.findAllBetweenDates(min, max);
    }

    @Override
    public CheckPoint getLastCheckPointByUser(int userId) throws EtResourceNotFoundException {
//        return CheckPointRepository.findLastCurrentByUserId(userId);
        return checkPointRepository.findByUserIdCurrent(userId);
    }

    @Override
    public List<CheckPoint> getHistoricCheckPointByUser(int userId) throws EtResourceNotFoundException {
//        return checkPointRepository.findAllHistoricByUserId(userId);
        return checkPointRepository.findByIdHistoric(userId);
    }

    @Override
    public Integer addCheckPoint(CheckPoint newCheckPoint) throws EtBadRequestException {
        newCheckPoint.setSectorId(2);
        return checkPointRepository.create(newCheckPoint);
    }

    @Override
    public void deleteOldHistoricCheckPoint(long beforeDate) {

    }
}
