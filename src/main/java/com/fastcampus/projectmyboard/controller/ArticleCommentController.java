package com.fastcampus.projectmyboard.controller;

import com.fastcampus.projectmyboard.dto.UserAccountDto;
import com.fastcampus.projectmyboard.dto.request.ArticleCommentRequest;
import com.fastcampus.projectmyboard.dto.security.BoardPrincipal;
import com.fastcampus.projectmyboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {
  private final ArticleCommentService articleCommentService;

  @PostMapping("/new")
  public String postNewArticleComment(ArticleCommentRequest articleCommentRequest, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
    articleCommentService.saveArticleComment(articleCommentRequest.toDto(boardPrincipal.toDto()));
    return "redirect:/articles/" + articleCommentRequest.articleId();//댓글을 작성한 게시물의 아이디로 redirection이 되게끔 하자
  }

  @PostMapping("/{commentId}/delete")
  public String deleteArticleComment(@PathVariable Long commentId, @AuthenticationPrincipal BoardPrincipal boardPrincipal, Long articleId){//댓글을 삭제하고 redirection이 일어날떄 게시물의 id가 필요하다
    articleCommentService.deleteArticleComment(commentId, boardPrincipal.getUsername());
    return "redirect:/articles/" + articleId;
  }
}
