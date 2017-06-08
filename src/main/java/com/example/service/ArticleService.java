package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Article;
import com.example.repository.ArticleRepository;

/**
 * 記事関連サービスクラス.
 * 
 * @author igamasayuki
 *
 */
@Service
public class ArticleService {

	@Autowired
	private ArticleRepository articleRepository;

	/**
	 * <pre>
	 * 記事一覧を取得します.
	 * 記事に含まれているコメント一覧も同時に取得します
	 * </pre>
	 * 
	 * @return コメントを含んだ記事一覧情報
	 */
	public List<Article> findAll() {
		System.out.println("ArticleService#findAll");
		return articleRepository.findAll();
	}

	/**
	 * 記事を登録します.
	 * 
	 * @param article
	 *            記事情報
	 * @return 登録した記事情報
	 */
	public Article save(Article article) {
		return articleRepository.update(article);
	}

	/**
	 * 記事を削除します。
	 * 
	 * @param id
	 *            削除したい記事ID
	 */
	public void delete(Long id) {
		articleRepository.delete(id);
	}
}
