# spring-gift-order

## step1

### 기능 목록
1. 카카오 애플리케이션 등록 및 설정
2. 카카오 로그인 URL로 리디렉션
3. 인가 코드 획득 및 리디렉션
4. 인가 코드를 사용해 액세스 토큰 요청
5. 액세스 토큰을 사용해 카카오 API 호출 준비

## step2

### 기능 목록
1. api/orders 관련해서 Controller 구현
2. 카카오 메시지 (나에게 보내기) 구현
3. Member DB 수정(Access Token 관련)
4. Access Token DB에 저장해서 관리할 수 있도록 DB Schema 구현
5. Access Token Session에서 관리할 수 있도록 구현
6. Access Token 잘 동작되는 지 간단한 페이지 만들어서 확인
7. 상품 옵션의 수량이 감소
8. 위시리스트에서 해당 상품이 존재하면 삭제