package com.example.deadlock.original;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeatureFlags {

    private final FeatureFlagsRepository featureFlagsRepository;

    public boolean isEnable(final String feature) {
        final FeatureFlag featureFlag = featureFlagsRepository.findByFeature(feature);
        return featureFlag.isEnable();
    }
}
