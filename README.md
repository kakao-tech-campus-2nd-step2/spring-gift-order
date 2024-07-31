# spring-gift-order

## 1단계 - 카카오 로그인

### 미션 요구 사항

카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

* 카카오계정 로그인을 통해 인증 코드를 받는다.
* 토큰 받기를 읽고 액세스 토큰을 추출한다.
* 앱 키, 인가 코드가 절대 유출되지 않도록 한다.
    * 특히 시크릿 키는 GitHub나 클라이언트 코드 등 외부에서 볼 수 있는 곳에 추가하지 않는다.
* (선택) 인가 코드를 받는 방법이 불편한 경우 카카오 로그인 화면을 구현한다.

---

## 2단계 - 주문하기

### 미션 요구 사항

카카오톡 메시지 API를 사용하여 주문하기 기능을 구현한다.

* 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
* 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
* 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
* 나에게 보내기를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
    * 메시지는 메시지 템플릿의 기본 템플릿이나 사용자 정의 템플릿을 사용하여 자유롭게 작성한다.

---

## 3단계 - API 문서 만들기

### 미션 요구 사항

API 사양에 관해 클라이언트와 어떻게 소통하기 위한 API 문서를 구현한다.

* API 정보를 확인할 수 있는 API 문서 페이지를 구현한다.

API 명세: https://fuchsia-tabletop-6fc.notion.site/22-438e773c1c0d475da1e87b4ef4ef42d8?pvs=74

---

## (선택) 4단계 - Circuit Breaker

### 미션 요구 사항

선물하기 서비스는 현재 여러 외부 API를 사용하고 있다. 이러한 API 중 하나가 갑자기 응답을 전혀 하지 않는다면 어떻게 될까?

* 연쇄적인 장애를 방지하기 위해 장애가 발생한 서비스에 대한 접속 차단을 구현한다.
* Circuit Breaker의 개념을 학습하고 적용한다.
