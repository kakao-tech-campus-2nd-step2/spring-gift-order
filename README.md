# spring-gift-ordergit

# 3단계 기능목록 

## API 문서 만들기 
* API 사양에 관해 클라이언트와 어떻게 소통할 수 있을까? 어떻게 하면 편하게 소통할 수 있을지 고민해 보고 그 방법을 구현한다.

### 시작전 할일 
- [ ] 2단계 : 주문내역 카카오톡 메세지 전송
- [x] 코드리뷰 반영 
  - [x] Order 네이밍 변경 ( DB예약어임 ) -> GiftOrder 로 변경 
  - [x] ExternalAPIService 에서 URL 을 상수로 수정

### 3단계 기능구현
- [ ] Swagger-editor를 통해 api를 문서화하고 명세
  - [x] Swagger 환경설정 
  - [ ] Swagger Tag 작성 

# 2단계 기능목록 

### 시작전 할일
- [x] gradle로 test build 안되는 문제 해결
- [x] 1단계 코드에서 test 말고 클래스코드 에서 
  - [x] 인가받는 기능 구현 
  - [x] 토큰 주고받기 기능 구현 
- [x] 테스트 오류 수정 

### 2단계 기능구현 
* 카카오톡 메시지 API를 사용하여 주문하기 기능을 구현한다.

- [x] 주문할 때 수령인에게 보낼 메시지를 작성할 수 있는 기능 구현
- [x] 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감되는기능 구현 
- [x] 주문 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제하는 기능 구현 
- [ ] 나에게 보내기를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
  - [ ] 메시지는 메시지 템플릿의 기본 템플릿, 사용자 정의 템플릿을 사용하여 자유롭게 작성가능


* 참고 * 
아래 예시와 같이 HTTP 메시지를 주고받도록 구현
- [x] 토큰으로 누가 주문했는지 찾아야함... 
Request
POST /api/orders HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json

{
"optionId": 1,
"quantity": 2,
"message": "Please handle this giftOrder with care."
}

Response
HTTP/1.1 201 Created
Content-Type: application/json

{
"id": 1,
"optionId": 1,
"quantity": 2,
"orderDateTime": "2024-07-21T10:00:00",
"message": "Please handle this giftOrder with care."
}

### 준비사항
- [x] 카카오 API를 사용하기 위한 애플리케이션을 등록
- [x] 내 애플리케이션 > 제품 설정 > 카카오 로그인 > 활성화 설정 ON (카카오 로그인 활성화 설정)
- [x] 내 애플리케이션 > 제품 설정 > 카카오 로그인 > Redirect URI 등록 > http://localhost:8080 저장 (Redirect URI 등록)
- [x] 내 애플리케이션 > 제품 설정 > 카카오 로그인 > 동의항목 > 접근권한 > 카카오톡 메시지 전송 > 선택 동의 (접근권한 동의항목)

# 1단계 기능목록

* 카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.
- [ ] 인가 코드 획득 
  * 클라이언트가 없는 상황에서는 아래와 같은 방법으로 인가 코드를 획득한다.
      - [x] 내 애플리케이션 > 앱 설정 > 앱 키로 이동하여 REST API 키를 복사한다.
      - [x] https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri=http://localhost:8080&client_id={REST_API_KEY} 에 접속하여 카카오톡 메시지 전송에 동의한다.
      - [x] http://localhost:8080/?code={AUTHORIZATION_CODE} 에서 인가 코드를 추출한다.
- [x] 카카오계정 로그인을 통해 인증 코드를 받는다. 
- [x] 토큰 받기를 읽고 액세스 토큰을 추출한다.
- [x] 앱 키, 인가 코드가 절대 유출되지 않도록 한다. -> gitignore에 올리기
- [ ] (선택) 인가 코드를 받는 방법이 불편한 경우 카카오 로그인 화면을 구현한다

# 힌트

* 애플리케이션 등록
카카오 플랫폼 서비스를 이용하여 개발한 애플리케이션은 이름, 아이콘, 상품명, 서비스명, 회사명, 로고, 심벌 등에 카카오의 상표를 사용할 수 없으므로(제17조(상표 사용 시 의무 사항)), 아래와 같이 간단하게 작성해도 된다.

* 앱 이름: spring-gift
회사명: 본인의 이름
카테고리: 교육
토큰 요청
토큰 받기에 따르면 아래와 같이 RequestEntity를 만들 수 있다.
`
var url = "https://kauth.kakao.com/oauth/token";
var headers = new HttpHeaders();
headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
var body = new LinkedMultiValueMap<String, String>();
body.add("grant_type", "authorization_code");
body.add("client_id", properties.clientId());
body.add("redirect_uri", properties.redirectUri());
body.add("code", authorizationCode);
var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
`
* 오류 처리
레퍼런스와 문제 해결을 참고하여 발생할 수 있는 다양한 오류를 처리한다.
예를 들어, 아래의 경우 카카오톡 메시지 전송에 동의하지 않았으므로 과제 진행 요구 사항의 안내에 따라 설정한다.
`403 Forbidden: "{"msg":"[spring-gift] App disabled [talk_message] scopes for [TALK_MEMO_DEFAULT_SEND] API on developers.kakao.com. Enable it first.","code":-3}"
RestTemplate을 사용하는 경우 Spring RestTemplate Error Handling를 참고한다.
`
* HTTP Client
REST Clients
사용할 클라이언트를 선택할 때 어떤 기준을 사용해야 할까?
클라이언트 인스턴스를 효과적으로 생성/관리하는 방법은 무엇인가?

* 더 적절한 테스트 방법이 있을까?
요청을 보내고 응답을 파싱하는 부분만 테스트하려면 어떻게 해야 할까?
비즈니스 로직에 연결할 때 단위/통합 테스트는 어떻게 할까?
API 호출에 문제가 발생하면 어떻게 할까?
응답 시간이 길면 어떻게 할까? 몇 초가 적당할까?
오류 코드는 어떻게 처리해야 할까?
응답 값을 파싱할 때 문제가 발생하면 어떻게 할까?


