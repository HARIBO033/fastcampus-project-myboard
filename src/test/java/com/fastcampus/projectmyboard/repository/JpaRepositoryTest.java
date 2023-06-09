package com.fastcampus.projectmyboard.repository;

import com.fastcampus.projectmyboard.domain.Article;
import com.fastcampus.projectmyboard.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

  private final ArticleRepository articleRepository;
  private final ArticleCommentRepository articleCommentRepository;
  private final UserAccountRepository userAccountRepository;

  public JpaRepositoryTest(
          @Autowired ArticleRepository articleRepository,
          @Autowired ArticleCommentRepository articleCommentRepository,
          @Autowired UserAccountRepository userAccountRepository) {
    this.articleRepository = articleRepository;
    this.articleCommentRepository = articleCommentRepository;
    this.userAccountRepository = userAccountRepository;
  }

  @DisplayName("select 테스트")
  @Test
  void givenTestData_whenSelecting_thenWorksFine() {
    // Given

    // When
    List<Article> articles = articleRepository.findAll();

    // Then
    assertThat(articles)
            .isNotNull()
            .hasSize(123);
  }

  //@Disabled // TODO : UserAccountRepository 아직 안만들어서 insert 테스트 Disabled 처리 해놓음, 추가예정
  @DisplayName("insert 테스트")
  @Test
  void givenTestData_whenInserting_thenWorksFine() {
    // Given
    long previousCount = articleRepository.count();
    UserAccount userAccount = userAccountRepository.save(UserAccount.of("newHaribo", "pw", null, null, null));
    Article article = Article.of(userAccount, "new article", "new content", "#spring");

    // When

    articleRepository.save(article);


    // Then
    assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
  }

  @DisplayName("update 테스트")
  @Test
  void givenTestData_whenUpdating_thenWorksFine() {
    // Given
    Article article = articleRepository.findById(1L).orElseThrow();
    String updatedHashtag = "#springboot";
    article.setHashtag(updatedHashtag);

    // When
    Article savedArticle = articleRepository.saveAndFlush(article);

    // Then
    assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
  }

  @DisplayName("delete 테스트")
  @Test
  void givenTestData_whenDeleting_thenWorksFine() {
    // Given
    Article article = articleRepository.findById(1L).orElseThrow();
    long previousArticleCount = articleRepository.count();
    long previousArticleCommentCount = articleCommentRepository.count();
    int deletedCommentsSize = article.getArticleComments().size();

    // When
    articleRepository.delete(article);

    // Then
    assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
    assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
  }

  @TestConfiguration // test할 때만 Configuration빈으로 등록
  public static class TestJpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
      return () -> Optional.of("uno");
    }
  }
}