package com.example.servicenewuser.utils;

import org.postgresql.geometric.PGpoint;

import java.awt.*;

public interface CoordinatesConversorType {
    Point objectToPoint(Object coordinateObject);

    PGpoint PointToPGpoint(Point coordinatePoint);

    Point pGPointToPoint(PGpoint coordinatePGpoint);
}
