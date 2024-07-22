# spring-gift-order
---
## 1단계 요구사항
### LoginController
- @GetMapping(/login)
- @PostMapping(/token)

1. 인가코드 요청
  Request:
    GET  https://kauth.kakao.com/oauth/authorize, {client_id}, {redirect_uri}, {response_type}
  Response:
    GET  {redirect_url}, 성공시: {code}, 실패시: {error}, {error_description}
3. 토큰 요청
  Request
    POST	https://kauth.kakao.com/oauth/token,
    Header  content-type	Content-type: application/x-www-form-urlencoded;charset=utf-8
    Body  {grant_type}, {client_id}, {redirect_uri}, {code}
  Response
    Body {token_type}, {access_token}, {expires_in}, {refresh_token}, {refresh_token_expires_in}
           
