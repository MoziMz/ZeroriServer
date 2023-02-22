package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    Optional<Tag> findByName(String name);
    @Query(value = "SELECT * FROM tag WHERE tag.name LIKE concat('%',:name,'%')", nativeQuery = true)
    List<Tag> findAllByNameContaining(@Param("name") String name);
}
