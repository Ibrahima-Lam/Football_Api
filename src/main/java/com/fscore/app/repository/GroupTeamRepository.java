package com.fscore.app.repository;

import com.fscore.app.entity.GroupTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupTeamRepository extends JpaRepository<GroupTeam, String>, JpaSpecificationExecutor<GroupTeam> {

    List<GroupTeam> findByGroupId(String groupId);
}
