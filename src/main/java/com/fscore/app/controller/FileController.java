package com.fscore.app.controller;

import com.fscore.app.dto.response.FileResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.StoredFile;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.service.FileStorageService;
import com.fscore.app.service.FilterService;
import com.fscore.app.service.StoredFileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;
    private final StoredFileService storedFileService;
    private final FilterService filterService;

    public FileController(FileStorageService fileStorageService,
                          StoredFileService storedFileService,
                          FilterService filterService) {
        this.fileStorageService = fileStorageService;
        this.storedFileService = storedFileService;
        this.filterService = filterService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<FileResponse> upload(@RequestParam("file") MultipartFile file) {
        FileStorageService.StoredFileInfo info = fileStorageService.store(file);
        StoredFile stored = StoredFile.builder()
            .originalName(info.originalName())
            .fileName(info.fileName())
            .category(info.category())
            .contentType(info.contentType())
            .size(info.size())
            .urlPath(info.url())
            .build();
        StoredFile saved = storedFileService.save(stored);
        return ResponseEntity.created(URI.create("/api/files/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<StoredFile> page = filterService.find("storedFileRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileResponse> findById(@PathVariable String id) {
        StoredFile entity = storedFileService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fichier introuvable avec l'id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        storedFileService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private FileResponse mapToResponse(StoredFile entity) {
        return FileResponse.builder()
            .id(entity.getId())
            .originalName(entity.getOriginalName())
            .fileName(entity.getFileName())
            .category(entity.getCategory())
            .contentType(entity.getContentType())
            .size(entity.getSize())
            .url(entity.getUrlPath())
            .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null)
            .build();
    }
}
