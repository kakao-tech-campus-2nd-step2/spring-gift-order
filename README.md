# spring-gift-order
- [1단계 - 카카오 로그인](#1단계---카카오-로그인)
    - [기능 요구 사항 정리](#기능-요구-사항-정리)
    - [구현 기능 목록](#구현-기능-목록)


---
# 1단계 - 카카오 로그인

## 기능 요구 사항 정리
카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

- 카카오계정 로그인을 통해 인증 코드를 받는다.
- 토큰 받기를 읽고 액세스 토큰을 추출한다.
- 앱 키, 인가 코드가 절대 유출되지 않도록 한다.
- (선택) 인가 코드를 받는 방법이 불편한 경우 카카오 로그인 화면을 구현한다.

실제 카카오 로그인은 아래 그림과 같이 진행된다.

![img.png](src/main/resources/static/images/img.png)
하지만 지금과 같이 클라이언트가 없는 상황에서는 아래와 같은 방법으로 인가 코드를 획득한다.

1. 내 애플리케이션 > 앱 설정 > 앱 키로 이동하여 REST API 키를 복사한다.
2. https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri=http://localhost:8080&client_id={REST_API_KEY} 에 접속하여 카카오톡 메시지 전송에 동의한다.
3. http://localhost:8080/?code={AUTHORIZATION_CODE} 에서 인가 코드를 추출한다.

## 구현 기능 목록

- 카카오 로그인 API 활용
  - 인가 코드 발급
  - 토큰 발급
  - 사용자 로그인 처리

---