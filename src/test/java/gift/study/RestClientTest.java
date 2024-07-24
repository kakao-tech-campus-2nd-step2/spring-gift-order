package gift.study;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.Kakao.Commerce;
import gift.DTO.Kakao.Content;
import gift.DTO.Kakao.Link;
import gift.DTO.Kakao.Template;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

public class RestClientTest {
    private final RestClient client = RestClient.builder().build();

    @Test
    void test1() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Link link = new Link("localhost:8080");
        Content content = new Content(
                "title",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-fHdqE2SSTrLLFfvUQCvfiHVeq0fJpEmtYg&s",
                "description",
                link
        );
        Commerce commerce = new Commerce("product_name", 4500);
        Template template = new Template("commerce", content, commerce);

        String template_str = objectMapper.writeValueAsString(template);

        LinkedMultiValueMap<Object, Object> body = new LinkedMultiValueMap<>();
        body.set("template_object", template_str);

        System.out.println("body = " + body);

        ResponseEntity<String> entity = client.post()
                .uri(URI.create("https://kapi.kakao.com/v2/api/talk/memo/default/send"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "8z42d3eQ04So-_AfZ_SZ5PkNvD5ulLkPAAAAAQo8IlIAAAGQ401T8H6MVWkGe_Nf")
                .retrieve()
                .toEntity(String.class);
        System.out.println("entity = " + entity);
    }
}
