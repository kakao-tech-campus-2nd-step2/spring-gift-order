# spring-gift-order

# 기능 요구 사항

- 카카오톡 메시지 API를 사용하여 주문하기 기능을 구현한다.

1. 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
2. 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
3. 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
4. 나에게 보내기를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
5. 메시지는 메시지 템플릿의 기본 템플릿이나 사용자 정의 템플릿을 사용하여 자유롭게 작성한다.

- 아래 예시와 같이 HTTP 메시지를 주고받도록 구현한다.
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

# 기능 구현 목록

## 유저 등록

- 사용자가 OAuth를 통해 로그인한 경우 해당 유저를 DB에 등록한다.
    - 유저의 이메일은 id 토큰의 이메일 값을 사용한다.
    - 유저의 패스워드는 id 토큰의 sub 값을 사용한다.
- 이미 존재한다면, 위 과정을 생략한다.

### 구현

1. 사용자가 OAuth를 통해 로그인한다.
2. 서버는 카카오톡 auth서버로 부터 토큰을 받는다.
    - [카카오톡 토큰 받기](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token)
3. id 토큰의 email값을 통해 DB에 해당 유저가 존재하는지 확인한다.
4. 존재하지 않는다면 email값과 sub값을 참조해 Member를 생성한다.
5. 사용자에게 access token을 반환한다.

## 주문

- 사용자는 다음 요청을 통해 주문을 할 수 있다.

### 기본정보

| 메서드  |               URL                | 인증 방식  |
|:----:|:--------------------------------:|:------:|
| POST | http://localhost:8080/api/orders | 엑세스 토큰 |

### 요청

#### 해더

|      이름       |                   설명                   | 필수 |
|:-------------:|:--------------------------------------:|:--:|
| Content-type  |     Content-type: application/json     | O  |
| Authorization | Authorization : Bearer ${access_token} | O  |

#### 본문

|    이름    |   타입   |         설명          | 필수 |
|:--------:|:------:|:-------------------:|:--:|
| optionId |  long  | 구매하고자 하는 상품 옵션의 id값 | O  |
| quantity |  int   | 구매하고자 하는 상품 옵션의 수량  | O  |
| message  | String |  수령인에게 보내고자하는 메시지   | O  |

### 응답

#### HttpStatus

- 정상 : `201 Created`
- 엑세스 토큰이 검증 실패 : `403 Forbidden`
- 해당 옵션이 존재하지 않는 경우 : `400 Bad request`
- 구매하고자 하는 수량이 존재하는 수량보다 많은 경우 : `400 Bad request`

#### 본문

|      이름       |   타입   |        설명        | 필수 |
|:-------------:|:------:|:----------------:|:--:|
|      id       |  long  |   구매한 상품의 id값    | O  |
|   optionId    |  long  |  구매된 상품 옵션의 id값  | O  |
|   quantity    |  int   | 구매된 하는 상품 옵션의 수량 | O  |
| orderDateTime |  Time  |      구매한 시각      | O  | 
|    message    | String | 수령인에게 보내고자하는 메시지 | O  |

### 구현

1. 위 요청으로 입력이 들어오면 Access 토큰을 검증한다.
    - [카카오톡 토큰 정보 보기](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#get-token-info)
    - 응답으로 `200 OK`가 발생하지 않으면 `403 Forbidden`를 반환한다.

2. 상품 옵션과 해당 수량을 입력된 수만큼 차감한다.
3. 위시리스트에 해당 상품이 존재한다면 해당 위시리스트를 삭제한다.
4. 아래 경로를 통해 카카오톡 계정으로 메시지를 보낸다.
    - [카카오톡 나에게 메시지 보내기](https://developers.kakao.com/docs/latest/ko/message/rest-api#default-template-msg)
5. `201 Created`와 응답 본문의 내용을 사용자에게 반환한다.