# spring-gift-order

# step1

## 구현할 기능 목록
- [x] 카카오 디벨로퍼에서 앱 등록
  - [x] 카카오 로그인 활성화
  - [x] Redirect URI 등록
  - [x] 카카오톡 메시지 전송 > 선택 동의
- [x] 카카오 디벨로퍼에서 앱 키 받기
- [x] 시크릿 키 설정
  - [x] Application 실행 시, application-secret.properties 추가
- [x] 인가 코드 받기
  - [x] 카카오톡 인가 코드 요청
  - [x] 리디렉션 URI에서 인가 코드 받는 Controller 작성
  - [x] 토큰을 받기 위한 Service code 작성
- [x] 토큰 받기
- [x] 발생할 수 있는 오류들 처리하기
  - [x] 카카오톡 메시지 전송 비동의한 경우 오류 처리하기
  - [x] RestTemplate 오류 처리하기

# step2

## 구현할 기능 목록
- [ ] 상품 옵션과 해당 수량을 선택하여 주문한다
  - [x] Order Entity 생성
    - [x] id(PK), user_id(FK), product_id(FK), Option_id, ordered_at, message, count
  - [ ] Order Controller에서 주문하기 만들기
    - [ ] 해당 상품 옵션의 수량이 차감된다
    - [ ] 해당 상품이 위시 리스트에 있는 경우 위시에서 삭제한다.
    - [ ] 위의 두 기능은 미리 구현되어 있으니, 주문하는 api 구현 할 때 사용한다.

- [ ] 수령인에게 보낼 메시지를 작성할 수 있다
  - [ ] 나에게 보내기로 주문 내역을 카카오톡 메시지로 전송한다.
  - [ ] "피드" 메시지를 사용하여 영수증처럼 주문 내역을 보낸다.
  
- [ ] 예외 처리 확인
  - [ ] 남은 옵션의 수량보다 많이 주문 할 경우 오류메시지가 뜨는 지 확인