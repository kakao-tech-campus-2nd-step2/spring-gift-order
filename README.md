# spring-gift-order

---
<details>
<summary><strong>1단계 - 카카오 로그인</strong></summary>

- application-secret-key.properties에 추가
    - cliendt_id
    - redirect_uri
- .gitignore에 properties 추가
- KakaoProperties 생성
    - config 패키지에 생성
    - client_id
    - redirect_uri
- Application 수정
- KakaoLoginController 생성
    - 카카오 로그인 화면
    - 액세스 토큰 얻기
- KakaoAccessTokenDTO 생성
- KakaoService 생성
    - 인가 코드를 통해 액세스 토큰 받기
- 카카오 로그인 화면 만들기
    - 버튼 클릭 시 로그인 화면으로 이동
- 액세스 토큰 얻기 성공 화면 만들기
</details>

<details>
<summary><strong>2단계 - 주문하기</strong></summary>

- 액세스 토큰으로부터 정보 추출하기
  - KakaoAccountDTO 생성
  - KakaoUserInfoDTO 생성
  - KakaoService에 메서드 추가
    - KakaoProperties에서 url 관리하기 
    - 액세스 토큰으로부터 이메일 뽑아와서 멤버로 저장하기
    - JwtToken 생성하기
  - KakaoLoginController 수정
  - kakao_access_token.html 수정

- 주문하기
  - Order 엔티티 만들기
  - OrderDTO 만들기
    - OrderRequestDTO
      - 주문 버튼 눌렀을 떄 넘겨줄 정보
    - OrderResponseDTO
      - 카카오톡 메시지 보낼 때 필요한 정보
  - OrderRepository 만들기
  - OrderService 만들기
  - OrderController 만들기
    - order_form 이동 시 예시 데이터 넣기
    - Order가 생성되면 주문이 된 것이므로 수량 차감하기
  - option_list.html 수정
    - order_form으로 이동하는 버튼 만들기
  - order_form 생성

- 카카오톡 메시지 보내기
  - KakaoService에 메서드 추가
    - 카카오톡 메시지 보내는 메서드 추가
    - 메시지 템플릿 생성
  - OrderController 수정
    - 주문 버튼을 눌렀을 때 메시지 보내도록 수정
</details>

<details>
<summary><strong>3단계 - API 문서 만들기</strong></summary>

- Swagger 사용 위해 build.gradle에 추가
- OpenAPI 설정 파일 추가
- API 문서 만들기
  - 컨트롤러에 Tag와 Operation을 사용
</details>