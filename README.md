# spring-gift-order
- [1단계 - 카카오 로그인](#1단계---카카오-로그인)
    - [기능 요구 사항 정리](#기능-요구-사항-정리)
    - [구현 기능 목록](#구현-기능-목록)


- [2단계 - 주문하기](#2단계---주문하기)
  - [기능 요구 사항 정리](#기능-요구-사항-정리-1)
  - [구현 기능 목록](#구현-기능-목록-1)

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
# 2단계 - 주문하기

## 기능 요구 사항 정리
카카오톡 메시지 API를 사용하여 주문하기 기능을 구현한다.
- 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
- 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
- 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
- [나에게 보내기](https://developers.kakao.com/docs/latest/ko/message/rest-api#default-template-msg-me)를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
  - 메시지는 메시지 템플릿의 기본 템플릿이나 사용자 정의 템플릿을 사용하여 자유롭게 작성한다.

아래 예시와 같이 HTTP 메시지를 주고받도록 구현한다.

### Request

```
POST /api/orders HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json

{
    "optionId": 1,
    "quantity": 2,
    "message": "Please handle this order with care."
}
```

### Response
```
HTTP/1.1 201 Created
Content-Type: application/json

{
    "id": 1,
    "optionId": 1,
    "quantity": 2,
    "orderDateTime": "2024-07-21T10:00:00",
    "message": "Please handle this order with care."
}
```


## 구현 기능 목록

- Order 엔티티 및 API 추가
- 카카오 메세지 API 활용
  - 나에게 보내기
---