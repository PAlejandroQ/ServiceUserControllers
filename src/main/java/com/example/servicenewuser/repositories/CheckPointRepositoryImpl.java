package com.example.servicenewuser.repositories;

import com.example.servicenewuser.Constants;
import com.example.servicenewuser.domain.CheckPoint;
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
public class CheckPointRepositoryImpl implements CheckPointRepository{

    private static final String SQL_FIND_ALL_CURRENT = "SELECT * FROM latest_et_checkpoints";
    private static final String SQL_FIND_ALL_HISTORIC = "SELECT * FROM et_checkpoints";
    private static final String SQL_CREATE = "INSERT INTO et_checkpoints (checkpoint_id, user_id, sector_id, point_gps, state, report_date) VALUES(NEXTVAL('ET_checkpoints_SEQ'), ? ,?,?,?,?)";
    private static final String SQL_FIND_ALL_BETWEEN_DATES = "SELECT * FROM et_checkpoints c WHERE c.report_date BETWEEN ? AND ?";
    private static final String SQL_FIND_BY_ID_CURRENT = "SELECT * FROM latest_et_checkpoints c WHERE c.checkpoint_id = ? ";
    private static final String SQL_FIND_BY_ID_HISTORIC= "SELECT * FROM et_checkpoints c WHERE c.report_date == ?";
    private static final String SQL_DELETE_BEFORE_DATE= " DELETE FROM et_checkpoints c WHERE c.report_date < ?";
    private static final String SQL_DELETE_ALL= " DELETE FROM et_checkpoints";
    @Autowired
    JdbcTemplate jdbcTemplate;
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
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, newCheckPoint.getUserId());
                ps.setLong(2, newCheckPoint.getSectorId());
                ps.setObject(3, newCheckPoint.getPoint_gps());
                ps.setObject(4, newCheckPoint.getState());
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
            return jdbcTemplate.query(SQL_FIND_ALL_BETWEEN_DATES, new Object[]{}, checkPointRowMapper);
        }catch (Exception e) {
            throw new EtResourceNotFoundException("Category not found");
        }
    }

    @Override
    public List<CheckPoint> findByIdCurrent() {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_ID_CURRENT, new Object[]{}, checkPointRowMapper);
        }catch (Exception e) {
            throw new EtResourceNotFoundException("Category not found");
        }
    }

    @Override
    public List<CheckPoint> findByIdHistoric(long startDate, long endDate) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_ID_HISTORIC, new Object[]{}, checkPointRowMapper);
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
    private RowMapper<CheckPoint> checkPointRowMapper = ((rs, rowNum) -> {
        return new CheckPoint(rs.getInt("checkpoint_id"),
                rs.getInt("user_id"),
                rs.getInt("sector_id"),
                (Point) rs.getObject("point_gps"),
                (Constants.StateUser) rs.getObject("state"),
                rs.getLong("report_date"));
    });
}
