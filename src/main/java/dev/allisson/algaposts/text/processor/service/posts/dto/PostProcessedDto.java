package dev.allisson.algaposts.text.processor.service.posts.dto;

import java.math.BigDecimal;

public record PostProcessedDto(
        String postId,
        Integer wordCount,
        BigDecimal calculatedValue) {

}
