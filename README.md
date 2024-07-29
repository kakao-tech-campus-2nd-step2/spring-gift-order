# spring-gift-order


## 1단계
카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

* 카카오계정 로그인을 통해 인증 코드를 받는다.
* 토큰 받기를 읽고 액세스 토큰을 추출한다.
* 앱 키, 인가 코드가 절대 유출되지 않도록 한다.
  * 특히 시크릿 키는 GitHub나 클라이언트 코드 등 외부에서 볼 수 있는 곳에 추가하지 않는다.

## 2단계
카카오톡 메시지 API를 사용하여 주문하기 기능을 구현한다.

* 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
* 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
* 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
* 나에게 보내기를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
  * 메시지는 메시지 템플릿의 기본 템플릿이나 사용자 정의 템플릿을 사용하여 자유롭게 작성한다.
* 아래 예시와 같이 HTTP 메시지를 주고받도록 구현한다.
### Request
POST /api/orders HTTP/1.1 \
Authorization: Bearer {token} \
Content-Type: application/json \

{ \
"optionId": 1, \
"quantity": 2, \
"message": "Please handle this order with care."\
}

### Response
HTTP/1.1 201 Created \
Content-Type: application/json \

{ \
"id": 1, \
"optionId": 1, \
"quantity": 2, \
"orderDateTime": "2024-07-21T10:00:00", \
"message": "Please handle this order with care." \
}


## 3단계 API 문서 만들기
# Product and Option Management API

다음 API들을 사용하면 상품, 옵션 등을 관리할 수 있습니다.

## Endpoints

### 전체 상품 조회

pagination을 사용해 전체 상품을 조회

- **URL**: `/products`
- **Method**: `GET`
- **Query Parameters**:
  - `page` (optional, default: 0) - Page number.
  - `size` (optional, default: 5) - Page size.
- **Response**:
  - `200 OK` - Returns a list of products.

### 상품 추가 

주어진 상세정보를 이용해 상품을 추가

- **URL**: `/products/add`
- **Method**: `POST`
- **Form Parameters**:
  - `name` (string, required) - Product name.
  - `price` (int, required) - Product price.
  - `imageUrl` (string, required) - Product image URL.
  - `categoryId` (long, required) - Category ID.
  - `optionNames` (list of strings, required) - Option names.
  - `optionQuantities` (list of longs, required) - Option quantities.
- **Response**:
  - `302 Found` - Redirects to the product list page.

### 상품 수정

주어진 상세 정보로 이미 존재하는 상품 정보 수정

- **URL**: `/products/update`
- **Method**: `POST`
- **Form Parameters**:
  - `name` (string, required) - Product name.
  - `price` (int, required) - Product price.
  - `imageUrl` (string, required) - Product image URL.
  - `categoryId` (long, required) - Category ID.
  - `optionNames` (list of strings, required) - Option names.
  - `optionQuantities` (list of longs, required) - Option quantities.
- **Response**:
  - `302 Found` - Redirects to the product list page.

### 상품 삭제
주어진 상품ID로 해당 상품을 삭제

- **URL**: `/products/delete/{id}`
- **Method**: `POST`
- **Path Parameters**:
  - `id` (long, required) - Product ID.
- **Response**:
  - `302 Found` - Redirects to the product list page.

### 상세 정보 조회

상품ID로 검색해 상세 정보를 불러옴

- **URL**: `/products/view/{id}`
- **Method**: `GET`
- **Path Parameters**:
  - `id` (long, required) - Product ID.
- **Response**:
  - `200 OK` - Returns the product details.

### ID로 상품 불러오기

상품 ID로 특정 상품 조회

- **URL**: `/products/{id}`
- **Method**: `GET`
- **Path Parameters**:
  - `id` (long, required) - Product ID.
- **Response**:
  - `200 OK` - Returns the product details.
  - `404 Not Found` - If the product is not found.

## Models

### Product

- **id** (long) - 상품의 고유 식별자
- **name** (string) - 상품의 이름
- **price** (int) - 상품의 가격
- **imageUrl** (string) - 상품 사진의 URL
- **category** (Category) - 해당 상품에 해당하는 카테고리
- **options** (List of Options) - 상품에 연결된 옵션

### Option

- **id** (long) - 옵션의 고유 식별자
- **optionName** (string) - 옵션의 이름
- **quantity** (long) - 옵션의 재고 수량
- **product** (Product) - 해당 옵션이 연결된 상품

### Category

- **id** (long) - 카테고리의 고유 식별자
- **name** (string) - 카테고리의 이름
- **products** (List of Products) - 카테고리와 연결된 상품


# Member Management API

다음 API들을 사용하면 회원가입, 로그인 등 회원관리가 가능합니다.

## Endpoints

### 회원가입

주어진 정보들로 새로운 회원을 등록

- **URL**: `/members/register`
- **Method**: `POST`
- **Request Body**:
  - `email` (string, required) - 회원의 이메일
  - `password` (string, required) - 회원의 비밀번호

- **Response**:
  - `200 OK` - JWT 토큰 반환
  - `409 CONFLICT` - 이미 존재하는 이메일

#### Example Request
```json
{
  "email": "example@example.com",
  "password": "password123"
}
```

#### Example Response
```json
{
  "token": "your_jwt_token"
}
```
### 회원 로그인
회원을 인증하고 성공하면 JWT 토큰을 반환

- **URL**: `/members/login`
- **Method**: `POST`
- **Request Body**:
  - `email` (string, required) - 회원의 이메일
  - `password` (string, required) - 회원의 비밀번호
- **Response**:
  - `200 OK` - JWT 토큰 반환
  - `403 FORBIDDEN` - 유효하지 않은 이메일과 비밀번호

#### Example Request
```json
{
    "email": "example@example.com",
    "password": "password123"
}
```
#### Example Response
```json
{
    "token": "your_jwt_token"
}
```
## Models
### Member
- **id** (long) - member의 고유 식별자
- **email** (string) - member의 이메일
- **password** (string) - member의 비밀번호
- **name** (string) - member의 이름
## Utility
### JwtUtil
- **generateToken** (method) - 회원 ID와 이메일을 기반으로 JWT 토큰을 생성

# Wishlist Management API
## 위시리스트의 상품 추가
인증된 회원의 위시리스트에 상품을 추가

- **URL**: `/wishlist/add`
- **Method**: `POST`
- **Request Header**:
  - **Authorization** (string, required) - Bearer token.
- **Request Parameters**:
  - **productId** (long, required) - 상품 ID
- **Response**:
  - `200 OK` - 추가된 위시리스트 항목을 반환
  - `401 UNAUTHORIZED` - 유효하지 않은 토큰
#### Example Request
```json

{
  "Authorization": "Bearer your_jwt_token",
  "productId": 1
}
```
#### Example Response
```json
{
  "id": 1,
  "productId": 1,
  "memberId": 1
}
```
## 위시리스트 항목 조회 
인증된 회원에 대해 페이지가 매겨진 위시리스트 항목 목록을 조회

- **URL**: `/wishlist/items`
- **Method**: `GET`
- **Request Header**:
  - **Authorization** (string, required) - Bearer token.
- **Request Parameters**:
  - **page** (int, optional, default=0) - 페이지 순번 숫자
  - **size** (int, optional, default=5) - 페이지 크기
- **Response**:
  - `200 OK` - 위시리스트 항목의 페이지가 매겨진 목록을 반환

## 위시리스트의 상품 삭제
해당 ID의 상품을 삭제

- **URL**: `/wishlist/delete/{id}`
- **Method**: `POST`
- **Path Variable**:
  - **id** (long, required) - 상품 ID
- **Response**:
  - `302 Found` - 성공 메시지와 함께 위시리스트로 리디렉션
## Models
### Product
- **id** (long) - 상품의 고유 식별자
- **name** (string) - 상품의 이름
- **price** (int) - 상품의 가격
- **imageUrl** (string) - 상품 사진의 URL
- **category** (Category) - 상품의 해당 카테고리
- **options** (list of Option) - 상품의 옵션 리스트
### Option
- **id** (long) - 옵션의 고유 식별자
- **optionName** (string) - 옵션의 이름
- **quantity** (long) - 옵션의 재고 수량
- **product** (Product) - 연결된 상품
### Member
- **id** (long) - 회원의 고유식별자
- **email** (string) - 회원의 이메일
- **password** (string) - 회원의 비밀번호
- **name** (string) - 회원의 이름
### WishList
- **id** (long) - 위시리스트 항목의 고유 식별자
- **productId** (long) - 위시리스트 항목과 연결된 상품 ID
- **memberId** (long) - 위시리스트 항목과 연결된 회원 ID

## Utility
### JwtUtil
- **generateToken** (method) - 회원 ID와 이메일을 기반으로 JWT 토큰을 생성
- **extractClaims** (method) - JWT 토큰에서 클레임을 추출
- **isTokenValid** (method) - JWT 토큰의 유효성을 검사

# Option API Documentation

이 API는 `제품` 엔터티와 연결된 `옵션` 엔터티를 관리하기 위한 엔드포인트를 제공\
옵션 추가, 삭제 및 검색 가능

## Endpoints

### 옵션 추가

- **URL:** `/options/add`
- **Method:** `POST`
- **Description:** 특정 제품에 옵션을 추가
- **Parameters:**
  - `productId` (Long): 옵션이 추가될 상품의 ID
  - `optionNames` (List<String>): 추가할 옵션 이름 목록
  - `optionQuantities` (List<Long>): 옵션명에 해당하는 수량 목록
- **Response:** 성공 메시지와 함께 제품 세부정보 페이지로 리디렉션

### 옵션 삭제
- **URL**: `/options/delete/{productId}`
- **Method**: `POST`
- **Description**: 특정 상품과 관련된 모든 옵션을 삭제
- **Parameters**:
  - `productId` (Long): 옵션을 삭제할 상품의 ID
- **Response**: 삭제 후 상품 상세 페이지로 리디렉션

### ID로 옵션 조회
- **URL**: `/options/{id}`
- **Method**: `GET`
- **Description**: ID로 옵션의 세부정보를 조회
- **Parameters**:
  - **id** (Long): 검색할 옵션의 ID입
- **Response**: 옵션이 발견되면 세부정보를 반환, 그렇지 않으면 404 상태를 반환

### Error Handling
- `404 Not Found`: 지정한 ID의 옵션이 존재하지 않는 경우
- `400 Bad Request`: 요청 매개변수가 유효하지 않거나 누락된 경우

# Category API Documentation

이 API는 `Category` 항목을 관리하기 위한 엔드포인트를 제공\ 
카테고리 추가, 삭제 가능

## Endpoints

### 카테고리 추가

- **URL:** `/category/add`
- **Method:** `POST`
- **Description:** 새로운 카테고리를 추가
- **Parameters:**
  - **Request Body:**
    - `category` (Category): 추가할 카테고리 객체
- **Response:**
  - **201 Created:** 카테고리가 성공적으로 추가된 경우
  - **409 Conflict:** 카테고리가 이미 존재하는 경우
- **Example Request:**

  ```
  POST /category/add
  Content-Type: application/json

  {
    "categoryName": "New Category"
  }

### 카테고리 삭제
- **URL**: `/category/delete/{id}`
- **Method**: `POST`
- **Description**: ID별로 카테고리를 삭제
- **Parameters**:
  - **Path Variable**:
    - **id**(Long): 삭제할 카테고리의 ID
  - **Request Body**:
    - **category** (Category): 삭제할 카테고리 객체
- **Response**:
  - `200 OK`: 카테고리가 성공적으로 삭제된 경우
  - `409 Conflict`: 카테고리가 존재하지 않는 경우
- **Example Request**:
```
POST /category/delete/1
Content-Type: application/json

{
"categoryName": "Existing Category"
}
```

- **Error Handling**
- `409 Conflict`: 추가 시 카테고리가 이미 존재하거나, 삭제 시 카테고리가 존재하지 않는 경우
- `400 Bad Request`: 요청 매개변수가 유효하지 않거나 누락된 경우

# Kakao Login API Documentation

This API provides endpoints for handling Kakao login and authorization. It allows redirecting to Kakao authorization, retrieving access tokens, and handling user login.

## Endpoints

### Authorize

- **URL:** `/kakao/login/authorize`
- **Method:** `GET`
- **Description:** 사용자를 카카오 인증 페이지로 리디렉션
- **Response:** 카카오 인증 URL로 리디렉션
- **Example Request:**

```http
 GET /kakao/login/authorize
```

### Token
- **URL**: `/kakao/login/token`
- **Method**: `GET`
- **Description**: 인증코드를 이용해 카카오에서 엑세스 토큰을 받아 사용자로 로그인한 후 JWT 토큰을 반환
- **Parameters**:
  - **code** (String): 카카오로부터 받은 인증코드
- **Response**:
- `201 Created`: 사용자가 성공적으로 로그인하면 JWT 토큰이 생성
  - **Headers**:
    - **Authorization**: 생성된 JWT 토큰
  - **Body**:
    - **responseBody** (Map<String, Object>): 로그인 프로세스에 대한 추가 정보
- **Example Request:

```
GET /kakao/login/token?code=authorization_code_here
```
- **Error Handling**
  - `400 Bad Request`: 요청 매개변수가 유효하지 않거나 누락된 경우
  - `500 Internal Server Error`: 로그인 과정에서 오류가 발생한 경우

# Order API Documentation

이 API는 주문 처리를 위한 엔드포인트를 제공합니다. \
새로운 주문 추가 가능

## Endpoints

### Add Order

- **URL:** `/orders`
- **Method:** `POST`
- **Description:** 새 주문을 추가하고 카카오톡 나에게 보내기로 메시지를 전송
- **Parameters:**
  - **Headers:**
    - `Authorization` (String): 회원을 인증하고 연결할 JWT 토큰
  - **Request Body:**
    - `order` (Order): 추가할 주문
- **Response:**
  - **201 Created:** 주문이 성공적으로 추가된 경우
  - **Headers:**
    - `Authorization`: 회원을 인증하고 연결할 JWT 토큰
  - **Body:**
    - `order` (Order): 추가된 주문 
- **Example Request:**

```
  POST /orders
  Content-Type: application/json
  Authorization: Bearer your_jwt_token_here

  {
    "productId": 1,
    "quantity": 2,
    "message": "Please handle this order with care."
  }
```
- **Error Handling**
  - `400 Bad Request`: 요청 매개변수가 유효하지 않거나 누락된 경우
  - `500 Internal Server Error`: 주문 처리 중 오류가 발생한 경우
