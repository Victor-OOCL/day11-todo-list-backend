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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void setUp() {
        todoRepository.deleteAll();
        todoRepository.flush();
        todoRepository.save(new Todo("123", false));
        todoRepository.save(new Todo("12", true));
        todoRepository.save(new Todo("1", false));
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

    @Test
    void should_return_todos_when_add_todo_given_todos() throws Exception {
        //Given
        List<Todo> givenTodos = todoRepository.findAll();
        String requestBody = "{\"text\": \"1234\",\"done\": \"false\" }";
        //When
        //Then
        client.perform(
                        MockMvcRequestBuilders.post("/todos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(false));
    }

    @Test
    void should_return_delete_todo_when_delete_given_id() throws Exception {
        // Given
        Todo deleteTodo = todoRepository.findAll().get(0);
        // When
        final var result = client.perform(MockMvcRequestBuilders.delete("/todos/" + deleteTodo.getId())).andReturn();
        // Then
        assertThat(todoRepository.findAll()).hasSize(2);
        assertThat(todoRepository.findAll()).doesNotContain(deleteTodo);
    }

    @Test
    void should_return_updated_todo_when_update_with_id_and_data() throws Exception {
        // Given
        var idToUpdate = todoRepository.findAll().get(0).getId();
        var textToUpdate = "1234";
        String requestBody = String.format("{\"text\": \"%s\",\"done\":\"%b\" }", textToUpdate, true);
        // When
        // Then
        client.perform(MockMvcRequestBuilders.put("/todos/" + idToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idToUpdate))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(textToUpdate))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(true));
    }

    @Test
    void should_throw_error_when_update_given_not_exist_id() throws Exception {
        // Given
        var idToUpdate = todoRepository.findAll().get(2).getId() + 1;
        var textToUpdate = "1234";
        String requestBody = String.format("{\"text\": \"%s\",\"done\":\"%b\" }", textToUpdate, true);
        // When
        // Then
        client.perform(MockMvcRequestBuilders.put("/todos/" + idToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"))  // 验证 errorCode
                .andExpect(jsonPath("$.errorMessage").value("TODO NOT FOUND"));

    }

}
