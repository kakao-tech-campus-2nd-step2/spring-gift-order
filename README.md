# spring-gift-order

## STEP-2 기능 구현 사항
- 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
- 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
- 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
- 나에게 보내기를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
- 메시지는 메시지 템플릿의 기본 템플릿이나 사용자 정의 템플릿을 사용하여 자유롭게 작성한다.
- 아래 예시와 같이 HTTP 메시지를 주고받도록 구현한다.

- Request
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


- Response
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

- 실제 카카오톡 메시지는 아래와 같이 전송된다.

1. User -> Application : 메세지 보내기 클릭
2. Application -> Kakao Server : 카카오톡 친구 가져오기 API 호출
3. Kakao Server -> Application : 카카오톡 친구 데이터 제공
4. Applicaton -> User : 서비스의 친구 목록
5. User -> Application : 선택한 친구에게 메세지 보내기 요청
6. Application -> Kakao Server : 카카오톡 메세지 API 호출

- 하지만 이번 미션에서는 수신자가 본인이기 때문에 카카오톡 친구 목록 가져오기는 생략한다.

## STEP-2 구현 해야 될 목록
- OrderEntity를 정의
  - Long id 
  - Long optionId
  - Long quantity
  - String message
  - LocalDateTime orderDateTime
- 해당 도메인에 대한 Controller, Service, Repository 구현
  - OrderController
    - POST addOrder()
    - GET getAllOrder()
  - OrderService
    - addOrder()
    - getAllOrder()
  - OrderRepository
  - KakaoService
    - createMessage()
    - sendMessage()
  - OptionService
    - Order의 quantity 만큼 quantity 차감

## STEP-3 기능 구현 사항
- API 사양에 관해 클라이언트와 어떻게 소통할 수 있을까? 어떻게 하면 편하게 소통할 수 있을지 고민해 보고 그 방법을 구현한다.
- Swagger를 활용한 API문서 만들기
  - build.gradle에 해당 의존성 추가
  - SwaggerConfig를 통한 설정
  - Annotation들을 통한 api 문서 작성

