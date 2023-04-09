package com.fastcampus.projectmyboard.repository;

import com.fastcampus.projectmyboard.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}