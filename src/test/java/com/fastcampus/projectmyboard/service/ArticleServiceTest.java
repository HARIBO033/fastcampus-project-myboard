package com.fastcampus.projectmyboard.service;


import com.fastcampus.projectmyboard.domain.Article;
import com.fastcampus.projectmyboard.domain.type.SearchType;
import com.fastcampus.projectmyboard.dto.ArticleDto;
import com.fastcampus.projectmyboard.dto.ArticleUpdateDto;
import com.fastcampus.projectmyboard.repository.ArticleRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut;
    @Mock
    private ArticleRepository articleRepository;

    @Test
    void 게시글을_검색하면_게시글리스트_반환_테스트() {
        Page<ArticleDto> articleDto = sut.searchArticles(SearchType.TITLE, "searchKeyword");

        assertThat(articleDto).isNotNull();
    }

    @Test
    void 게시글_Id로_조회하면_게시글을_반환_테스트() {

        ArticleDto articleDto = sut.searchArticle(1L);
        assertThat(articleDto).isNotNull();
    }

    @Test
    void 게시글정보를_입력하면_게시글을_생성() {
        ArticleDto articleDto = ArticleDto.of(LocalDateTime.now(), "haribo", "title", "content", "#java");
        given(articleRepository.save(any(Article.class))).willReturn(null);
        sut.saveArticle(articleDto);
        then(articleRepository).should().save(any(Article.class));
    }

    @Test
    void 게시글_ID와_수정정보를_입력하면_게시물_수정() {
        given(articleRepository.save(any(Article.class))).willReturn(null);
        sut.updateArticle(1L, ArticleUpdateDto.of("title", "content","#java"));
        then(articleRepository).should().save(any(Article.class));
    }

    @Test
    void 게시글_ID로_게시글_삭제() {
        willDoNothing().given(articleRepository).delete(any(Article.class));
        sut.deleteArticle(1L);
        then(articleRepository).should().delete(any(Article.class));
    }
}
