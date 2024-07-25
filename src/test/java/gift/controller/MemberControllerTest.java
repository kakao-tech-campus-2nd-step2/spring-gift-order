package gift.controller;

import gift.dto.MemberDto;
import gift.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
@ActiveProfiles("test")
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private MemberDto memberDto;

    @BeforeEach
    void setUp() {
        memberDto = new MemberDto(1L, "john.doe@example.com", "password123", "token");
    }

    @Test
    void registerMember() throws Exception {
        given(memberService.register(any(MemberDto.class))).willReturn(memberDto);

        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"john.doe@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(memberDto.getId()))
                .andExpect(jsonPath("$.email").value(memberDto.getEmail()))
                .andExpect(jsonPath("$.token").value(memberDto.getToken()));

        Mockito.verify(memberService).register(any(MemberDto.class));
    }

    @Test
    void loginMember() throws Exception {
        given(memberService.login("john.doe@example.com", "password123")).willReturn(memberDto);

        mockMvc.perform(post("/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"john.doe@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(memberDto.getId()))
                .andExpect(jsonPath("$.email").value(memberDto.getEmail()))
                .andExpect(jsonPath("$.token").value(memberDto.getToken()));

        Mockito.verify(memberService).login("john.doe@example.com", "password123");
    }
}
