package com.example.servicenewuser.services;

import com.example.servicenewuser.domain.Sector;
import com.example.servicenewuser.exceptions.EtBadRequestException;
import com.example.servicenewuser.exceptions.EtResourceNotFoundException;
import com.example.servicenewuser.repositories.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

@Service
@Transactional
public class SectorServiceImpl implements SectorService{
    @Autowired
    SectorRepository sectorRepository;
    @Override
    public List<Sector> getLastSectors() {
        return sectorRepository.findAllCurrent();
    }

    @Override
    public List<Sector> getHistoricSectors() {
        return sectorRepository.findAllHistoric();
    }

    @Override
    public Sector getLastSectorById(int sectorId) throws EtResourceNotFoundException {
        return sectorRepository.findByIdCurrent(sectorId);
    }

    @Override
    public List<Sector> getHistoricSectorById(int sectorId) throws EtResourceNotFoundException {
        return null;
    }

    @Override
    public Integer addSector(Sector newSector) throws EtBadRequestException {
        return sectorRepository.create(newSector);
    }

    @Override
    public void addFullSectors(List<Sector> sectorsNewList) throws EtBadRequestException {

    }

    @Override
    public void deleteOldHistoricSectors(long sectorBeforeDate) {

    }

    @Override
    public List<Sector> getHistoricSectorsInRange(Long start, Long end) {
        return sectorRepository.findAllBetweenDates(start, end);
    }

    @Override
    public Sector updateSector(Integer sectorId, Point updatedSectorGPS) {
        return null;
    }

    @Override
    public boolean deleteSectorById(Integer sectorId) {
        return false;
    }
}
