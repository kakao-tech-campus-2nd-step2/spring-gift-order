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