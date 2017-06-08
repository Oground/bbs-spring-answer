package com.example.repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Article;
import com.example.domain.Comment;

/**
 * articlesテーブル操作用のリポジトリクラス.
 * 
 * @author igamasayuki
 *
 */
@Repository
public class ArticleRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * articlesとcommentsテーブルを結合したものからarticleリストを作成する.<br>
	 * articleオブジェクト内にはcommentリストを格納する。
	 */
	private static final ResultSetExtractor<List<Article>> ARTICLE_RESULT_SET_EXTRACTOR = (rs) -> {
		List<Article> articleList = new LinkedList<Article>();
		Article article = null;
		List<Comment> commentList = null;
		long beforeArticleId = 0;
		while (rs.next()) {
			if (rs.getInt("id") != beforeArticleId) {
				article = new Article();
				article.setId(rs.getLong("id"));
				article.setName(rs.getString("name"));
				article.setContent(rs.getString("content"));
				commentList = new ArrayList<Comment>();
				article.setCommentList(commentList);
				articleList.add(article);
			}
			if (rs.getInt("com_id") != 0) {
				Comment comment = new Comment();
				comment.setId(rs.getLong("com_id"));
				comment.setName(rs.getString("com_name"));
				comment.setContent(rs.getString("com_content"));
				commentList.add(comment);
			}
			beforeArticleId = article.getId();
		}
		return articleList;
	};

	/**
	 * <pre>
	 * 記事一覧を取得します.記事に含まれているコメント一覧も同時に取得します
	 * </pre>
	 * 
	 * @return コメントを含んだ記事一覧情報
	 */
	public List<Article> findAll() {
		String sql = "select a.id, a.name, a.content, com.id com_id, com.name com_name, com.content com_content,com.article_id "
				+ "from articles a left join comments com on a.id = com.article_id order by a.id desc, com.id;";
		List<Article> articleList = jdbcTemplate.query(sql, ARTICLE_RESULT_SET_EXTRACTOR);

		return articleList;
	}

	/**
	 * 記事をインサートします.
	 * 
	 * @param article
	 *            記事
	 * @return 記事
	 */
	public Article update(Article article) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(article);
		String sql = "INSERT INTO articles(name, content) VALUES(:name, :content)";
		if (article.getId() != null) {
			throw new NullPointerException();
		}
		namedParameterJdbcTemplate.update(sql, param);
		return article;
	}

	/**
	 * 記事をDBから削除する. <br>
	 * コメントも同時に削除する.
	 * 
	 * @param id
	 *            削除したい記事ID
	 */
	public void delete(Long articleId) {
		SqlParameterSource sqlparam = new MapSqlParameterSource().addValue("id", articleId);
		String sql = "WITH deleted AS (DELETE FROM articles WHERE id = :id RETURNING id)"
				+ "DELETE FROM comments WHERE article_id IN (SELECT id FROM deleted)";

		namedParameterJdbcTemplate.update(sql, sqlparam);
	}
}