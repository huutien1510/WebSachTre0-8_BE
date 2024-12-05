package ute.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ute.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Integer> {
    Page<Article> findAllByOrderByDateDesc(Pageable pageable);
    boolean existsByTitle(String title);
}
