package com.example.deadlock.original.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository extends JpaRepository<BaseEntity, Long> {
}
