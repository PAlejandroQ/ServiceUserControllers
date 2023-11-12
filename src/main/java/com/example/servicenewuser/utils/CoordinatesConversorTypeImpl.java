package com.example.servicenewuser.utils;

import org.postgresql.geometric.PGpoint;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Map;

@Service
public class CoordinatesConversorTypeImpl implements CoordinatesConversorType{
    @Override
    public Point objectToPoint(Object coordinateObject) {
        System.out.println(coordinateObject);
        Map<String, Object> coordinateMap = (Map<String, Object>) coordinateObject;
        return new Point((Integer) coordinateMap.get("x"),(Integer) coordinateMap.get("y"));
    }

    @Override
    public PGpoint PointToPGpoint(Point coordinatePoint) {
        return new PGpoint(coordinatePoint.getX(), coordinatePoint.getY());
    }
    @Override
    public Point pGPointToPoint(PGpoint coordinatePGpoint) {
        return new Point((int) coordinatePGpoint.x, (int) coordinatePGpoint.y);
    }
}
