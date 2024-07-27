# spring-gift-giftOrder

### 과제 진행 요구 사항

- 미션은 [주문하기](https://github.com/kakao-tech-campus-2nd-step2/spring-gift-giftOrder) 저장소를 포크하고 클론하는 것으로 시작한다.
- [온라인 코드 리뷰 요청 1단계 문서](https://github.com/next-step/nextstep-docs/blob/master/codereview/review-step1.md)를 참고하여 실습 환경을
  구축한다.
- 기능을 구현하기 전 README.md에 구현할 기능 목록을 정리해 추가한다.
- Git의 커밋 단위는 앞 단계에서 README.md에 정리한 기능 목록 단위로
  추가한다. [AngularJS Git Commit Message Convention](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)을 참고해 커밋
  메시지를 작성한다.

### 프로그래밍 요구 사항

- 자바 코드 컨벤션을 지키면서 프로그래밍 한다. (들여쓰기는 '4 spaces' 로 한다)
- indent (들여쓰기) depth 를 3이 넘지 않도록 구현한다.
- 3항 연산자를 사용하지 않는다.
- 함수는 한가지 일만 하도록 최대한 작게 만든다.
- 함수의 길이가 15 라인을 넘어가지 않도록 구현한다.
- JUnit 5 와 AssertJ 를 이용하여 정리한 기능 목록이 정상적으로 작동하는지 테스트 코드로 확인한다.
- else 예약어를 사용하지 않는다.
- 도메인 로직에 단위 테스트를 구현해야 한다.(핵심 로직을 구현하는 코드와 UI를 담당하는 로직을 분리해 구현한다.)

### 기능 요구 사항 (5주차)

#### 0단계

- [X] 상품 고도화 코드를 옮겨온다.
- [X] 4주차 피드백 반영한다.

#### 1단계

- [X] 카카오계정 로그인을 통해 인증코드를 받는다.
- [X] 토큰 받기를 읽고 AccessToken 을 추출한다.
- [X] 앱 키, 인가 코드가 유출되지 않도록 한다.
- 가입 및 로그인 요청 URL : http://localhost:8080/api/kakao/get-oauth (JWT 불필요)
- 토큰 요청 URL : http://localhost:8080/api/kakao/get-token (JWT 필요)
- 토큰 갱신 요청 URL : http://localhost:8080/api/kakao/token/refresh (JWT 필요)

#### 2단계

- [X] 카카오의 AccessToken, RefreshToken 을 서버에서 적절히 관리한다.
- [X] 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
- [X] 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
- [X] 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
- [X] 주문 내역을 카카오톡 메시지로 전송한다.
- [X] Mockito 를 사용하여 카카오 로그인 테스트를 진행한다.

### 나만의 HTTP RULE

| HTTP Method | 사용상황                           | 반환(상태코드) |
|-------------|--------------------------------|----------|
| GET         | 리소스의 조회                        | 200      | 
| POST        | 새로운 리소스 생성                     | 201      |
| PUT         | 리소스의 전체 업데이트 또는 ID를 통한 리소스 생성  | 204      |
| PATCH       | 리소스의 일부분(일부 필드) 업데이트           | 204      |
| DELETE      | 리소스의 삭제                        | 204      |

### 나만의 계층 RULE

| 계층         | 역할                                                          |
|------------|-------------------------------------------------------------|
| Controller | HTTP 요청을 받아 적절한 Service 호출, 입력 검증, 유효성 검사, HTTP 응답 생성 및 반환  |
| Service    | 비즈니스 로직 수행, DTO 와 엔티티 변환, 다수 Repository 를 통한 하나의 트랜잭션 처리 작업 |
| Model      | Entity, DTO 가 속하며 데이터구조, 데이터베이스와의 연동되는 객체                   |
| Repository | DB 관련 CRUD 작업, DB 의 결과를 Entity 로 변환하는 작업                    |

### 나만의 개행 RULE

- 지역변수는 사이에 개행을 두지 않는다. 하지만 첫 지역변수 전줄, 마지막 지역변수 다음줄에 개행을 추가한다.
- 생성자 전후에 개행을 추가한다.
- 추상체, 구현체 모두 메서드 전후에 개행을 추가한다. 단 마지막 메서드 후에는 추가하지 않는다.
- 클래스의 마지막 줄에는 개행을 추가한다.

### 연관관계 매핑 RULE

- M:1 관계에서는 M 에서 1 에 대한 정보까지 추가한다. ex) setProduct() 는 Product 가 아닌 WishProduct 에서 수행을 하는것 처럼
- save 를 하는 과정에서 우선 객체를 생성하고 연관관계를 맺어준 후에 repository.save() 를 호출한다.

### 계층간 의존 RULE

- M:1 관계에서 M 에서는 1에 대한 조회만을 수행하기에 서비스 계층에서는 레포지토리 계층을 의존한다.(R)
- M:1 관계에서 1 에서는 M에 대한 로직을 수행할 수 있기에(삭제 등) 서비스 계층을 의존한다.(CUD)
