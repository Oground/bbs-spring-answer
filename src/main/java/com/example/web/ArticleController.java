package com.example.web;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Article;
import com.example.domain.Comment;
import com.example.service.ArticleService;
import com.example.service.CommentService;

/**
 * 掲示板を操作するコントローラ.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/bbs")
@Transactional
public class ArticleController {
	@Autowired
	private ArticleService articleService;

	@Autowired
	private CommentService commentService;

	/**
	 * 記事のフォームを初期化します.
	 * 
	 * @return 記事フォーム
	 */
	@ModelAttribute
	public ArticleForm setUpArticleForm() {
		return new ArticleForm();
	}

	/**
	 * コメントのフォームを初期化します.
	 * 
	 * @return コメントフォーム
	 */
	@ModelAttribute
	public CommentForm setUpCommentForm() {
		return new CommentForm();
	}

	/**
	 * 掲示板を表示します.
	 * 
	 * @return 掲示板画面
	 */
	@RequestMapping
	public String form(Model model) {
		List<Article> articleList = articleService.findAll();
		model.addAttribute("articleList", articleList);
		return "bbs";
	}

	/**
	 * 記事投稿.
	 * 
	 * @param form
	 *            フォーム
	 * @param result
	 *            リザルト
	 * @param model
	 *            モデル
	 * @return 掲示板画面
	 */
	@RequestMapping(value = "/postarticle")
	public String postarticle(@Validated ArticleForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return form(model);
		}
		Article article = new Article();
		BeanUtils.copyProperties(form, article);
		articleService.save(article);
		return "redirect:/bbs";
	}

	/**
	 * コメント投稿.
	 * 
	 * @param form
	 *            フォーム
	 * @param result
	 *            リザルト
	 * @param model
	 *            モデル
	 * @return 掲示板画面
	 */
	@RequestMapping(value = "/postcomment")
	public String postcomment(@Validated CommentForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return form(model);
		}
		Comment comment = new Comment();
		comment.setArticleId(form.getArticleId());
		comment.setName(form.getName());
		comment.setContent(form.getContent());
		commentService.save(comment);
		return "redirect:/bbs";
	}

	/**
	 * 記事を削除します.
	 * 
	 * @param form
	 *            記事フォーム
	 * @return 記事登録画面
	 */
	@RequestMapping(value = "/deletearticle")
	public String deletearticle(ArticleForm form) {
		articleService.delete(form.getId());
		return "redirect:/bbs";
	}
}
