package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ute.dto.response.BookDetailResponse;
import ute.entity.Article;
import ute.entity.Book;
import ute.exception.AppException;
import ute.exception.ErrorCode;
import ute.repository.ArticleRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ArticleServices {
    ArticleRepository articleRepository;

    public Page<Article> getAllArticles(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findAllByOrderByDateDesc(pageable);
    }

    public Article getArticlesByID(Integer articleID){
        return articleRepository.findById(articleID).orElseThrow(()->new RuntimeException("Article not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Article addArticle(Article article) {
        if (article.getTitle() == null || article.getTitle().isBlank() ||
                article.getContent() == null || article.getContent().isBlank() ||
                article.getDate() == null  ||
                article.getImage() == null || article.getImage().isBlank()||
                article.getAuthor() == null || article.getAuthor().isBlank()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        if (articleRepository.existsByTitle(article.getTitle())) {
            throw new AppException(ErrorCode.ARTICLE_EXISTED);
        }
        return articleRepository.save(article);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Article editArticle(Integer articleID, Article body) {
        body.setId(articleID);
        return articleRepository.save(body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteArticle(Integer articleID) {
        articleRepository.deleteById(articleID);
    }

}
