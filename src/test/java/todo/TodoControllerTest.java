package todo;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import todo.model.CreateTodoItem;
import todo.model.TodoItem;
import todo.model.TodoStatus;

@WebMvcTest(TodoController.class)
class TodoControllerTest {
    private static final UUID ID = UUID.randomUUID();
    private static final String TEXT = randomAlphabetic(8);

    @Autowired
    public MockMvc mockMvc;
    @MockBean
    private TodoService service;
    @Autowired
    private ObjectMapper objectMapper;

    @NotNull
    private static TodoItem newTodoItem() {
        return new TodoItem().id(UUID.randomUUID()).text(randomAlphabetic(8));
    }

    @Test
    void allTodoItems() throws Exception {
        when(service.allTodoItems()).thenReturn(List.of(newTodoItem(), newTodoItem()));

        mockMvc.perform(get("/api/v1/todo/")) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.size()").value(2));

        verify(service).allTodoItems();
    }

    @Test
    void changeTodoItem() throws Exception {
        final TodoStatus status = new TodoStatus().done(false);

        mockMvc.perform(patch("/api/v1/todo/" + ID) //
                .contentType(APPLICATION_JSON) //
                .content(objectMapper.writeValueAsString(status))) //
                .andExpect(status().isNoContent());

        verify(service).updateStatus(ID, status);
    }

    @Test
    void createTodoItem() throws Exception {
        final CreateTodoItem newItem = new CreateTodoItem().text(TEXT);
        when(service.save(newItem)).thenReturn(new TodoItem().id(ID).text(TEXT));

        mockMvc.perform(post("/api/v1/todo/") //
                .contentType(APPLICATION_JSON) //
                .content(objectMapper.writeValueAsString(newItem))) //
                .andExpect(status().isCreated()) //
                .andExpect(jsonPath("$.id").value(ID.toString())) //
                .andExpect(jsonPath("$.text").value(TEXT));

        verify(service).save(newItem);
    }

    @Test
    void deleteTodoItem() throws Exception {
        mockMvc.perform(delete("/api/v1/todo/" + ID))//
                .andExpect(status().isNoContent());

        verify(service).delete(ID);
    }
}