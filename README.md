# spring-gift-order
## step1
### 기능 요구 사항
카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

- 카카오계정 로그인을 통해 인증 코드를 받는다.
- 토큰 받기를 읽고 액세스 토큰을 추출한다.
- 앱 키, 인가 코드가 절대 유출되지 않도록 한다.
- (선택) 인가 코드를 받는 방법이 불편한 경우 카카오 로그인 화면을 구현한다.

지금과 같이 클라이언트가 없는 상황에서는 아래와 같은 방법으로 인가 코드를 획득한다.

1. 내 애플리케이션 > 앱 설정 > 앱 키로 이동하여 REST API 키를 복사한다.
2. https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri=http://localhost:8080&client_id={REST_API_KEY}에 접속하여 카카오톡 메시지 전송에 동의한다.
3. http://localhost:8080/?code={AUTHORIZATION_CODE}에서 인가 코드를 추출한다.

### 구현 기능 목록
kakao oauth
- [X] kakao 간편 로그인 사용자 table 만들기
- [X] kakao oauth를 위한 controller 작성
- [X] kakao oauth 처리 service 작성

웹페이지
- [X] 메인 로그인 페이지
- [X] kakao 간편 회원가입 페이지

## step2
### 기능 요구 사항
카카오톡 메시지 API를 사용하여 주문하기 기능을 구현한다.
- 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
- 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
- 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
- 나에게 보내기를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
  - 메시지는 메시지 템플릿의 기본 템플릿이나 사용자 정의 템플릿을 사용하여 자유롭게 작성한다.

### 구현 기능 목록
Order
- [X] Order Entity
- [X] Order Repository
- [X] Order Service
  - [X] Option 수량 확인하고 Order Request 수량 만큼 감소하기 (OptionService 이용)
  - [X] Order 저장하기
  - [X] Order에 해당하는 Wish 삭제하기 (WishService 이용)
- [X] Order Controller
  - [X] Order 전달하기

메시지 전송
- [X] KakaoMemberService
  - [X] 토큰 주인에게 구매 확인 메시지 전송하기

## step3
### 기능 요구 사항
어떻게 하면 API 사양에 관해 클라이언트와 편하게 소통할 수 있을지 고민해 보고 그 방법을 구현한다.

### 구현 기능 목록
- [X] Swagger로 api 문서 생성하기