package com.fscore.app.repository;

import com.fscore.app.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, String>, JpaSpecificationExecutor<News> {
    java.util.List<News> findByCompetitionIdAndDeletedAtIsNullOrderByPublishedAtDesc(String competitionId);
    java.util.List<News> findByTeamIdAndDeletedAtIsNullOrderByPublishedAtDesc(String teamId);
    java.util.List<News> findByDeletedAtIsNullOrderByPublishedAtDesc();
}
