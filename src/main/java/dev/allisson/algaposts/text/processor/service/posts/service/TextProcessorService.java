package dev.allisson.algaposts.text.processor.service.posts.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import dev.allisson.algaposts.text.processor.service.posts.dto.PostCreatedDto;
import dev.allisson.algaposts.text.processor.service.posts.dto.PostProcessedDto;

@Service
public class TextProcessorService {

    public PostProcessedDto processPost(final PostCreatedDto postCreatedDto) {
        final int wordCount = postCreatedDto.postBody().split("\\s+").length;
        final BigDecimal calculatedValue = BigDecimal.valueOf(wordCount)
                .multiply(BigDecimal.valueOf(0.1))
                .setScale(2, RoundingMode.HALF_EVEN);
        return new PostProcessedDto(
                postCreatedDto.postId(),
                wordCount,
                calculatedValue);
    }

}
