# spring-gift-order

## Step1 - 카카오 로그인
### 기능 요구 사항
카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.
1. 인가 코드 받기
2. 토큰 받기
3. 사용자 로그인 처리
4. 발생할 수 있는 오류 처리

## Step2 - 주문하기
### 기능 요구 사항
카카오톡 메시지 API를 사용하여 주문하기 기능을 구현한다.

- 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
- 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
- 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
- 주문 내역을 카카오톡 메시지로 전송한다.
    - 메시지 템플릿은 자유

Request
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

Response
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

## Step3 - API 문서 만들기
### 기능 요구 사항
- API 사양에 관해 클라이언트와 어떻게 소통할 수 있을까? 어떻게 하면 편하게 소통할 수 있을지 고민해 보고 그 방법을 구현한다.

### 기능 구현 사항
- swagger 사용하여 API 문서 작성