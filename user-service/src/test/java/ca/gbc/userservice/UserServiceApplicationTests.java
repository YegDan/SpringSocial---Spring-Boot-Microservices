package ca.gbc.userservice;

import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.model.User;
import ca.gbc.userservice.repository.UserRepository;
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
import java.util.Queue;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc
class UserServiceApplicationTests extends ContainerBaseTest{

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	private UserRequest makeUserRequest(){
		return UserRequest.builder()
				.fullName("Harleen Hammar")
				.username("Harlee03")
				.password("2003Heen")
				.email("Hupee@yahoo.com")
				.bio("In my influencer era")
				.build();
	}
	private List<String> getPosts(){
		List<String> posts = new ArrayList<>();
		posts.add("1guh736983");
		posts.add("biduqgc189");

		return  posts;
	}
	private List<String> getFollowers(){
		List<String> followers = new ArrayList<>();
		followers.add("Ziba_3");
		followers.add("ImSaharrr");

		return  followers;
	}
	private List<String> getFollowerings(){
		List<String> followerings = new ArrayList<>();
		followerings.add("Eric_nam");
		followerings.add("Judith.danow");

		return  followerings;
	}

	private List<User> getUsers(){
		List<User> users = new ArrayList<>();
		UUID uuid = UUID.randomUUID();

		User user = User.builder()
				.id(uuid.toString())
				.fullName("Harleen Hammar")
				.username("Harlee03")
				.password("2003Heen")
				.email("Hupee@yahoo.com")
				.bio("In my influencer era")
				.posts(getPosts())
				.followers(getFollowers())
				.followings(getFollowerings())
				.build();

		return users;
	}

	private String objectToStringMapper(List<UserResponse> users) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(users);

	}

	private List<UserResponse> stringToObjectMapper(String jsonString) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonString, new TypeReference<List<UserResponse>>(){});
	}

	@Test
	void createUser() throws Exception{

		UserRequest userRequest = makeUserRequest();

		String userJson = objectMapper.writeValueAsString(userRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/myAccount")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson))
				.andExpect(MockMvcResultMatchers.status().isCreated());
		Assertions.assertTrue(userRepository.findAll().size() > 0);
		Query query = new Query();
		query.addCriteria(Criteria.where("fullName").is("Harleen Hammar"));
		List<User> user = mongoTemplate.find(query, User.class);

		Assertions.assertTrue(user.size()==1);


	}

	@Test
	void getAllUsers() throws Exception{

		userRepository.saveAll(getUsers());

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.
				get("/api/myAccount")
				.accept(MediaType.APPLICATION_JSON));

		response.andExpect(MockMvcResultMatchers.status().isOk());
		response.andDo(MockMvcResultHandlers.print());

		MvcResult result = response.andReturn();
		String jsonResponse = result.getResponse().getContentAsString();
		JsonNode jsonNodes = new ObjectMapper().readTree(jsonResponse);

		int actualSize = jsonNodes.size();
		int expectedSize = getUsers().size();

		Assertions.assertEquals(expectedSize, actualSize);
	}

	@Test
	void updateUser() throws Exception{
		User user = User.builder().id(UUID.randomUUID().toString())
				.fullName("Harleen Hammar")
				.username("Harlee03")
				.password("2003Heen")
				.email("Hupee@yahoo.com")
				.bio("In my influencer era")
				.posts(getPosts())
				.followers(getFollowers())
				.followings(getFollowerings())
				.build();
		userRepository.save(user);
		user.setUsername("Harlee05");
		String userRequestJson = objectMapper.writeValueAsString(user);

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.put("/api/myAccount/" + user.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(userRequestJson));

		response.andExpect(MockMvcResultMatchers.status().isNoContent());
		response.andDo(MockMvcResultHandlers.print());

		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(user.getId()));
		User storedUser = mongoTemplate.findOne(query, User.class);

		assertEquals(user.getUsername(),storedUser.getUsername());


	}

	@Test

	void deleteUser() throws Exception{
		User user = User.builder().id(UUID.randomUUID().toString())
				.fullName("Harleen Hammar")
				.username("Harlee03")
				.password("2003Heen")
				.email("Hupee@yahoo.com")
				.bio("In my influencer era")
				.posts(getPosts())
				.followers(getFollowers())
				.followings(getFollowerings())
				.build();

		userRepository.save(user);

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.delete("/api/myAccount/" + user.getId())
				.contentType(MediaType.APPLICATION_JSON));

		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(user.getId()));
		Long userCount = mongoTemplate.count(query, User.class);

		assertEquals(0, userCount);

	}





}
