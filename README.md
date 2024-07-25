# spring-gift-order


## step0 구현 기능

- 4주차 코드 클론
- PathVariable은 카멜 케이스로 디자인한다. 
- 생성자가 아닌 함수는 클래스명과 다륵 짓는걸 지향한다.
- @Profile("dev")을 적용해서 테스트 케이스 데이터가 개발 단계에서만 사용되게 한다.
- 생성자 주입 형태로 변경

## step1 구현 기능

- 카카오 로그인을 통해 인가 코드를 받고 선물하기 서비스 로그인
    - 기존 회원가입된 유저가 추가적으로 카카오 인증을 하면 사용할 수 있음
    - 카카오 인증으로 회원가입은 불가
      - 유저생성을 위한 데이터(이메일)을 받을 수 없음
      - 해당 데이터를 받기 위해서는 사업자번호 필요
    - 코드 발급
      - 프론트에서 url로 요청
    - 토큰 발급
      - https://kauth.kakao.com/oauth/authorize로 로그인
      - redirect_uri로 code를 받아서 토큰 발급하는 카카오api로 토큰 생성
      - client_id는 비공개
    - 소셜 로그인 등록
      - 토큰을 검증해 가져온 id를 서버에 저장
    - 소셜 로그인 인증
      - 토큰을 검증해 가져온 id와 서버내 저장된 id를 대조해서 인증
      - 자체 jwt토큰 발급 및 반환

## step2 구현 기능

- 주문할때 수령인에게 보낼 메세지를 작성
  - 상품 옵션과 해당 수량을 선택해서 주문하면 재고가 차감된다.
  - 해당 상품이 위시 리스트에 있는경우 위시 리스트에서 삭제됨
  - 주문내역을 카카오톡 메시지로 전송한다.
    - 메세지 템플릿은 자유

## step3 구현 기능

- /api/social/code/kakao로 카카오 토큰을 받게금 수정함
  - @RestController가 아닌 뷰를 반환해야 해서 @Controller로 구현
  - 로그인폼 반환
  - 로그인 및 동의시 /api/social/token/kakao로 리다이렉트
- api 사양 문서화
  - swagger3 사용
  - http://localhost:8080/swagger-ui/index.html