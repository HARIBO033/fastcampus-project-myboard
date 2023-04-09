package com.fastcampus.projectmyboard.repository;

import com.fastcampus.projectmyboard.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}