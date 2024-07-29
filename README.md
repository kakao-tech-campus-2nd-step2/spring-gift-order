# 5주차 과제

## API 명세서
### Product
#### product 조회
    - URL: /products
    - Method: GET
    - Parameters: Model
    - return : 뷰템플릿 / products
#### 개별 상품 조회
    - URL: /products/{id}
    - Method: GET
    - Parameters: id, Model
    - return : 뷰템플릿 / product
#### 상품 정보 수정 페이지
    - URL: /products/{id}/eidit
    - Method: GET
    - Parameters: id, Model
    - return : 뷰템플릿 / editForm
#### 상품 정보 수정
    - URL: /products/{id}/eidit
    - Method: POST
    - Parameters: id, productRequestDto
    - return : 뷰템플릿 / products
#### 상품 추가 페이지
    - URL: /products/{id}/add
    - Method: GET
    - Parameters: Model
    - return : 뷰템플릿 / addForm
#### 상품 추가
    - URL: /products/{id}/add
    - Method: POST
    - Parameters: productRequestDto
    - return : 뷰템플릿 / products
#### 상품 삭제
    - URL: /products/{id}/delete
    - Method: GET
    - Parameters: id
    - return : 뷰템플릿 / products

### Member
#### 회원 가입
    - URL: /members/register
    - Method: POST
    - Parameters: MemberRequestDto
    - return : Status Created / MemberResponseDto
#### 회원 로그인
    - URL: /members/login
    - Method: POST
    - Parameters: MemberRequestDto
    - return : Status Ok / response

### Oauth
#### 카카오 로그인
    - URL: /oauth/kakao
    - Method: GET
    - Parameters: HttpServletResponse response
    - return : kakao로그인페이지 리다이렉트
#### 카카오 토큰 생성
    - URL: /
    - Method: GET
    - Parameters: String authorization_code
    - return : Status Ok / access_Token
#### 카카오 회원가입
    - URL: /oauth/register
    - Method: POST
    - Parameters: OAuthTokenDto
    - return : Status CREATED 

### Option
#### product 생성(option 포함)
    - URL: /api/products
    - Method: POST
    - Parameters: ProductOptionRequestDto
    - return : Status Ok 
#### option 추가
    - URL: /api/products/{productId}/options
    - Method: POST
    - Parameters: Long id, OptionRequestDto
    - return : Status Ok 
#### option 확인
    - URL: /api/products/{productId}/options
    - Method: GET
    - Parameters: Long productId
    - return : Status Ok / List<OptionResponseDto>
#### option 삭제
    - URL: /api/products/{productId}/options/{optionId}
    - Method: DELETE
    - Parameters: Long productId , Long optionId
    - return : Status OK
#### option 수량 차감
    - URL: /api/products/{productId}/options/{optionId}
    - Method: PUT
    - Parameters: Long productId , Long optionId
    - return : Status OK

### Order
#### order 추가
    - URL: /api/order
    - Method: POST
    - Parameters: String fullToken, OrderRequestDto
    - return : Status OK / OrderResponseDto
#### order 메세지 전송
    - URL: /api/order/message/list
    - Method: GET
    - Parameters: String fullToken
    - return : 

### Wish
#### wish 추가
    - URL: /wishes
    - Method: POST
    - Parameters: String fullToken, WishRequestDto
    - return : Status CREATED
#### wish 조회
    - URL: /wishes
    - Method: GET
    - Parameters: String fullToken, Pageable
    - return : Status Ok / pageable
#### wish 삭제
    - URL: /wishes/{productId}
    - Method: DELETE
    - Parameters: String fullToken, Long productId
    - return : Status Ok 
#### wish 수정
    - URL: /wishes/{productId}
    - Method: PATCH
    - Parameters: String fullToken, Long productId, WishPatchDto
    - return : Status Ok 

### Category
#### 카테고리 추가
    - URL: /api/categories
    - Method: POST
    - Description: 새로운 카테고리를 추가합니다.
    - Parameters: -
        category (Category): 추가할 카테고리 객체
    - return : Status Created
#### 카테고리 조회
    - URL: /api/categories
    - Method: GET
    - Description: 모든 카테고리를 조회합니다.
    - Parameters: -
        category (Category): 추가할 카테고리 객체
    - return : Status: Created / List<CategoryResponseDto>