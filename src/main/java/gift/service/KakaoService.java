package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.other.KakaoLoginResponse;
import gift.domain.other.Member;
import gift.domain.other.WishList;
import gift.domain.other.getTokenDto;

import gift.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.LinkedList;
import java.util.Map;

@Service
public class KakaoService {
    @Value("${my.client_id}")
    private String client_id;

    private MemberRepository memberRepository;

    public KakaoService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public URI makeUri() {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", client_id)
                .queryParam("redirect_uri", "http://localhost:8080/api/kakao/code")
                .queryParam("response_type", "code")
                .encode()
                .build()
                .toUri();
        return uri;
    }

    public Member getToken(String code) throws JsonProcessingException {
        var url = "https://kauth.kakao.com/oauth/token";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        var dto = new getTokenDto("authorization_code",client_id,"http://localhost:8080/api/kakao/code",code);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        Map<String, String> map = objectMapper.convertValue(dto, new TypeReference<Map<String, String>>() {}); // (3)
        body.setAll(map);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<KakaoLoginResponse> response = restTemplate.exchange(request, KakaoLoginResponse.class);

        headers = new HttpHeaders();
        headers.add("Authorization",response.getBody().access_token());
        return getUserInformation(response.getBody().access_token());
    }

    public Member getUserInformation(String token){
        var url = "https://kapi.kakao.com/v2/user/me";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Authorization","Bearer " + token);

        var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = response.getBody();
        try{
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
            Map<String, Object> properties = (Map<String, Object>) responseMap.get("properties");
            Map<String, Object> kakao_account = (Map<String,Object>)responseMap.get("kakao_account");
            String nickname = (String) properties.get("nickname");
            String email =  (String)kakao_account.get("email");
            return saveMember(email,nickname);
        }
        catch (Exception e){
        }
        return null;
    }

    public Member saveMember(String email,String name){
        Member member = new Member(email,name,null,new LinkedList<WishList>());
        return memberRepository.save(member);
    }

}
