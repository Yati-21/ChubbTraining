package com.quiz.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Quiz 
{
	@Id
	private String id;

	private String title;
	
	
	//transient - doesnt save in db ::: questions wont get saved in db
	transient private List<Question> questions;
}
