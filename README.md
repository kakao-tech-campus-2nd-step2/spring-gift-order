# spring-gift-order

## STEP1
- 카카오계정 로그인을 통해 인가 코드를 받는다.
  - `GET` `/oauth2/authorization/kakao`
    - 사용자가 카카오 로그인시 사용할 API
    - 302 응답으로 사용자를 https://kauth.kakao.com/oauth/authorize 로 redirect 시킨다
  - `GET` `login/oauth2/code/kakao`
    - 인증 성공시 카카오 인증 서버에 의해 사용자가 redirect 될 API
    - 인가 코드를 받아 실제 로그인 처리를 담당한다.
- 인가 코드로 카카오 인증 서버로부터 토큰을 발급받는다.
  - 아직 이 토큰은 최초 사용자 정보 조회시에만 사용되므로 db에 별도로 저장하지 않는다.
- 발급받은 토큰으로 로그인, 회원가입 처리를 수행한다.
  - 발급받은 토큰으로 카카오 인증서버 사용자 정보를 조회한다.
  - 가입하지 않은 사용자라면 조회한 닉네임 정보를 이용해 회원 정보를 등록한다. 
    - 이 때 email과 비밀번호는 무작위 UUID값을 생성해 저장한다.
  - 이후 refresh 토큰을 쿠키로 설정하여 사용자에게 반환한다.
- refresh 토큰 발급 기능을 구현한다.
  - 사용자 로그인시 12시간 기한의 refresh 토큰을 발급한다.
  - refresh 토큰에는 만료 기한 정보만 포함한다.
  - db에 이 refresh 토큰을 저장하여 관리한다.
    - refresh 토큰은 USER와 1대1 단방향 연관관계를 갖는다
- access 토큰 재발급 기능을 구현한다.
  - refresh 토큰을 입력받아 유효성을 검증한다
  - db에 저장해둔 refresh 토큰 정보로 새로운 access 토큰을 발급한다.
- 위 기능들을 구현할 때 spring security의 기능은 기존 코드를 수정하는 수준에서 최소한으로 사용한다.

