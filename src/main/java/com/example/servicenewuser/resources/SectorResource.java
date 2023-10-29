package com.example.servicenewuser.resources;

import com.example.servicenewuser.domain.Sector;
import com.example.servicenewuser.services.SectorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/sectors")
public class SectorResource {
    @Autowired
    SectorService sectorService;

    @GetMapping("")
    public ResponseEntity<List<Sector>> getLastSectors(HttpServletRequest request){
        List<Sector> sectors = sectorService.getLastSectors();
        return new ResponseEntity<>(sectors, HttpStatus.OK);
    }

    @GetMapping("/historic")
    public ResponseEntity<List<Sector>> getHistoricSectors(HttpServletRequest request){
        List<Sector> sectors = sectorService.getHistoricSectors();
        return new ResponseEntity<>(sectors, HttpStatus.OK);
    }

    @GetMapping("/{sectorId}")
    public ResponseEntity<Sector> getCoordinateBySector(HttpServletRequest request,
                                                              @PathVariable("sectorId") Integer sectorId){
        Sector sector = sectorService.getLastSectorById(sectorId);

        return new ResponseEntity<>(sector, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<List<Sector>> addSector(HttpServletRequest request,
                                                  @RequestBody Map<String, Object> sectorMap){
        return null;
    }

    @PutMapping("/{sectorId}")
    public ResponseEntity<Map<String, Boolean>> updateCoordinateBySector(){
        return null;
    }

    @PutMapping("")
    public ResponseEntity<Map<String, Boolean>> updateCoordinates(){
        return null;
    }

    @DeleteMapping("/{sectorId}")
    public ResponseEntity<List<Sector>> deleteSectorById(){
        return null;
    }

    @DeleteMapping("")
    public ResponseEntity<List<Sector>> deleteAllSectors(){
        return null;
    }

    @GetMapping("")
    public ResponseEntity<List<Sector>> deleteSectorsBeforeDate(){
        return null;
    }
}
