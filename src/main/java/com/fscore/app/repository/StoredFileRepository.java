package com.fscore.app.repository;

import com.fscore.app.entity.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoredFileRepository extends JpaRepository<StoredFile, String>, JpaSpecificationExecutor<StoredFile> {

    Optional<StoredFile> findByUrlPath(String urlPath);
}
