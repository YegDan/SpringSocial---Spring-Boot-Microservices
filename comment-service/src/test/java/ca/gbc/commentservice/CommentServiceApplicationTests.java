package ca.gbc.commentservice;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class CommentServiceApplicationTests extends ContainerBaseTest{
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private MongoTemplate mongoTemplate;

	private CommentRequest getCommentRequest(){
		return CommentRequest.builder()
				.postId("ugieqg9qoeu")
				.userId("iegvuoqeviqe")
				.text("cool")
				.build();
	}
	private List<Comment> getComments(){
		List<Comment> comments = new ArrayList<>();
		UUID id = UUID.randomUUID();
		UUID postId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		Comment comment = Comment.builder()
				.id(id.toString())
				.postId(postId.toString())
				.userId(userId.toString())
				.text("hello")
				.build();
		comments.add(comment);

		return  comments;

	}
	private String convertObjectToString(List<CommentResponse> productList) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(productList);

	}

	private List<CommentResponse> convertStringToObject(String jsonString) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonString, new TypeReference<List<CommentResponse>>(){});
	}

	@Test
	void createComment() throws Exception{
		CommentRequest commentRequest = getCommentRequest();
		String commentRequestJson = objectMapper.writeValueAsString(commentRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(commentRequestJson))
				.andExpect(MockMvcResultMatchers.status().isCreated());
		Assertions.assertTrue(commentRepository.findAll().size()>0);
		Query query = new Query();
		query.addCriteria(Criteria.where("text").is("cool"));
		List<Comment> comments = mongoTemplate.find(query,Comment.class);

		Assertions.assertTrue(comments.size()==1);

	}

	@Test
	void getAllComments() throws Exception{
		commentRepository.saveAll(getComments());

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.get("/api/comments")
				.accept(MediaType.APPLICATION_JSON));

		response.andExpect(MockMvcResultMatchers.status().isOk());
		response.andDo(MockMvcResultHandlers.print());


		MvcResult result = response.andReturn();
		String jsonResponse = result.getResponse().getContentAsString();
		JsonNode jsonNodes = new ObjectMapper().readTree(jsonResponse);

		int actualSize = jsonNodes.size();
		int expectedSize = getComments().size();

		Assertions.assertEquals(expectedSize, actualSize);
	}

	@Test
	void updateComment() throws Exception{

		UUID id = UUID.randomUUID();
		UUID postId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		Comment comment = Comment.builder()
				.id(id.toString())
				.postId(postId.toString())
				.userId(userId.toString())
				.text("hello")
				.build();
		commentRepository.save(comment);
		comment.setText("Actually bye");

		String productRequestString = objectMapper.writeValueAsString(comment);

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.put("/api/comments/" + comment.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString));

		response.andExpect(MockMvcResultMatchers.status().isNoContent());
		response.andDo(MockMvcResultHandlers.print());

		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(comment.getId()));

		Comment storedComment = mongoTemplate.findOne(query, Comment.class);

		assertEquals(comment.getText(),storedComment.getText());

	}
	@Test
	void deleteComment() throws Exception{
		//give
		UUID id = UUID.randomUUID();
		UUID postId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		Comment comment = Comment.builder()
				.id(id.toString())
				.postId(postId.toString())
				.userId(userId.toString())
				.text("hello")
				.build();
		commentRepository.save(comment);

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.delete("/api/comments/" + comment.getId())
				.contentType(MediaType.APPLICATION_JSON));

		response.andExpect(MockMvcResultMatchers.status().isNoContent());
		response.andDo(MockMvcResultHandlers.print());

		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(comment.getId()));
		Long commentCount = mongoTemplate.count(query, Comment.class);

		assertEquals(0, commentCount);

	}

}
