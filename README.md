# spring-gift-order

## step1

### 기능 요구 사항
카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

- 카카오계정 로그인을 통해 인증 코드를 받는다.
- 토큰 받기를 읽고 액세스 토큰을 추출한다.
- 앱  키, 인가 코드가 절대 유출되지 않도록 한다.
  - 특히 시크릿 키는 GitHub나 클라이언트 코드 등 외부에서 볼 수 있는 곳에 추가하지 않는다.
- (선택) 인가 코드를 받는 방법이 불편한 경우 카카오 로그인 화면을 구현한다.

### 구현 기능 목록
- 카카오 로그인 페이지 작성
- 카카오 로그인 콜백 처리
- 카카오 로그인 서비스 코드 작성
- 테스트 코드 작성

## step2

### 기능 요구 사항
카카오톡 메시지 API를 사용하여 주문하기 기능을 구현한다.

- 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
- 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
- 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
- 나에게 보내기를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
  - 메시지는 메시지 템플릿의 기본 템플릿이나 사용자 정의 템플릿을 사용하여 자유롭게 작성한다.

### API 명세

Request

POST /api/orders HTTP/1.1

Authorization: Bearer {token}

Content-Type: application/json


{

    "optionId": 1,
    "quantity": 2,
    "message": "Please handle this order with care."
    
}

Response

HTTP/1.1 201 Created

Content-Type: application/json

{

    "id": 1,
    "optionId": 1,
    "quantity": 2,
    "orderDateTime": "2024-07-21T10:00:00",
    "message": "Please handle this order with care."
    
}

### 구현 기능 목록

- 카카오 API 설정
- 주문 API 구현
  - 주문 시 주문 내역 카카오톡 메시지로 전송
  - 상품 옵션 선택 및 수량 차감
  - 위시 리스트에서 해당 상품 삭제
- 주문 폼 작성
- 응답 및 예외 처리
- 테스트 코드 작성

## step3

### 기능 요구 사항
API 사양에 관해 클라이언트와 어떻게 소통할 수 있을까? 어떻게 하면 편하게 소통할 수 있을지 고민해 보고 그 방법을 구현한다.

### 구현 기능 목록
- Swagger 사용하여 API 작성하기

