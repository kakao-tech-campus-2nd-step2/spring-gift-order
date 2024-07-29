### 기능 요구 사항
카카오톡 메시지 API를 사용하여 주문하기 기능을 구현한다.

- 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
- 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
- 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
- 나에게 보내기를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
   - 메시지는 메시지 템플릿의 기본 템플릿이나 사용자 정의 템플릿을 사용하여 자유롭게 작성한다.

아래 예시와 같이 HTTP 메시지를 주고받도록 구현한다.


### Request
```http request
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
```http request
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
