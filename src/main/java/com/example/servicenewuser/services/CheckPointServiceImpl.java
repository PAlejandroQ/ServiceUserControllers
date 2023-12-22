package com.example.servicenewuser.services;

import com.example.servicenewuser.Constants;
import com.example.servicenewuser.domain.CheckPoint;
import com.example.servicenewuser.exceptions.EtBadRequestException;
import com.example.servicenewuser.exceptions.EtResourceNotFoundException;
import com.example.servicenewuser.repositories.CheckPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CheckPointServiceImpl implements CheckPointService{
    @Autowired
    CheckPointRepository checkPointRepository;
    @Override
    public List<CheckPoint> getLastCheckPoint() {
        return checkPointRepository.findAllCurrent();
    }

    @Override
    public List<CheckPoint> getHistoricCheckPoint() {
        return checkPointRepository.findAllHistoric();
    }

    @Override
    public List<CheckPoint> getHistoricCheckPoint(long min, long max) {
        return  checkPointRepository.findAllBetweenDates(min, max);
    }

    @Override
    public CheckPoint getLastCheckPointByUser(int userId) throws EtResourceNotFoundException {
//        return CheckPointRepository.findLastCurrentByUserId(userId);
        return checkPointRepository.findByUserIdCurrent(userId);
    }

    @Override
    public List<CheckPoint> getHistoricCheckPointByUser(int userId) throws EtResourceNotFoundException {
//        return checkPointRepository.findAllHistoricByUserId(userId);
        return checkPointRepository.findByIdHistoric(userId);
    }

    @Override
    public Integer addCheckPoint(CheckPoint newCheckPoint) throws EtBadRequestException {
//        newCheckPoint.setSectorId(this.findSector(newCheckPoint));
        newCheckPoint = this.configureNewCheckPoint(newCheckPoint);
        return checkPointRepository.create(newCheckPoint);
    }

    @Override
    public void deleteOldHistoricCheckPoint(long beforeDate) {

    }

//    @org.jetbrains.annotations.Nullable
    private CheckPoint configureNewCheckPoint(CheckPoint newCheckPoint) {
        double thresholdDistance = 70; // Mismo que eps en el script de Python
        List<CheckPoint> checkPoints = checkPointRepository.findAllCurrent();

        // Encontrar puntos cercanos al nuevo punto según el umbral
        List<Integer> nearbyPoints = new ArrayList<>();
        for (int i = 0; i < checkPoints.size(); i++) {
            Point checkpointCoordinates = checkPoints.get(i).getPoint_gps();
            double distance = calculateDistance(newCheckPoint.getPoint_gps(), checkpointCoordinates);
            if (distance < thresholdDistance & !Objects.equals(checkPoints.get(i).getUserId(), newCheckPoint.getUserId())) {
                nearbyPoints.add(checkPoints.get(i).getUserId());
            }
        }

        List<Point> labels = checkPointRepository.findAllSectorLabels();
        // Verificar si el nuevo punto se une a un cluster existente
        if (!nearbyPoints.isEmpty()) {
            for (Point punto : labels) {
                if ((int) punto.getX() == nearbyPoints.get(0)) {
                    System.out.println("Cluster asignado: " + (int) punto.getY() + " IdReferencia: "+ punto.getX());
                    newCheckPoint.setSectorId((int) punto.getY());
                    return newCheckPoint;
                }
            }
            newCheckPoint.setSectorId(null);
            return newCheckPoint;
//            throw new RuntimeException("This User_Id doesn't exist");
        } else {
            newCheckPoint.setSectorId(null);
            newCheckPoint.setState(Constants.StateUser.alert);
            System.out.println("El nuevo punto no pertenece a ningún cluster existente");

            return newCheckPoint;
        }
    }

    private double calculateDistance(Point point1, Point point2) {
        double sum = 0.0;
        sum += Math.pow(point1.getX() - point2.getX(), 2);
        sum += Math.pow(point1.getY() - point2.getY(), 2);
        return Math.sqrt(sum);
    }
}
