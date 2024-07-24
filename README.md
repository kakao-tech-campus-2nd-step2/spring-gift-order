# spring-gift-order

## step 1 - 카카오 로그인

- **카카오 로그인 구현**
    - [ ] 기존 로그인은 일단 그대로 유지
    - [ ] http://localhost:8080/oauth/kakao 에 접속 시, 로그인 페이지로 리다이렉트
    - [ ] 해당 페이지에서 로그인 완료 시, 인가 코드를 http://localhost:8080/oauth/kakao/callback 으로 전달
    - [ ] 인가 코드 추출 후, 해당 코드를 통해 access token 발급

- **추가 기능**
    - [ ] 카카오 로그인 시, 유저 정보를 통해 서비스의 User에도 등록하여 로그인 처리 되도록 함