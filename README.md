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