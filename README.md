# spring-gift-order

<br/>

# 1단계 - 카카오 로그인

## 요구 사항 정의

카카오 로그인을 통해 받은 인가 코드로 토큰 발급하기

앱 키, 인가 코드, 시크릿 키가 유출되지 않도록 함

### 인가 코드 받기
- Request
    ```
    GET https://kauth.kakao.com/oauth/authorize
        ?response_type=code
        &client_id=${REST_API_KEY}
        &redirect_uri=${REDIRECT_URI}
    ```
- Response
    ```
    HTTP/1.1 302 Found
    Content-Length: 0
    Location: ${REDIRECT_URI}?code=${AUTHORIZE_CODE}
    ```

### 토큰 발급하기
- Request
    ```
    curl -v -X POST "https://kauth.kakao.com/oauth/token" \
      -H "Content-Type: application/x-www-form-urlencoded" \
      -d "grant_type=authorization_code" \
      -d "client_id=${REST_API_KEY}" \
      --data-urlencode "redirect_uri=${REDIRECT_URI}" \
      -d "code=${AUTHORIZE_CODE}"
    ```
- Response
    ```
    HTTP/1.1 200 OK
    Content-Type: application/json;charset=UTF-8
    {
        "token_type":"bearer",
        "access_token":"${ACCESS_TOKEN}",
        "expires_in":43199,
        "refresh_token":"${REFRESH_TOKEN}",
        "refresh_token_expires_in":5184000,
        "scope":"account_email profile"
    }
    ```

### 사용자 정보 가져오기
- Request
    ```
    curl -v -G GET "https://kapi.kakao.com/v2/user/me"
      -H "Authorization: Bearer ${ACCESS_TOKEN}"
    ```
- Response
    ```
    HTTP/1.1 200 OK
    {
        "id":123456789,
        "connected_at": "2022-04-11T01:45:28Z",
        "kakao_account": {
            "email": "1234@kakao.com"
        }
    },  
    "properties": {
        "${CUSTOM_PROPERTY_KEY}": "${CUSTOM_PROPERTY_VALUE}",
        ...
    }
    ```

<br/>
<br/>

# 2단계 - 주문하기

## 요구 사항 정의

카카오톡 메시지 API를 사용하여 주문하기 기능 구현

- 주문할 때 수령인에게 보낼 메시지를 작성할 수 있음
- 상품의 옵션과 수량을 선택하여 주문하면 해당 상품 옵션의 수량 차감
- 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제
- 주문 내역을 카카오톡 메시지(나에게 보내기)로 전송
  - 기본 템플릿이나 사용자 정의 템플릿을 사용

### 주문하기
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

<br/>
<br/>

# 3단계 - API 문서 만들기

## 요구 사항 정의

API 사양에 관해 편히 소통할 수 있는 방법 고민해 보고 구현하기

Swagger로 API 명세서 작성