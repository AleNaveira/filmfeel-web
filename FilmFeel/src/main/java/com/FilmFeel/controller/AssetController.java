package com.FilmFeel.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.FilmFeel.service.StorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assets")
public class AssetController {

    private static final Logger logger = LoggerFactory.getLogger(AssetController.class);

    @Autowired
    private StorageServiceImpl storageService;


    @GetMapping("/{filename:.+}")
    Resource getResource(@PathVariable("filename") String filename){
        logger.info("Solicitud de recurso: {}", filename);
        return storageService.loadAsResource(filename);
    }
}
