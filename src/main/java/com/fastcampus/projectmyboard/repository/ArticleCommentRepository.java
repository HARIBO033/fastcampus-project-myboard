package com.fastcampus.projectmyboard.repository;

import com.fastcampus.projectmyboard.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource //spring data rest 사용 할 준비
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}