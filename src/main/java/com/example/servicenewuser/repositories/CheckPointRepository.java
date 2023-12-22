package com.example.servicenewuser.repositories;

import com.example.servicenewuser.domain.CheckPoint;

import java.awt.*;
import java.util.List;

public interface CheckPointRepository {
    List<CheckPoint> findAllCurrent();
    List<Point> findAllSectorLabels();
    List<CheckPoint> findAllHistoric();
    Integer create(CheckPoint newCheckPoint);
    List<CheckPoint> findAllBetweenDates(long startDate, long endDate);
    CheckPoint findByUserIdCurrent(int userId);
    List<CheckPoint> findByIdHistoric(int userId);
    void deleteBeforeDate(Long limitDay);
    void deleteAll();
}
