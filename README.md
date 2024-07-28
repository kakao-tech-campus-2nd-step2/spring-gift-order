# spring-gift-order
---

# 1단계 - 카카오 로그인

## 기능 요구 사항

카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

* 카카오계정 로그인을 통해 인증 코드(code)를 받는다.
* 토큰 받기를 읽고 액세스 토큰(accessToken)을 추출한다.
  앱 키, 인가 코드가 절대 유출되지 않도록 한다. &rarr; application.properties
* (선택) 인가 코드를 받는 방법이 불편한 경우 카카오 로그인 화면을 구현한다. ✅

## 구현 사항

- [x] 프론트에서 인가 코드 받을 수 있도록 html, js 작성
- [x] 받은 인가 코드 백으로 fetch
- [x] 백에서 인가 코드 받기
- [x] 인가 코드 포함한 post 전송으로 액세스 토큰 추출

---

# 2단계 - 주문하기

## 기능 요구 사항

* 주문할 때 수령인(나)에게 보낼 메시지를 작성할 수 있다.
    * 나에게 보내기를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
* 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
* 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.

## 구현 사항

- [x] kakao login시 유저를 구분할 수 있는 email 분리 및 db에 저장
    - 세션에 email, role, kakaoAccessToken 저장
- [x] 주문 객체 만들기
    - order (productId, optionId, quantity, message) + accessToken 담아서 보내기
- [x] user - order (one-to-many)
- [x] 해당 옵션 수량 차감
    - optionId로 접근해서 차감
- [x] 위시리스트에 있으면 삭제
    - 액세스 토큰으로 이메일 받아오기
    - product_wishlist에서 product.id=:productId and wishlist.email=:email 인거 삭제
- [x] order 테스트 코드 &uarr;
- [x] 나에게 보내기 구현

---

## 리펙토링 해야 하는 부분

* session과 HttpServletRequest를 둘다 사용? -> o
* 무조건 클라이언트측에서 jwt token, kakao accesstoken 보내주는 방식으로
* 세션에는 이메일, 역할만 저장
* 역할로 입밴하는 필터 하나 더 만들어서 (/admin) 모시깽 컷 하기
* 모든 요청에 Authorization 뭐시기 담아서 보냄
* signup/login -> session에 email하고 role 저장
* 로그아웃 시 session 삭제

### 0. user

- [x] user에 role 추가
- [x] pw 암호화해서 저장
- [x] 세션에 email, role 저장
- [x] 모든 /api 경로 대상 filter 추가 : signup, login, kakaologin 제외
- [x] admin 필터에서 role=admin 아니면 차단

### 1. product

- [x] product-user (manyToOne)
- [x] product put, delete 시 user 확인 후 진행

### 2. option

- [x] option-user (manyToOne)
- [x] option service save 수정
- [x] option put, delete 시 user 확인 후 진행

### 3. order

- [x] order-user(buyer) (manyToOne)
- [x] order delete 시 user 확인

### 4. category

- [x] category는 admin만 등록 가능하도록 - filter에서 접속 필터링으로 처리완

### 5. etc

- [x] 테스트 코드 수정

---

# 3단계 - API 문서 만들기

- [x] category
- [x] option
- [x] order
- [x] product
- [x] user
- [x] wishlist
