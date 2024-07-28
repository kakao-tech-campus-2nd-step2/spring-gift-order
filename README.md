# spring-gift-order


쓰고 나니 부족한 부분이 보여 기능추가를 해야겠다. 

## API 문서

* 모든 URL 형식은 `http://localhost:8080/spring-gift` 로시작합니다.
* spring-gift 가 제공하는 기능은 회원가입, 제품, 카테코리, 옵션 CRUD와 위시리스트만들기, 주문기능이 있습니다.

### 유저 관련 API
| 제목         | 메서드/URL 엔드포인트 | 요청 컨텐트 타입 / 요청 객체                                 | 응답객체                                                                                              | 설명                                     |
|--------------|-----------------------|-----------------------------------------------------------|---------------------------------------------------------------------------------------------------|----------------------------------------|
| 회원가입 API | POST /members/join    | Content-Type: application/json<br>Body: UserJoinRequest   | header: {"Authorization": "Bearer {token}" } Body: { "redirectUrl": "/spring-gift"}               |  |
| 로그인 API   | POST /members/login   | Content-Type: application/json<br>Body: UserLoginRequest  | header: {"Authorization": "Bearer {token}" } Body: { "redirectUrl": "/spring-gift"}    |                                        |

### 카카오 로그인 API
| 제목                     | 메서드/URL 엔드포인트        | 요청 컨텐트 타입 / 요청 객체 | 응답객체                                       | 설명 |
|--------------------------|-------------------------------|------------------------------|------------------------------------------------|------|
| 카카오 로그인 화면 요청   | GET /kakao/login              | -                            | Redirect to Kakao Login URL                   | 클라이언트가 이 URL로 요청하면 카카오 로그인 화면으로 리다이렉트됩니다. 응답이 다른 URL (`/kakao/login/callback`)로 전달됩니다. |
| 카카오 로그인 및 토큰 처리 | GET /kakao/login/callback     | Query Parameters: code       | Status: 201 Created<br>Body: "successfully LoggedIn" | 카카오 서버에서 리다이렉트된 코드를 사용하여 사용자를 로그인 처리하고 JWT 토큰을 반환합니다. |

* 프론트에서 토큰 처리 메서드로 요청하실 필요는 없습니다. 다만 응답을 callback에서 받습니다.


### 제품 등록,수정,삭제 API
| 제목              | 메서드/URL 엔드포인트    | 요청 컨텐트 타입 / 요청 객체                                                 | 응답객체                          | 설명 |
|------------------|-------------------------|---------------------------------------------------------------------------|-----------------------------------|------|
| 제품 등록        | POST /admin/product     | Content-Type: application/json<br>Body: ProductAllRequest                 | Status: 200<br>Body: "Product added successfully" |      |
| 제품 정보 업데이트 | PUT /admin/product/{id} | Content-Type: application/json<br>Body: ProductRequest                    | Status: 200<br>Body: "Product updated successfully" |      |
| 제품 삭제        | DELETE /admin/product/{id} | Content-Type: application/json<br>Path Variable: id (Long)               | Status: 200<br>Body: "Product deleted successfully" |      |
* 현재 권한 검사가 일어나지는 않지만, 이후 권한을 두어 추가할 예정입니다. 
* 제품을 조회하거나 볼 수 있는 기능은 아래 따로 두었습니다.

### 제품,옵션 조회 API 
| 제목                 | 메서드/URL 엔드포인트        | 요청 컨텐트 타입 / 요청 객체 | 응답객체                                      | 설명 |
|----------------------|------------------------------|------------------------------|----------------------------------------------|------|
| 제품 목록 페이지 조회 | GET /products                | -                            | Status: 200 OK<br>Body: Page of ProductResponce | 페이지네이션을 이용한 제품 목록을 반환합니다. |
| 특정 제품 조회       | GET /product/{id}            | -                            | Status: 200 OK<br>Body: ProductResponce       | 제품 ID에 해당하는 상세 제품 정보를 반환합니다. |
| 제품 옵션 조회       | GET /product/{id}/options    | -                            | Status: 200 OK<br>Body: ProductAllResponse   | 제품 ID에 해당하는 제품과 그 옵션들의 상세 정보를 반환합니다. |


### 카테고리 관련 API 
| 제목            | 메서드/URL 엔드포인트    | 요청 컨텐트 타입 / 요청 객체                                    | 응답객체                             | 설명 |
|-----------------|-------------------------|----------------------------------------------------------------|--------------------------------------|------|
| 카테고리 생성    | POST /admin/category    | Content-Type: application/json<br>Body: CategoryRequest       | Status: 201 Created<br>Body: "Category created successfully" |      |
| 카테고리 업데이트 | PUT /admin/category/{id}| Content-Type: application/json<br>Body: CategoryRequest       | Status: 200 OK<br>Body: "Category updated successfully" |      |
| 카테고리 삭제    | DELETE /admin/category/{id} | Content-Type: application/json<br>Path Variable: id (Long)  | Status: 200 OK<br>Body: "Category deleted successfully" |      |


### 옵션 관련 API 
| 제목               | 메서드/URL 엔드포인트                          | 요청 컨텐트 타입 / 요청 객체                                | 응답객체                                        | 설명 |
|--------------------|-----------------------------------------------|-----------------------------------------------------------|------------------------------------------------|------|
| 옵션 조회          | GET /admin/product/{id}/options               | Content-Type: application/json                            | Status: 200 OK<br>Body: List of OptionResponse |      |
| 옵션 추가          | POST /admin/product/{productId}/option        | Content-Type: application/json<br>Body: OptionRequest     | Status: 200 OK<br>Body: "Option added successfully" |      |
| 옵션 수정          | PUT /admin/product/{productId}/option/{optionId} | Content-Type: application/json<br>Body: OptionRequest     | Status: 200 OK<br>Body: "Option updated successfully" |      |
| 옵션 삭제          | DELETE /admin/product/{productId}/option/{optionId} | Content-Type: application/json                            | Status: 200 OK<br>Body: "Option deleted successfully" |      |
| 옵션 수량 변경     | PUT /admin/product/{productId}/option/{optionId}/quantity | Content-Type: application/json<br>Body: OptionChangeQuantityRequest | Status: 200 OK<br>Body: "Option Quantity changed successfully" |      |


### 위시리스트 관련 API 
| 제목               | 메서드/URL 엔드포인트        | 요청 컨텐트 타입 / 요청 객체 | 응답객체                                           | 설명 |
|--------------------|-----------------------------|------------------------------|----------------------------------------------------|------|
| 위시리스트 제품 삭제 | DELETE /wishlist/{productId}| -                            | Status: 200 OK<br>Body: "successfully deleted the item to your wishlist" | 위시리스트에서 특정 제품을 삭제합니다. |
| 위시리스트 조회     | GET /wishlists              | -                            | Status: 200 OK<br>Body: Page of WishProductResponse | 사용자의 위시리스트 페이지를 반환합니다. |
| 위시리스트 제품 추가 | POST /wishlist/{productId}  | -                            | Status: 200 OK<br>Body: "successfully added the item to your wishlist" | 위시리스트에 특정 제품을 추가합니다. |


### 주문 관련 API 
| 제목       | 메서드/URL 엔드포인트 | 요청 컨텐트 타입 / 요청 객체                        | 응답객체                            | 설명 |
|------------|-----------------------|--------------------------------------------------|-------------------------------------|------|
| 제품 주문   | POST /order/{id}      | Content-Type: application/json<br>Body: OrderRequest | Status: 200 OK<br>Body: OrderResponce | 제품을 주문하고, 해당 제품의 옵션 수량을 감소시킨 후, 카카오 메시지로 주문 내용을 전송합니다. |

### 페이지 관련 API 
| 제목               | 메서드/URL 엔드포인트        | 요청 컨텐트 타입 / 요청 객체 | 응답객체                          | 설명 |
|--------------------|------------------------------|------------------------------|-----------------------------------|------|
| 관리자 제품 페이지  | GET /admin/product           | -                            | HTML: "adminProduct"              | 관리자 제품 관리 페이지를 반환합니다. |
| 회원 로그인/등록 페이지 | GET /members/login, /members/register | -                            | HTML: "user"                      | 회원 로그인 및 등록 페이지를 반환합니다. |
| 제품 페이지        | GET /product                 | -                            | HTML: "memberProduct"             | 제품 페이지를 반환합니다. |
| 위시리스트 페이지   | GET /wishlist                | -                            | HTML: "memberWishList"            | 회원의 위시리스트 페이지를 반환합니다. |
=======
