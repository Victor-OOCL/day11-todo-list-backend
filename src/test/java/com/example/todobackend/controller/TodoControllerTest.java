package com.example.todobackend.controller;

import com.example.todobackend.model.Todo;
import com.example.todobackend.repository.TodoRepository;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
public class TodoControllerTest {
    @Autowired
    private MockMvc client;
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private JacksonTester<List<Todo>> todoListJacksonTester;

    @BeforeEach
    void setUp(){
        todoRepository.deleteAll();
        todoRepository.flush();
        todoRepository.save(new Todo("123",false));
        todoRepository.save(new Todo("12",true));
        todoRepository.save(new Todo("1",false));
    }

    @Test
    void should_return_todos_when_get_all_todos_given_todos() throws Exception {
        //Given
        List<Todo> givenTodos = todoRepository.findAll();
        //When
        final MvcResult result = client.perform(MockMvcRequestBuilders.get("/todos")).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        final List<Todo> fetchedTodos = todoListJacksonTester.parseObject(response.getContentAsString());
        assertThat(fetchedTodos).hasSameSizeAs(givenTodos);
        assertThat(fetchedTodos)
                .usingRecursiveComparison(
                        RecursiveComparisonConfiguration.builder()
                                .withComparedFields("text", "done")
                                .build()
                )
                .isEqualTo(givenTodos);
    }

}
