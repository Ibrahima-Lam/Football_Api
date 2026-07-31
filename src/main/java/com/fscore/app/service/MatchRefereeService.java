package com.fscore.app.service;

import com.fscore.app.entity.MatchReferee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface MatchRefereeService {
    Page<MatchReferee> findAll(Pageable pageable);
    Optional<MatchReferee> findById(String id);
    MatchReferee save(MatchReferee entity);
    MatchReferee update(String id, MatchReferee entity);
    void delete(String id);
}
