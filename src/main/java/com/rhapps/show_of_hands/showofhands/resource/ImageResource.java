package com.rhapps.show_of_hands.showofhands.resource;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/images")
public class ImageResource {
    private GridFsTemplate gridFsTemplate;
    private GridFsOperations gridFsOperations;

    public ImageResource(GridFsTemplate gridFsTemplate, GridFsOperations gridFsOperations) {

        this.gridFsTemplate = gridFsTemplate;
        this.gridFsOperations = gridFsOperations;
    }

    @PostMapping("/save")
    public String saveImage(@RequestParam("file") MultipartFile file) throws IOException {
        DBObject metaData = new BasicDBObject();

        System.out.println(file.getOriginalFilename() + " ::: " + file.getSize());
        metaData.put("type", "image");
        System.out.println(file.getInputStream().available());
        String fileid = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metaData).toString();
        System.out.println("FileId" + fileid);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Headers","Authorization");
        responseHeaders.set("access-control-expose-headers","Authorization");
        //return new ResponseEntity<>(fileid , responseHeaders, HttpStatus.OK);
        file.getInputStream().close();
        return fileid;
    }

    @GetMapping("/getImage/{imageId}")
    public byte[] getImage(@PathVariable("imageId") String imageId ) throws IOException {
        GridFSFile gridFsFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(imageId)));
        GridFsResource resource = gridFsTemplate.getResource(gridFsFile.getFilename());
        System.out.println(resource.getContentType());
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType(resource.getContentType()))
//                .contentLength(resource.contentLength())
//                .body(resource);
        return Base64.getEncoder().encode(resource.getInputStream().readAllBytes());
    }
}
