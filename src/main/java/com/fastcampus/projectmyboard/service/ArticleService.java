package com.fastcampus.projectmyboard.service;

import com.fastcampus.projectmyboard.domain.Article;
import com.fastcampus.projectmyboard.domain.type.SearchType;
import com.fastcampus.projectmyboard.dto.ArticleDto;
import com.fastcampus.projectmyboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectmyboard.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

  private final ArticleRepository articleRepository;

  @Transactional(readOnly = true)
  public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
    if (searchKeyword == null || searchKeyword.isBlank()) {
      return articleRepository.findAll(pageable).map(ArticleDto::from);
    }

    return switch (searchType) {
      case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
      case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
      case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
      case NICKNAME ->
              articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
      case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
    };
  }

  @Transactional(readOnly = true)
  public ArticleWithCommentsDto getArticle(Long articleId) {
    return articleRepository.findById(articleId)
            .map(ArticleWithCommentsDto::from)
            .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
  }

  public void saveArticle(ArticleDto dto) {
    articleRepository.save(dto.toEntity());
  }

  public void updateArticle(ArticleDto dto) {
    try {
      Article article = articleRepository.getReferenceById(dto.id());
      if (dto.title() != null) {
        article.setTitle(dto.title());
      }//dto의 타입을 record로 선언해놔서 dto.getTitle이 아니라 dto.title 로 쓸 수 있다.(자바 13,14버전 부터 사용가능)
      if (dto.content() != null) {
        article.setContent(dto.content());
      }
      article.setHashtag(dto.hashtag());
      //Transactional 로 묶여있어서 save()를 하지않아도 업데이트가 된다
    } catch (EntityNotFoundException e) {
      log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto);
    }
  }

  public void deleteArticle(long articleId) {
    articleRepository.deleteById(articleId);
  }

  public long getArticleCount(){
    return articleRepository.count();
  }

  @Transactional(readOnly = true)
  public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable) {
    if(hashtag == null || hashtag.isBlank()){
      return Page.empty(pageable);
    }
    return articleRepository.findByHashtag(hashtag,pageable).map(ArticleDto::from);
  }

  public List<String> getHashtags() {
    return articleRepository.findAllDistinctHashtags();
  }
}
