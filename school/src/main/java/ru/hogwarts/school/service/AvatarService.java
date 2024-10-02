package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.FileNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentRepository.findById(studentId).
                orElseThrow(() -> new StudentNotFoundException(studentId));

        Path filePath = saveFileToDisk(avatarFile);

        Avatar avatar = new Avatar();
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }



    private Path saveFileToDisk(MultipartFile file) throws IOException {
        Path directoryPath = Paths.get(avatarsDir);
        Files.createDirectories(directoryPath); // Создаем папку, если её нет

        Path filePath = directoryPath.resolve(file.getOriginalFilename());

        Files.write(filePath, file.getBytes());

        return filePath;
    }

    public byte[] getAvatarDataFromDb(Long studentId) {
        Avatar avatar = avatarRepository.findByStudentId(studentId);
        return avatar.getData();
    }

    public UrlResource getAvatarFromFile(Long studentId) throws MalformedURLException, ru.hogwarts.school.exception.FileNotFoundException {
        Avatar avatar = avatarRepository.findByStudentId(studentId);
        Path filePath = Paths.get(avatar.getFilePath());
        UrlResource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException("Не найден аватар файл по student ID: " + studentId);
        }
    }
}
