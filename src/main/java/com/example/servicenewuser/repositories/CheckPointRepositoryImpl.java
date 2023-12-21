package com.example.servicenewuser.repositories;

import com.example.servicenewuser.Constants;
import com.example.servicenewuser.domain.CheckPoint;
import com.example.servicenewuser.exceptions.EtBadRequestException;
import com.example.servicenewuser.exceptions.EtResourceNotFoundException;
import com.example.servicenewuser.utils.CoordinatesConversorType;
import org.postgresql.geometric.PGpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CheckPointRepositoryImpl implements CheckPointRepository{

    private static final String SQL_FIND_ALL_CURRENT = "SELECT * FROM latest_et_checkpoints";
    private static final String SQL_FIND_ALL_HISTORIC = "SELECT * FROM et_checkpoints";
    private static final String SQL_CREATE = "INSERT INTO et_checkpoints (checkpoint_id, user_id, sector_id, point_gps, state, report_date) VALUES(NEXTVAL('et_checkpoint_seq'), ? ,?,?,?,?)";
    private static final String SQL_FIND_ALL_BETWEEN_DATES = "SELECT * FROM et_checkpoints c WHERE c.report_date BETWEEN ? AND ?";
    private static final String SQL_FIND_BY_ID_CURRENT = "SELECT * FROM latest_et_checkpoints c WHERE c.user_id = ? ";
    private static final String SQL_FIND_BY_ID_HISTORIC= "SELECT * FROM et_checkpoints c WHERE c.user_id = ?";
    private static final String SQL_DELETE_BEFORE_DATE= " DELETE FROM et_checkpoints c WHERE c.report_date < ?";
    private static final String SQL_DELETE_ALL= " DELETE FROM et_checkpoints";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CoordinatesConversorType coordinatesConversorType;
    @Override
    public List<CheckPoint> findAllCurrent() {
        return jdbcTemplate.query(SQL_FIND_ALL_CURRENT, new Object[]{}, checkPointRowMapper);
    }

    @Override
    public List<CheckPoint> findAllHistoric() {
        return jdbcTemplate.query(SQL_FIND_ALL_HISTORIC, new Object[]{}, checkPointRowMapper);
    }

    @Override
    public Integer create(CheckPoint newCheckPoint) {
        try {
            PGpoint pGpoint = coordinatesConversorType.PointToPGpoint(newCheckPoint.getPoint_gps());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, newCheckPoint.getUserId());
                ps.setInt(2, newCheckPoint.getSectorId());
                ps.setObject(3, pGpoint);
//                ps.setString(4, newCheckPoint.getState().toString());
                ps.setObject(4,null);
                ps.setLong(5, newCheckPoint.getCheckPointDate());
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("checkpoint_id");
        }catch (Exception e) {
            throw new EtBadRequestException("Invalid request");
        }
    }

    @Override
    public List<CheckPoint> findAllBetweenDates(long startDate, long endDate) {
        try {
            return jdbcTemplate.query(SQL_FIND_ALL_BETWEEN_DATES, new Object[]{startDate,endDate}, checkPointRowMapper);
        }catch (Exception e) {
            throw new EtResourceNotFoundException("Category not found");
        }
    }

    @Override
    public CheckPoint findByUserIdCurrent(int userId) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_ID_CURRENT, new Object[]{userId}, checkPointRowMapper).get(0);
        }catch (Exception e) {
            throw new EtResourceNotFoundException("Category not found");
        }
    }

    @Override
    public List<CheckPoint> findByIdHistoric(int userId) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_ID_HISTORIC, new Object[]{userId}, checkPointRowMapper);
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


    public static java.awt.Point convertPGPointToAWTPoint(PGpoint pgPoint) {
        return new java.awt.Point((int) pgPoint.x, (int) pgPoint.y);
    }


/*    private RowMapper<Sector> sectorRowMapper = ((rs, rowNum) -> {
        PGpoint pgPoint = (PGpoint) rs.getObject("center_gps"); // Extraer el objeto PGpoint de la base de datos
        java.awt.Point javaPoint = convertPGPointToAWTPoint(pgPoint);

        return new Sector(rs.getInt("sector_id"), javaPoint, rs.getLong("report_date"));
    });*/
    private RowMapper<CheckPoint> checkPointRowMapper = ((rs, rowNum) -> {
    PGpoint pgPoint = (PGpoint) rs.getObject("point_gps");
    java.awt.Point javaPoint = convertPGPointToAWTPoint(pgPoint);
    return new CheckPoint(rs.getInt("checkpoint_id"),
                rs.getInt("user_id"),
                rs.getInt("sector_id"),
                javaPoint,
//                Constants.StateUser.valueOf((String) rs.getObject("state")),
                null,
                rs.getLong("report_date"));
    });
}
