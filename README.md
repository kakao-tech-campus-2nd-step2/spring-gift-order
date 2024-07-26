# spring-gift-order

## **🚀 Step1- 카카오 로그인**

---

카카오 로그인은 아래 그림과 같이 진행된다.
![Untitled](https://developers.kakao.com/docs/latest/ko/assets/style/images/kakaologin/kakaologin_sequence.png)

### 💻 기능 요구 사항

---
카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

- [X]  카카오계정 로그인을 통해 인증 코드를 받는다.
- [X]  엑세스 토큰 추출
- [X]  앱 키, 인가 코드 유출하지 않도록 구현
   - [X]  따로 properties 만들어서 깃허브 등 외부에서 볼 수 없도록 한다.

## **🚀 Step2 - 주문하기**

---

### 기능 요구 사항
카카오톡 메시지 API를 사용하여 주문하기 기능을 구현한다.
- [ ]  상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감
- [ ]  해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제
- [ ]  주문 내역을 카카오톡 메시지로 전송한다.

메시지는  [메시지 템플릿](https://developers.kakao.com/docs/latest/ko/message/message-template)의 기본 템플릿이나 사용자 정의 템플릿을 사용하여 자유롭게 작성한다.


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

실제 카카오톡 메시지는 아래와 같이 전송된다. 하지만 이번 미션에서는 수신자가 나이기 때문에 카카오톡 친구 목록 가져오기는 생략한다.

![Untitled](https://developers.kakao.com/docs/latest/ko/assets/style/images/message/message_talk.png)