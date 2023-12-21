package com.example.servicenewuser.resources;

import com.example.servicenewuser.Constants;
import com.example.servicenewuser.domain.CheckPoint;
import com.example.servicenewuser.exceptions.EtBadRequestException;
import com.example.servicenewuser.services.CheckPointService;
import com.example.servicenewuser.utils.CoordinatesConversorType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkpoints")
public class CheckPointResource {
    @Autowired
    CheckPointService checkPointService;

    @Autowired
    CoordinatesConversorType coordinatesConversorType;
    Date date = new Date();

    // OK
    @GetMapping("")
    public ResponseEntity<List<CheckPoint>> getLastCheckPoint(){
        List<CheckPoint> checkPoints = checkPointService.getLastCheckPoint();
        return new ResponseEntity<>(checkPoints, HttpStatus.OK);
    }
    // OK
    @GetMapping("/historic")
    public ResponseEntity<List<CheckPoint>> getHistoricCheckPoint(@RequestParam(required = false) String min,
                                                                  @RequestParam(required = false) String max) {
        List<CheckPoint> checkPoints;

        if (min != null && max != null) {
//            System.out.println("Entra");
            checkPoints = checkPointService.getHistoricCheckPoint(Long.parseLong(min), Long.parseLong(max));
        } else {
//            System.out.println("Entra");
            checkPoints = checkPointService.getHistoricCheckPoint();
        }
        return new ResponseEntity<>(checkPoints, HttpStatus.OK);
    }
    // OK
    @GetMapping("/{userId}")
    public ResponseEntity<CheckPoint> getLastCheckPointByUser(@PathVariable("userId") Integer userId) {
        CheckPoint checkPoint = checkPointService.getLastCheckPointByUser(userId);
        if (checkPoint == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(checkPoint, HttpStatus.OK);
    }
    // OK
    @GetMapping("/{userId}/historic")
    public ResponseEntity<List<CheckPoint>> getHistoricCheckPointByUser(@PathVariable("userId") Integer userId) {
        List<CheckPoint> checkPoint = checkPointService.getHistoricCheckPointByUser(userId);
        if (checkPoint == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(checkPoint, HttpStatus.OK);
    }
    // OK
    @PostMapping("")
    public ResponseEntity<CheckPoint> addCheckPoint(@RequestBody Map<String, Object> checkPointMap, HttpServletRequest request) {
        // Extraer los valores del mapa checkPointMap
//        Integer userId = (Integer) checkPointMap.get("userId");
        Integer userId = (Integer) request.getAttribute("userId");
        Point point = coordinatesConversorType.objectToPoint(checkPointMap.get("coordinates"));
        Constants.StateUser stateUser = Constants.StateUser.valueOf((String) checkPointMap.get("state"));
        // Crear un nuevo objeto CheckPoint con los valores extraídos
        CheckPoint newCheckPoint = new CheckPoint();
        newCheckPoint.setUserId(userId);
        newCheckPoint.setPoint_gps(point);
        newCheckPoint.setCheckPointDate(date.getTime());
        newCheckPoint.setState(stateUser);

        // Guardar el nuevo CheckPoint en tu base de datos
        if (checkPointService.addCheckPoint(newCheckPoint) != null) {
            return new ResponseEntity<>(newCheckPoint, HttpStatus.CREATED);
        } else {
            throw new EtBadRequestException("Invalid request");
        }
    }
//
//    @DeleteMapping("")
//    public ResponseEntity<Void> deleteOldHistoricCheckPoints(HttpServletRequest request,
//                                                             @RequestBody Map<String, String> dateRange) {
//        // Extraer las fechas del mapa dateRange
//        String startDate = dateRange.get("startDate");
//        String endDate = dateRange.get("endDate");
//
//        checkPointService.deleteOldHistoricCheckPointsInRange(startDate, endDate);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
