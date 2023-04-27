package com.fastcampus.projectmyboard.dto.request;

import com.fastcampus.projectmyboard.dto.ArticleCommentDto;
import com.fastcampus.projectmyboard.dto.UserAccountDto;

public record ArticleCommentRequest(Long articleId, String content) {

  public static ArticleCommentRequest of(Long articleId, String content){
    return new ArticleCommentRequest(articleId, content);
  }

  public ArticleCommentDto toDto(UserAccountDto userAccountDto){
    return ArticleCommentDto.of(articleId,userAccountDto,content);
  }
}
