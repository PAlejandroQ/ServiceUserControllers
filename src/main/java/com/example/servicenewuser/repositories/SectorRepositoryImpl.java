package com.example.servicenewuser.repositories;

import com.example.servicenewuser.domain.CheckPoint;
import com.example.servicenewuser.domain.Sector;
import com.example.servicenewuser.exceptions.EtBadRequestException;
import com.example.servicenewuser.exceptions.EtResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
@Repository
public class SectorRepositoryImpl implements SectorRepository {
    private static final String SQL_FIND_ALL_CURRENT = "";
    private static final String SQL_FIND_ALL_HISTORIC = "";
    private static final String SQL_CREATE = "";
    private static final String SQL_FIND_ALL_BETWEEN_DATES = "";
    private static final String SQL_FIND_BY_ID_CURRENT = "";
    private static final String SQL_FIND_BY_ID_HISTORIC= "";
    private static final String SQL_DELETE_BEFORE_DATE= "";
    private static final String SQL_DELETE_ALL= "";

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public List<Sector> findAllCurrent() {
        return jdbcTemplate.query(SQL_FIND_ALL_CURRENT, new Object[]{}, sectorRowMapper);
    }

    @Override
    public List<Sector> findAllHistoric() {
        return jdbcTemplate.query(SQL_FIND_ALL_HISTORIC, new Object[]{}, sectorRowMapper);
    }

    @Override
    public Integer create(Sector newCheckPoint) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, newCheckPoint.getSectorId());
                ps.setObject(2, newCheckPoint.getCenter_gps());
                ps.setLong(3, newCheckPoint.getReport_date());
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("sector_id");
        }catch (Exception e) {
            throw new EtBadRequestException("Invalid request");
        }
    }

    @Override
    public List<Sector> findAllBetweenDates(long startDate, long endDate) {
        try {
            return jdbcTemplate.query(SQL_FIND_ALL_BETWEEN_DATES, new Object[]{}, sectorRowMapper);
        }catch (Exception e) {
            throw new EtResourceNotFoundException("Category not found");
        }
    }

    @Override
    public List<Sector> findByIdCurrent() {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_ID_CURRENT, new Object[]{}, sectorRowMapper);
        }catch (Exception e) {
            throw new EtResourceNotFoundException("Category not found");
        }
    }

    @Override
    public List<Sector> findByIdHistoric(long startDate, long endDate) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_ID_HISTORIC, new Object[]{}, sectorRowMapper);
        }catch (Exception e) {
            throw new EtResourceNotFoundException("Category not found");
        }
    }

    @Override
    public void deleteBeforeDate(Long limitDay) {
        int count = jdbcTemplate.update(SQL_DELETE_BEFORE_DATE, new Object[]{});
        if(count == 0)
            throw new EtResourceNotFoundException("Before date not found");
    }

    @Override
    public void deleteAll() {
        int count = jdbcTemplate.update(SQL_DELETE_ALL, new Object[]{});
        if(count == 0)
            throw new EtResourceNotFoundException("Delete failed");
    }
    private RowMapper<Sector> sectorRowMapper = ((rs, rowNum) -> {
        return new Sector(rs.getInt("sector_id"),
                rs.getObject("center_gps", Point.class),
                rs.getLong("report_date"));
    });
}
