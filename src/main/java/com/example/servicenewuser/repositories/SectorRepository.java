package com.example.servicenewuser.repositories;

import com.example.servicenewuser.domain.Sector;

import java.util.List;

public interface SectorRepository {
    List<Sector> findAllCurrent();
    List<Sector> findAllHistoric();
    Integer create(Sector newSector);
    List<Sector> findAllBetweenDates(long startDate, long endDate);
    Sector findByIdCurrent(int sectorId);
    List<Sector> findByIdHistoric(long startDate, long endDate);
    void deleteBeforeDate(Long limitDay);
    void deleteAll();
}
