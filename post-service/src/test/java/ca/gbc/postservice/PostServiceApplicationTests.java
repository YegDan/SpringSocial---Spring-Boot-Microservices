package ca.gbc.postservice;

import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.dto.PostResponse;
import ca.gbc.postservice.model.Post;
import ca.gbc.postservice.repository.PostRepository;
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
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class PostServiceApplicationTests extends ContainerBaseTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    private PostRequest makePostRequest() {
        return PostRequest.builder()
                .caption("First post!")
                .userId("buicboicboiec")
                .build();
    }

    private List<String> getComments() {
        List<String> comments = new ArrayList<>();
        comments.add("Amazing");
        comments.add("Woww");

        return  comments;
    }

    private List<Post> getPosts() {
        List<Post> users = new ArrayList<>();
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Post post = Post.builder()
                .id(postId.toString())
                .caption("First post!")
                .likes(200)
                .userId(userId.toString())
                .commentId(getComments())
                .build();

        return users;
    }


    private String objectToStringMapper(List<PostResponse> users) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(users);

    }

    private List<PostResponse> stringToObjectMapper(String jsonString) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<List<PostResponse>>(){});
    }

    @Test
    void createPost() throws Exception {

        PostRequest postRequest = makePostRequest();

        String postJson = objectMapper.writeValueAsString(postRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Assertions.assertTrue(postRepository.findAll().size() > 0);
        Query query = new Query();
        query.addCriteria(Criteria.where("caption").is("First post!"));
        List<Post> post = mongoTemplate.find(query, Post.class);

        Assertions.assertTrue(post.size()==1);

    }

    @Test
    void getAllPosts() throws Exception {

        postRepository.saveAll(getPosts());


        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.
                get("/api/post")
                .accept(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andDo(MockMvcResultHandlers.print());

        MvcResult result = response.andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNodes = new ObjectMapper().readTree(jsonResponse);

        int actualSize = jsonNodes.size();
        int expectedSize = getPosts().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void updatePost() throws Exception {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Post post = Post.builder()
                .id(postId.toString())
                .caption("First post!")
                .likes(200)
                .userId(userId.toString())
                .commentId(getComments())
                .build();

        postRepository.save(post);
        post.setCaption("First post with Deanna!");
        String postRequestJson = objectMapper.writeValueAsString(post);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/post/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(postRequestJson));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(post.getId()));
        Post storedPost = mongoTemplate.findOne(query, Post.class);

        Assertions.assertEquals(post.getCaption(),storedPost.getCaption());

    }

    @Test
    void deletePost() throws Exception{
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Post post = Post.builder()
                .id(postId.toString())
                .caption("First post!")
                .likes(200)
                .userId(userId.toString())
                .commentId(getComments())
                .build();

        postRepository.save(post);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/post/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON));

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(post.getId()));
        Long postCount = mongoTemplate.count(query, Post.class);

        Assertions.assertEquals(0, postCount);

    }





}
