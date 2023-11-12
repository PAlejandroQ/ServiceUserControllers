package com.example.servicenewuser.repositories;

import com.example.servicenewuser.domain.Sector;
import com.example.servicenewuser.exceptions.EtBadRequestException;
import com.example.servicenewuser.exceptions.EtResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.postgresql.geometric.PGpoint;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
@Repository
public class SectorRepositoryImpl implements SectorRepository {
    private static final String SQL_FIND_ALL_CURRENT = "SELECT * FROM latest_et_sectors";
    private static final String SQL_FIND_ALL_HISTORIC = "SELECT * FROM et_sectors";
    private static final String SQL_CREATE = "INSERT INTO et_sectors (sector_id, center_gps, report_date) VALUES(?, ?, ?)";
    private static final String SQL_FIND_ALL_BETWEEN_DATES = "SELECT * FROM et_sectors c WHERE c.report_date BETWEEN ? AND ?";
    private static final String SQL_FIND_BY_ID_CURRENT = "SELECT * FROM latest_et_sectors c WHERE c.sector_id = ?  ";
    private static final String SQL_FIND_BY_ID_HISTORIC= "SELECT * FROM et_sectors c where c.sector_id == ? ";
    private static final String SQL_DELETE_BEFORE_DATE= " DELETE FROM et_sectors c  WHERE c.report_date < ? ";
    private static final String SQL_DELETE_ALL= "DELETE FROM et_sectors c ";

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

    public static PGpoint convertAWTPointToPGPoint(java.awt.Point awtPoint) {
        return new PGpoint(awtPoint.getX(), awtPoint.getY());
    }
    @Override
    public Integer create(Sector newSector) {
        try {
            PGpoint pGpoint = convertAWTPointToPGPoint(newSector.getCenter_gps());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, newSector.getSectorId());
                ps.setObject(2, pGpoint);
                ps.setLong(3, newSector.getReport_date());
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
            return jdbcTemplate.query(SQL_FIND_ALL_BETWEEN_DATES, new Object[]{startDate,endDate}, sectorRowMapper);
        }catch (Exception e) {
            throw new EtResourceNotFoundException("Sectors not found");
        }
    }

    @Override
    public Sector findByIdCurrent(int sectorId) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_ID_CURRENT, new Object[]{sectorId}, sectorRowMapper).get(0);
        }catch (Exception e) {
            throw new EtResourceNotFoundException("Sectors not found");
        }
    }

    @Override
    public List<Sector> findByIdHistoric(long startDate, long endDate) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_ID_HISTORIC, new Object[]{}, sectorRowMapper);
        }catch (Exception e) {
            throw new EtResourceNotFoundException("Sectors not found");
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

    public static java.awt.Point convertPGPointToAWTPoint(PGpoint pgPoint) {
        return new java.awt.Point((int) pgPoint.x, (int) pgPoint.y);
    }

    private RowMapper<Sector> sectorRowMapper = ((rs, rowNum) -> {
        PGpoint pgPoint = (PGpoint) rs.getObject("center_gps"); // Extraer el objeto PGpoint de la base de datos
        java.awt.Point javaPoint = convertPGPointToAWTPoint(pgPoint);

        return new Sector(rs.getInt("sector_id"), javaPoint, rs.getLong("report_date"));
    });
}
