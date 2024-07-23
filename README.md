# spring-gift-order

# 기능 요구 사항

- 카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

1. 카카오계정 로그인을 통해 인증 코드를 받는다.
2. [토큰 받기](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token)를 읽고 액세스
   토큰을 추출한다.
3. 앱 키, 인가 코드가 절대 유출되지 않도록 한다.
4. (선택) 인가 코드를 받는 방법이 불편한 경우 카카오 로그인 화면을 구현한다.

- 실제 카카오 로그인은 아래 그림과 같이 진행된다.
- 하지만 지금과 같이 클라이언트가 없는 상황에서는 아래와 같은 방법으로 인가 코드를 획득한다.

1. 내 애플리케이션 > 앱 설정 > 앱 키로 이동하여 REST API 키를 복사한다.
2. https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri=http://localhost:8080&client_id={REST_API_KEY}에
   접속하여 카카오톡 메시지 전송에 동의한다.
3. http://localhost:8080/?code={AUTHORIZATION_CODE}에서 인가 코드를 추출한다.

# 힌트

## 애플리케이션 등록

- 카카오 플랫폼 서비스를 이용하여 개발한 애플리케이션은 이름, 아이콘, 상품명, 서비스명, 회사명, 로고, 심벌 등에 카카오의 상표를 사용할 수 없으므로(제17조(상표 사용 시
  의무 사항)), 아래와 같이 간단하게 작성해도 된다.
    - 앱 이름: spring-gift
    - 회사명: 본인의 이름
    - 카테고리: 교육

## 토큰 요청

- [토큰 받기](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token)에 따르면 아래와 같이
  RequestEntity를 만들 수 있다.

```
var url = "https://kauth.kakao.com/oauth/token";
var headers = new HttpHeaders();
headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
var body = new LinkedMultiValueMap<String, String>();
body.add("grant_type", "authorization_code");
body.add("client_id", properties.clientId());
body.add("redirect_uri", properties.redirectUri());
body.add("code", authorizationCode);
var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
```

## 오류 처리

- [레퍼런스](https://developers.kakao.com/docs/latest/ko/rest-api/reference)
  와 [문제 해결](https://developers.kakao.com/docs/latest/ko/kakaologin/trouble-shooting)을 참고하여 발생할 수 있는
  다양한 오류를 처리한다.
- 예를 들어, 아래의 경우 카카오톡 메시지 전송에 동의하지 않았으므로 과제 진행 요구 사항의 안내에 따라 설정한다.
  `403 Forbidden: "{"msg":"[spring-gift] App disabled [talk_message] scopes for [TALK_MEMO_DEFAULT_SEND] API on developers.kakao.com. Enable it first.","code":-3}"`
- RestTemplate을 사용하는
  경우 [Spring RestTemplate Error Handling](https://www.baeldung.com/spring-rest-template-error-handling)
  를 참고한다.

## HTTP Client

- [REST Clients](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html)
- 사용할 클라이언트를 선택할 때 어떤 기준을 사용해야 할까?
- 클라이언트 인스턴스를 효과적으로 생성/관리하는 방법은 무엇인가?

## 더 적절한 테스트 방법이 있을까?

- 요청을 보내고 응답을 파싱하는 부분만 테스트하려면 어떻게 해야 할까?
- 비즈니스 로직에 연결할 때 단위/통합 테스트는 어떻게 할까?

## API 호출에 문제가 발생하면 어떻게 할까?

- 응답 시간이 길면 어떻게 할까? 몇 초가 적당할까?
- 오류 코드는 어떻게 처리해야 할까?
- 응답 값을 파싱할 때 문제가 발생하면 어떻게 할까?

# 기능 구현 목록

1. 카카오계정 로그인을 위한 로직을 구현한다.
    1. 사용자는 `/api/oauth/kakao/login`로 접속해서 kakao 로그인을 진행한다.
    2. `/api/oauth/kakao/token`경로로 인증 코드를 받아, 토큰을 발급하고 사용자에게 반환한다.

    - clientId와 같은 정보는 .env를 사용하여 숨긴다.