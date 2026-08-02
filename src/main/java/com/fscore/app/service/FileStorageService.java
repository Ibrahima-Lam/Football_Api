package com.fscore.app.service;

import com.fscore.app.config.StorageProperties;
import com.fscore.app.exception.FileStorageException;
import com.fscore.app.exception.FileTooLargeException;
import com.fscore.app.exception.InvalidFileTypeException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final String IMAGE_CATEGORY = "images";
    private static final String VIDEO_CATEGORY = "videos";

    private final Path rootLocation;
    private final StorageProperties properties;

    public FileStorageService(StorageProperties properties) {
        this.properties = properties;
        this.rootLocation = Paths.get(properties.getLocation()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new FileStorageException("Fichiers: impossible de créer le répertoire de stockage " + this.rootLocation, e);
        }
    }

    public record StoredFileInfo(String category, String fileName, String relativePath,
                                 String url, String contentType, long size, String originalName) {
    }

    public StoredFileInfo store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileTypeException("Aucun fichier fourni");
        }
        String contentType = file.getContentType() != null ? file.getContentType() : "";
        String category = resolveCategory(contentType);
        long maxSize = category.equals(IMAGE_CATEGORY)
            ? maxImageSize()
            : maxVideoSize();
        if (file.getSize() > maxSize) {
            throw new FileTooLargeException("Fichier trop volumineux (max " + (maxSize / 1024 / 1024) + " Mo)");
        }

        String extension = extractExtension(file.getOriginalFilename(), contentType);
        String fileName = UUID.randomUUID().toString().replace("-", "") + extension;
        Path target = resolve(category, fileName);

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Fichiers: échec de l'enregistrement de " + fileName, e);
        }

        String relativePath = category + "/" + fileName;
        return new StoredFileInfo(
            category,
            fileName,
            relativePath,
            "/uploads/" + relativePath,
            contentType,
            file.getSize(),
            file.getOriginalFilename()
        );
    }

    public Resource load(String relativePath) {
        Path path = resolvePath(relativePath);
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            return null;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public void delete(String relativePath) {
        try {
            Files.deleteIfExists(resolvePath(relativePath));
        } catch (IOException e) {
            throw new FileStorageException("Fichiers: échec de la suppression de " + relativePath, e);
        }
    }

    public long maxImageSize() {
        return properties.getMaxImageSize();
    }

    public long maxVideoSize() {
        return properties.getMaxVideoSize();
    }

    private Path resolve(String category, String fileName) {
        Path dir = this.rootLocation.resolve(category).normalize();
        if (!dir.startsWith(this.rootLocation)) {
            throw new FileStorageException("Chemin de stockage invalide");
        }
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new FileStorageException("Fichiers: impossible de créer " + dir, e);
        }
        return resolvePath(dir.resolve(fileName).normalize());
    }

    private Path resolvePath(Path path) {
        Path normalized = path.normalize();
        if (!normalized.startsWith(this.rootLocation)) {
            throw new FileStorageException("Chemin de fichier invalide: " + normalized);
        }
        return normalized;
    }

    private Path resolvePath(String relativePath) {
        if (!StringUtils.hasText(relativePath)) {
            throw new FileStorageException("Chemin de fichier vide");
        }
        Path path = this.rootLocation.resolve(relativePath).normalize();
        if (!path.startsWith(this.rootLocation)) {
            throw new FileStorageException("Chemin de fichier invalide: " + relativePath);
        }
        return path;
    }

    private String resolveCategory(String contentType) {
        if (contentType.startsWith("image/")) {
            return IMAGE_CATEGORY;
        }
        if (contentType.startsWith("video/")) {
            return VIDEO_CATEGORY;
        }
        throw new InvalidFileTypeException("Type de fichier non autorisé (images et vidéos uniquement): " + contentType);
    }

    private String extractExtension(String originalName, String contentType) {
        if (StringUtils.hasText(originalName)) {
            String name = originalName.toLowerCase(Locale.ROOT);
            int dot = name.lastIndexOf('.');
            if (dot >= 0 && dot < name.length() - 1) {
                String ext = name.substring(dot);
                if (ext.matches("\\.[a-z0-9]{1,8}")) {
                    return ext;
                }
            }
        }
        if (contentType.equals("image/jpeg")) {
            return ".jpg";
        }
        if (contentType.equals("image/svg+xml")) {
            return ".svg";
        }
        if (contentType.equals("video/mp4")) {
            return ".mp4";
        }
        return "." + contentType.substring(contentType.indexOf('/') + 1).replace("+", "").toLowerCase(Locale.ROOT);
    }
}
