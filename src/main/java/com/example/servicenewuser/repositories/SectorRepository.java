package com.example.servicenewuser.repositories;

import com.example.servicenewuser.domain.CheckPoint;
import com.example.servicenewuser.domain.Sector;

import java.util.List;

public interface SectorRepository {
    List<Sector> findAllCurrent();
    List<Sector> findAllHistoric();
    Integer create(Sector newCheckPoint);
    List<Sector> findAllBetweenDates(long startDate, long endDate);
    List<Sector> findByIdCurrent();
    List<Sector> findByIdHistoric(long startDate, long endDate);
    void deleteBeforeDate(Long limitDay);
    void deleteAll();
}
