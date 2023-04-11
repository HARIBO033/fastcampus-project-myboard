package com.fastcampus.projectmyboard.service;

import com.fastcampus.projectmyboard.domain.Article;
import com.fastcampus.projectmyboard.domain.ArticleComment;
import com.fastcampus.projectmyboard.dto.ArticleCommentDto;
import com.fastcampus.projectmyboard.repository.ArticleCommentRepository;
import com.fastcampus.projectmyboard.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks
    private ArticleCommentService sut;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ArticleCommentRepository articleCommentRepository;

    @Test
    void articleId로_댓글리스트_조회() {
        Long articleId = 1L;
        given(articleRepository.findById(articleId)).willReturn(Optional.of(Article.of("title", "content", "#java")));
        List<ArticleCommentDto> articleComments = sut.searchArticleComments(articleId);
        assertThat(articleComments).isNotNull();
        then(articleRepository).should().findById(articleId);
    }

    @Test
    void 댓글저장() {
        ArticleCommentDto articleCommentDto = ArticleCommentDto.of(LocalDateTime.now(), "haribo", LocalDateTime.now(), "haribo", "content");
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);
        sut.saveArticleComment(articleCommentDto);
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }
}