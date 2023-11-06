package com.example.servicenewuser.resources;

import com.example.servicenewuser.domain.Sector;
import com.example.servicenewuser.services.SectorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/sectors")
public class SectorResource {
    @Autowired
    SectorService sectorService;

    @GetMapping("")
    public ResponseEntity<List<Sector>> getLastSectors(){
        List<Sector> sectors = sectorService.getLastSectors();
        return new ResponseEntity<>(sectors, HttpStatus.OK);
    }

    /*
    * Metodo que puede recibir dos parametros, retornando los registros que se tengan
    * dentro de ese rango de tiempo. En caso de no recibir parametros. */
    @GetMapping("/historic")
    public ResponseEntity<List<Sector>> getHistoricSectors(@RequestParam(required = false) String start,
                                                           @RequestParam(required = false) String end){

        List<Sector> sectors;
        if (start != null && end != null) {
            sectors = sectorService.getHistoricSectorsInRange(start, end);
        } else {
            sectors = sectorService.getHistoricSectors();
        }
        return new ResponseEntity<>(sectors, HttpStatus.OK);
    }

    @GetMapping("/{sectorId}")
    public ResponseEntity<Sector> getCoordinateBySector(@PathVariable("sectorId") Integer sectorId){
        Sector sector = sectorService.getLastSectorById(sectorId);
        if (sector == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sector, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Sector> addSector(HttpServletRequest request,
                                                  @RequestBody Map<String, Object> sectorMap){
        // Extraer los valores del mapa sectorMap
        Integer sectorId = (Integer) sectorMap.get("sectorId");
        Point coordinatesMap = (Point) sectorMap.get("coordinates");

        // Crear un nuevo objeto Sector con los valores extraídos
        Sector newSector = new Sector();
        newSector.setSectorId(sectorId);
        // Supongo que Sector tiene un campo para coordenadas llamado "coordinates"
        newSector.setCenter_gps(coordinatesMap);

        // Guardar el nuevo sector en tu base de datos
        newSector = sectorService.addSector(newSector);

        return new ResponseEntity<>(newSector, HttpStatus.CREATED);
    }

    @PutMapping("/{sectorId}")
    public ResponseEntity<Sector> updateCoordinateBySector(@PathVariable("sectorId") Integer sectorId,
                                                                         @PathVariable("newGpsCoordinate") Point updatedSectorGPS){

        Sector updated = sectorService.updateSector(sectorId, updatedSectorGPS);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

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
    public ResponseEntity<List<Sector>> deleteAllSectors(){
        return null;
    }

//    @GetMapping("")
//    public ResponseEntity<List<Sector>> deleteSectorsBeforeDate(){
//        return null;
//    }
}
