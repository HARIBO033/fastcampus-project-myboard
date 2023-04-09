package com.fastcampus.projectmyboard.repository;

import com.fastcampus.projectmyboard.config.JpaConfig;
import com.fastcampus.projectmyboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("jpa test")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select test")
    @Test
    void selectTest() {
        List<Article> articles = articleRepository.findAll();

        assertThat(articles).isNotNull().hasSize(100);
    }

    @Test
    @DisplayName("insert 테스트")
    void insertTest() {
        //Given
        long previousCount = articleRepository.count();
        //When
        Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#spring"));
        //Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @Test
    @DisplayName("update 테스트")
    void updateTest() {
        //Given - articleRepository 로 1번 article 을 가져와(없으면 orElseThrow()로 던져줌)
        // article 객체에 담아주고 1번 article 의 해쉬태그를 updatedHashtag 로 바꿔준다
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#java";
        article.setHashtag(updatedHashtag);

        //When -
        Article updatedArticle = articleRepository.saveAndFlush(article);

        //Then - article.getHashtag 와 updatedArticle 의 Hashtag 를 비교하자
        assertThat(updatedArticle).hasFieldOrPropertyWithValue("hashtag",updatedHashtag);
        //assertThat(updatedHashtag).isNotEqualTo(updatedArticle.getHashtag());
    }

    @Test
    @DisplayName("delete 테스트")
    void deleteTest() {
        /*article 객체에 1번 게시물을 담고 삭제하기전 article과 articleComment 의 count를 저장 article객체에 바인딩되어있는
        article의 articleComment의 사이즈를 (int)deleteCommentsSize에 저장
        1번 article 삭제
        articleCommentRepository에 article이 가지고있던 Comment들이 몇개나 삭제되었는지 검사*/

        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        //도메인에 article을 삭제하면 연관된 articleComment도 삭제하도록 cascade 설계를 해놓았기 때문에 같이 검사
        long previousArticleCommentCount = articleRepository.count();
        int deleteCommentsSize = article.getArticleComments().size();
        //When
        articleRepository.delete(article);

        //Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deleteCommentsSize );
    }
}
