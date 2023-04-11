package com.fastcampus.projectmyboard.service;


import com.fastcampus.projectmyboard.domain.type.SearchType;
import com.fastcampus.projectmyboard.dto.ArticleDto;
import com.fastcampus.projectmyboard.dto.ArticleUpdateDto;
import com.fastcampus.projectmyboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType title, String searchKeyword) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleDto searchArticle(long l) {
        return null;
    }

    public void saveArticle(ArticleDto articleDto) {
        //articleRepository.save(articleDto);
    }

    public void updateArticle(Long id, ArticleUpdateDto articleUpdateDto){

    }

    public void deleteArticle(long id) {
    }
}
