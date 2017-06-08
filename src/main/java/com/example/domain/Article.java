package com.example.domain;

import java.util.List;

/**
 * 記事を表すエンティティ.
 * 
 * @author igamasayuki
 */
public class Article {

	/** id */
	public Long id;

	/** 名前 */
	public String name;

	/** 内容 */
	public String content;

	/** コメント一覧 */
	public List<Comment> commentList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	
}
