package com.fscore.app.controller;

import com.fscore.app.dto.request.NewsRequest;
import com.fscore.app.dto.response.NewsResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.News;
import com.fscore.app.entity.*;
import com.fscore.app.service.NewsService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService service;
    private final FilterService filterService;

    public NewsController(NewsService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<News> page = filterService.find("newsRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable String id) {
        News entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("News not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<NewsResponse> save(@Valid @RequestBody NewsRequest request) {
        News entity = mapToEntity(request);
        News saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/news/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsResponse> update(@PathVariable String id, @Valid @RequestBody NewsRequest request) {
        News entity = mapToEntity(request);
        News updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private NewsResponse mapToResponse(News entity) {
        NewsResponse response = NewsResponse.builder()
            .author(entity.getAuthor())
            .competitionId(entity.getCompetition() != null ? entity.getCompetition().getId() : null)
            .content(entity.getContent())
            .excerpt(entity.getExcerpt())
            .id(entity.getId())
            .image(entity.getImage())
            .language(entity.getLanguage())
            .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
            .slug(entity.getSlug())
            .sourceUrl(entity.getSourceUrl())
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .title(entity.getTitle())
            .publishedAt(entity.getPublishedAt())
            .build();
        return response;
    }

    private News mapToEntity(NewsRequest request) {
        News news = News.builder()
            .competition(request.getCompetitionId() != null ? Competition.builder().id(request.getCompetitionId()).build() : null)
            .player(request.getPlayerId() != null ? Player.builder().id(request.getPlayerId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .author(request.getAuthor())
            .content(request.getContent())
            .excerpt(request.getExcerpt())
            .image(request.getImage())
            .language(request.getLanguage())
            .slug(request.getSlug())
            .sourceUrl(request.getSourceUrl())
            .title(request.getTitle())
            .publishedAt(request.getPublishedAt())
            .build();
        return news;
    }
}
