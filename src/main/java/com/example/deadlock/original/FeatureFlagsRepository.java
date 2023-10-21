package com.example.deadlock.original;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureFlagsRepository extends JpaRepository<FeatureFlag, Long> {
    FeatureFlag findByFeature(String feature);
}
