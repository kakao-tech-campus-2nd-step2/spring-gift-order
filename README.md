# spring-gift-enhancement

## 기능 요구사항
카카오 로그인을 통해 인가 코드를 받고, 인가코드를 사용해 토큰을 받을 후 향후 카카오 API 사용을 준비한다.
    - 카카오계정 로그인을 통해 인증코드를 받는다
    - 토큰 받기를 읽고 액세스 토큰을 추출한다.
    - 앱 키, 인가 코드가 절대 유출되지 않도록 한다.
        - 특히 시크릿 키는 Github나 클라이언트 코드등 외부에서 볼 수 있는 곳에 추가하지 않는다.
    - (선택) 인가코드를 받는 방법이 불편한 경우 카카오 로그인 화면을 구현한다.
지금과 같이 클라이언트가 없는 상황에서는 아래와 같은 방법으로 REST API 키를 복사한다.
1. 내 애플리케이션 > 앱 설정 > 앱 키로 이동하여 REST API키를 복사한다.
2. https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri=http://localhost:8080&client_id={REST_API_KEY}에 접속하여 카카오톡 메시지 전송에 동의한다.
3. http://localhost:8080/?code={AUTHORIZATION_CODE}에서 인가 코드를 추출한다.

### 힌트
**애플리케이션 등록**
카카오 플랫폼 서비스를 이용하여 개발한 애플리케이션은 카카오의 상표를 사용할 수 없으므로 아래와 같이 간단하게 작성해도 된다.
- 앱 이름: spring-gift
- 회사명: 이름
- 카테고리: 교육

<details>
<summary> Step1 정리 </summary>
## 기능 요구 사항

1. 상품 정보에 카테고리를 추가한다.
   1. 상품 카테고리는 수정할 수 있다.
   2. 관리자 화면에서 상품을 추가할 때 카테고리를 지정할 수 있다.
2. 카테고리는 1차 카테고리만 있으며 2차 카테고리는 고려하지 않는다.
3. 카테고리의 예시는 아래와 같다.
   1. 교환권, 상품권, 뷰티, 패션, 식품, 리빙/도서, 레저/스포츠, 아티스트/캐릭터, 유아동/반려, 디지털/가전, 카카오프렌즈, 트렌드 선물, 백화점 ...
      아래 예시와 같이 HTTP 메시지를 주고 받도록 구현한다.

**Request**
```angular2html
GET /api/categories HTTP/1.1
```
**Response**
```angular2html
HTTP/1.1 200 
Content-Type: application/json

[
  {
    "id": 91,
    "name": "교환권",
    "color": "#6c95d1",
    "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
    "description": ""
  }
]
```

### 힌트
아래의 DDL을 보고 유추한다.
```angular2html
create table category
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB

create table product
(
    price       integer      not null,
    category_id bigint       not null,
    id          bigint       not null auto_increment,
    name        varchar(15)  not null,
    image_url   varchar(255) not null,
    primary key (id)
) engine=InnoDB

alter table category
    add constraint uk_category unique (name)

alter table product
    add constraint fk_product_category_id_ref_category_id
        foreign key (category_id)
            references category (id)

```
</details>

<details>
<summary>step2 정리</summary>
## 기능 요구사항
1. 상품 정보에 옵션을 추가한다. 상품과 옵션 모델 간의 관계를 고려하여 설계하고 구현한다.
   1. 상품에는 항상 하나 이상의 옵션이 있어야 한다.
      1. 옵션 이름은 공백을 포함하여 최대 50자까지 입력할 수 있다.
      2. 특수 문자
         1. 가능:  ( ), [ ], +, -, &, /, _
         2. 그 외 특수 문자 사용 불가
      3. 옵션 수량은 최소 1개 이상 1억개 미만이다.
   2. 중복된 옵션은 구매 시 고객에게 불편을 줄 수 있다. 동일한 상품 내의 옵션 이름은 중복될 수 없다.
   3. (선택) 관리자 화면에서 옵션을 추가할 수 있다.
2. 아래 예시와 같이 HTTP 메시지를 주고받도록 구현한다.

**Request**
   ```
   GET /api/products/1/options HTTP/1.1
   ```

**Response**
   ```
   HTTP/1.1 200 
Content-Type: application/json

   [
     {
       "id": 464946561,
       "name": "01. [Best] 시어버터 핸드 & 시어 스틱 립 밤",
       "quantity": 969
     }
   ]

   ```

3. 상품 옵션의 수량을 지정된 숫자만큼 빼는 기능을 구현한다.
    1. 별도의 HTTP API를 만들 필요는 없다.
    2. 서비스 클래스 또는 엔티티 클래스에서 기능을 구현하고 나중에 사용할 수 있도록 한다.
    3. 힌트
       ```
       var option = optionRepository.findByProductId(productId).orElseThrow();
       option.subtract(quantity) 
       ```

---
### 이전까지의 선택 사항
1. E2E 테스트
    - 내가 만든 기능이 제대로 작동하는지 자동화하고 검증하려면 어떻게 해야 할까? 그 방법을 생각해보고 구현한다.
2. API 사양에 관해 클라이언트와 어떻게 소통할 수 있을까? 어떻게 하면 편하게 소통할 수 있을지 고민해보고 그 방법을 구현한다.
3. 동시성 테스트
    - 상품 옵션의 수량이 하나 남았는데 동시에 차감 요청이 들어오는 경우를 생각한다. 수량을 정상적으로 차감하려면 어떻게 구현해야 할까?

</details>