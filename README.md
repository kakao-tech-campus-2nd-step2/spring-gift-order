# spring-gift-order
---
## 1단계 요구사항
### LoginViewController
- @GetMapping("/login")
### kakao_login.html
- post "https://kauth.kakao.com/oauth/authorize"
### LoginController
@RequestParam(value = "code") String code
### LoginService
- getAccessToken(String code)

1. 인가코드 요청
  - Request:
    - GET  https://kauth.kakao.com/oauth/authorize, {client_id}, {redirect_uri}, {response_type}
  - Response:
    - GET  {redirect_url}, 성공시: {code}, 실패시: {error}, {error_description}
3. 토큰 요청
  - Request
    - POST	https://kauth.kakao.com/oauth/token,
    - Header  content-type	Content-type: application/x-www-form-urlencoded;charset=utf-8
    - Body  {grant_type}, {client_id}, {redirect_uri}, {code}
  - Response
    - Body {token_type}, {access_token}, {expires_in}, {refresh_token}, {refresh_token_expires_in}
---           
## 2단계 요구사항
### wishlist.html
- 옵션 선택버튼 추가
- 메시지 입력창 추가
- 주문 넣는 버튼 추가 -> api/orders
### OrderRequest
- optionId
- quantity
- message
### Order, OrderResponse
- id
- optionId
- quantity
- orderDateTime
- message
### OrderService
- OrderRequest -> Order -> save()
- DateTime구하기
- 카카오톡 메시지 api 전송
### OrderRepository
