package com.fastcampus.projectmyboard.controller;

import com.fastcampus.projectmyboard.config.SecurityConfig;
import com.fastcampus.projectmyboard.config.TestSecurityConfig;
import com.fastcampus.projectmyboard.dto.ArticleCommentDto;
import com.fastcampus.projectmyboard.dto.request.ArticleCommentRequest;
import com.fastcampus.projectmyboard.service.ArticleCommentService;
import com.fastcampus.projectmyboard.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Import({TestSecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(ArticleCommentController.class)
class ArticleCommentControllerTest {
  private final MockMvc mvc;
  private final FormDataEncoder formDataEncoder;

  @MockBean
  private ArticleCommentService articleCommentService;

  //@Autowired
  public ArticleCommentControllerTest(@Autowired MockMvc mvc, @Autowired FormDataEncoder formDataEncoder) {
    this.mvc = mvc;
    this.formDataEncoder = formDataEncoder;
  }

  @WithUserDetails(value = "hariboTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  @DisplayName("[view][POST] 댓글 등록 - 정상 호출")
  @Test
  void givenArticleCommentInfo_whenRequesting_thenSavesNewArticleComment() throws Exception {
    // Given
    long articleId = 1L;
    ArticleCommentRequest request = ArticleCommentRequest.of(articleId, "test comment");
    willDoNothing().given(articleCommentService).saveArticleComment(any(ArticleCommentDto.class));

    // When & Then
    mvc.perform(
                    post("/comments/new")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content(formDataEncoder.encode(request))//formDataEncoder 데이터형식으로 request 인코딩
                            .with(csrf())//csrf검증
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/articles/" + articleId))
            .andExpect(redirectedUrl("/articles/" + articleId));
    then(articleCommentService).should().saveArticleComment(any(ArticleCommentDto.class));
  }
  @WithUserDetails(value = "hariboTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  @DisplayName("[view][GET] 댓글 삭제 - 정상 호출")
  @Test
  void givenArticleCommentIdToDelete_whenRequesting_thenDeletesArticleComment() throws Exception {
    // Given
    long articleId = 1L;
    long articleCommentId = 1L;
    String userId = "hariboTest";
    willDoNothing().given(articleCommentService).deleteArticleComment(articleCommentId, userId);

    // When & Then
    mvc.perform(
                    post("/comments/" + articleCommentId + "/delete")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content(formDataEncoder.encode(Map.of("articleId", articleId)))
                            .with(csrf())
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/articles/" + articleId))
            .andExpect(redirectedUrl("/articles/" + articleId)); //redirection이 잘 일어나는지
    then(articleCommentService).should().deleteArticleComment(articleCommentId, userId);
  }

}
