package com.example.servicenewuser.services;

import com.example.servicenewuser.domain.Sector;
import com.example.servicenewuser.exceptions.EtBadRequestException;
import com.example.servicenewuser.exceptions.EtResourceNotFoundException;

import java.awt.*;
import java.util.List;

public interface SectorService {
    List<Sector> getLastSectors();
    List<Sector> getHistoricSectors();

    Sector getLastSectorById(int sectorId) throws EtResourceNotFoundException;

    List<Sector> getHistoricSectorById(int sectorId) throws EtResourceNotFoundException;

    Sector addSector(Sector newSector) throws EtBadRequestException;

    void addFullSectors(List<Sector> sectorsNewList) throws EtBadRequestException;

    void deleteOldHistoricSectors(long sectorBeforeDate);

    List<Sector> getHistoricSectorsInRange(String start, String end);

    Sector updateSector(Integer sectorId, Point updatedSectorGPS);

    boolean deleteSectorById(Integer sectorId);
}
