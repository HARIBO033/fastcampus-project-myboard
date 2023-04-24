package com.fastcampus.projectmyboard.repository.querydsl;

import java.util.List;

public interface ArticleRepositoryCustom {
  List<String> findAllDistinctHashtags();
}
