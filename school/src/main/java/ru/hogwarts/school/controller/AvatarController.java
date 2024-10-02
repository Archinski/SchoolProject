package ru.hogwarts.school.controller;

import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.MediaList;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable("studentId") Long studentId,
                                               @RequestParam("file") MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok("Аватар успешно загружен");
    }

    @GetMapping("/from-db/{studentId}")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable Long studentId) {
        byte[] imageData = avatarService.getAvatarDataFromDb(studentId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageData);
    }

    // Эндпоинт для получения аватара с диска
    @GetMapping("/from-file/{studentId}")
    public ResponseEntity<Resource> getAvatarFromFile(@PathVariable Long studentId) throws MalformedURLException, FileNotFoundException {
        Resource resource = (Resource) avatarService.getAvatarFromFile(studentId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}

