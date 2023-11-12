package com.example.servicenewuser.resources;

import com.example.servicenewuser.domain.Sector;
import com.example.servicenewuser.exceptions.EtBadRequestException;
import com.example.servicenewuser.services.SectorService;
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
@RequestMapping("/api/sectors")
public class SectorResource {
    @Autowired
    SectorService sectorService;

    @Autowired
    CoordinatesConversorType coordinatesConversorType;
    Date date = new Date();
    //OK
    @GetMapping("")
    public ResponseEntity<List<Sector>> getLastSectors(){
        List<Sector> sectors = sectorService.getLastSectors();
        return new ResponseEntity<>(sectors, HttpStatus.OK);
    }

    /* OK */
    @GetMapping("/historic")
    public ResponseEntity<List<Sector>> getHistoricSectors(@RequestParam(required = false) String min,
                                                           @RequestParam(required = false) String max){

        List<Sector> sectors;

        if (min != null && max != null) {
//            System.out.println("Entra");
            sectors = sectorService.getHistoricSectorsInRange(Long.parseLong(min), Long.parseLong(max));
        } else {
//            System.out.println("Entra");
            sectors = sectorService.getHistoricSectors();
        }
        return new ResponseEntity<>(sectors, HttpStatus.OK);
    }
    // OK
    @GetMapping("/{sectorId}")
    public ResponseEntity<Sector> getCoordinateBySector(@PathVariable("sectorId") Integer sectorId){
        Sector sector = sectorService.getLastSectorById(sectorId);
        if (sector == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sector, HttpStatus.OK);
    }
    // OK with integers
    @PostMapping("")
    public ResponseEntity<Sector> addSector(HttpServletRequest request,
                                            @RequestBody Map<String, Object> sectorMap){
        // Extraer los valores del mapa sectorMap
        Integer sectorId = (Integer) sectorMap.get("sectorId");
        Point point =  coordinatesConversorType.objectToPoint(sectorMap.get("coordinates"));

        // Crear un nuevo objeto Sector con los valores extraídos
        Sector newSector = new Sector();
        newSector.setSectorId(sectorId);
        newSector.setCenter_gps(point);
        newSector.setReport_date(date.getTime());

        // Guardar el nuevo sector en tu base de datos
//        newSector = sectorService.addSector(newSector);
        if(sectorService.addSector(newSector)!= null)
            return new ResponseEntity<>(newSector, HttpStatus.CREATED);
        else{
            throw new EtBadRequestException("Invalid request");
        }
    }
//      EL ENDPOINT NO REQUIERE EXISTIR YA QUE LA ACTUALIZACION DE POSICION LO HACE LA VISTA
//    @PutMapping("/{sectorId}")
//    public ResponseEntity<Sector> updateCoordinateBySector(@PathVariable("sectorId") Integer sectorId,
//                                                           @RequestBody Object updatedSectorGPS){
//        System.out.println(updatedSectorGPS);
//        Point point =  coordinatesConversorType.objectToPoint(updatedSectorGPS);
//
//        Sector updated = sectorService.updateSector(sectorId, point);
//        if (updated == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(updated, HttpStatus.OK);
//    }

//    @PutMapping("")
//    public ResponseEntity<Map<String, Boolean>> updateCoordinates(){
//        return null;
//    }

    @DeleteMapping("/{sectorId}")
    public ResponseEntity<List<Sector>> deleteSectorById(@PathVariable("sectorId") Integer sectorId){
        boolean deleted = sectorService.deleteSectorById(sectorId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteAllSectors() {
        sectorService.deleteAllSectors();
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("")
//    public ResponseEntity<List<Sector>> deleteSectorsBeforeDate(){
//        return null;
//    }
}
