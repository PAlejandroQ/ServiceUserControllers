package com.example.servicenewuser.resources;

import com.example.servicenewuser.domain.CheckPoint;
import com.example.servicenewuser.domain.Sector;
import com.example.servicenewuser.services.CheckPointService;
import com.example.servicenewuser.services.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/checkpoints")
public class CheckPointResource {
    @Autowired
    CheckPointService checkPointService;

    @GetMapping("")
    public ResponseEntity<List<CheckPoint>> getLastCheckPoint(){
        List<CheckPoint> checkPoints = checkPointService.getLastCheckPoint();
        return new ResponseEntity<>(checkPoints, HttpStatus.OK);
    }

//    @GetMapping("{userId}")
//    public ResponseEntity<List<CheckPoint>> getLastSectors(@PathVariable("userId") Integer userId){
//        List<CheckPoint> checkPoints = checkPointService.getHistoricCheckPointByUser(userId);
//        return new ResponseEntity<>(checkPoints, HttpStatus.OK);
//    }
}
