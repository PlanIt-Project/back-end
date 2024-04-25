## PlanIT-BE 입니다.

## 주소
- 웹페이지 : https://plan-it-jkkkp.netlify.app/
- 백엔드 코드 : https://planit.run/
- 프론트 코드 : https://plan-it-jkkkp.netlify.app/
- api 명세서 :

# BE 기술스택

- Java
- Spring
- Spring Security
- JPA
- JWT
- MySql
- Docker
- Git Action
- AWS EC2
- AWS RDS
- etc.



## 팀원

|                                          김문진                                           |                                                                 박주현                                                                 |                                                              김태환                                                              |                                                                                                               
|:--------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------:| 
| <img width="160px" src="https://avatars.githubusercontent.com/u/69244467?s=400&v=4" /> |                          <img width="160px" src="https://avatars.githubusercontent.com/u/112999744?v=4" />                          |                                                  <img width="160px" src="https://avatars.githubusercontent.com/u/99311705?v=4"/>                                                  |
|                     [@moonjin-kim](https://github.com/moonjin-kim)                     |                                          [@JuhyunPark](https://github.com/JuhyunPark1831)                                           |                                           [@thana973](https://github.com/thana973)                                            |
|회원권/PT API 구현<br/>예약 API 구현<br/>로깅 기능 구현<br/>gitAction 자동배포 구현<br/>aws ec2/rds 배포|member/로그인 API 구현<br/>소셜 로그인 API 구현<br/>spring security 적용<br>상품 API 구현<br/>jwt 적용|직원 근무시간 API 구현|



## 주요 기능 

### - 회원권 / PT권 등록 기능
- 웹페이지에서 회원권과 PT권을 등록 가능

### - 트레이너의 PT 수업 예약 등록
- 트레이너가 자유롭게 PT가 가능한 시간의 대한 일정을 등록 가능

### - PT회원의 수업 예약
- 등록된 PT 수업을 웹 페이지에서 예약 가능

아키텍처
---

### 아키텍처
![KakaoTalk_Photo_2024-04-21-22-55-27](https://github.com/PlanIt-Project/back-end/assets/69244467/9c8e8047-adaa-4ded-954d-41bdac2c3152)

### DB
![db](https://github.com/PlanIt-Project/back-end/assets/69244467/ca641259-9a0d-461a-9b64-1beb03363cd8)

### 폴더 구조
```bash
./src/main/java/com/sideProject/PlanIT
├── PlanItApplication.java
├── common
│   ├── baseentity
│      └── BaseEntity.java
│   ├── loging
│   │   ├── LogTraceAspect.java
│   │   ├── TraceId.java
│   │   ├── TraceStatus.java
│   │   └── logtrace
│   │       ├── LogTrace.java
│   │       └── ThreadLocalLogTrace.java
│   ├── modules
│   │   └── FileHandler.java
│   ├── response
│   │   ├── ApiResponse.java
│   │   ├── CustomException.java
│   │   ├── ErrorCode.java
│   │   └── GlobalExceptionHandler.java
│   ├── scheduler
│   │   └── MemberShipScheduler.java
│   ├── security
│   │   ├── JwtAccessDeniedHandler.java
│   │   ├── JwtAuthenticationEntryPoint.java
│   │   ├── JwtTokenFilter.java
│   │   └── MockSpringSecurityFilter.java
│   └── util
│       ├── JwtTokenProvider.java
│       ├── JwtUtil.java
│       └── RedisUtil.java
├── config
│   ├── EmailConfig.java
│   ├── JwtConfig.java
│   ├── LogConfig.java
│   ├── RedisConfig.java
│   └── SecurityConfig.java
└── domain
    ├── file
    │   └── controller
    │       └── FileController.java
    ├── post
    │   ├── controller
    │   │   ├── BannerAdminController.java
    │   │   ├── BannerController.java
    │   │   ├── NoticeAdminController.java
    │   │   └── NoticeController.java
    │   ├── dto
    │   │   ├── request
    │   │   │   ├── BannerRequestDto.java
    │   │   │   └── NoticeRequestDto.java
    │   │   └── response
    │   │       ├── BannerResponseDto.java
    │   │       └── NoticeResponseDto.java
    │   ├── entity
    │   │   ├── Banner.java
    │   │   └── Notice.java
    │   ├── repository
    │   │   ├── BannerRepository.java
    │   │   └── NoticeRepository.java
    │   └── service
    │       ├── BannerService.java
    │       ├── BannerServiceImpl.java
    │       ├── NoticeService.java
    │       └── NoticeServiceImpl.java
    ├── product
    │   ├── controller
    │   │   ├── ProductAdminController.java
    │   │   ├── ProductController.java
    │   │   └── enums
    │   │       └── ProductSearchOption.java
    │   ├── dto
    │   │   ├── request
    │   │   │   └── ProductRequestDto.java
    │   │   └── response
    │   │       └── ProductResponseDto.java
    │   ├── entity
    │   │   ├── Product.java
    │   │   └── enums
    │   │       ├── ProductSellingType.java
    │   │       └── ProductType.java
    │   ├── repository
    │   │   └── ProductRepository.java
    │   └── service
    │       ├── ProductService.java
    │       └── ProductServiceImpl.java
    ├── program
    │   ├── controller
    │   │   ├── ProgramAdminController.java
    │   │   └── ProgramController.java
    │   ├── dto
    │   │   ├── request
    │   │   │   ├── ApproveRequestDto.java
    │   │   │   ├── ProgramModifyRequestDto.java
    │   │   │   └── RegistrationRequestDto.java
    │   │   └── response
    │   │       ├── FindRegistrationResponseDto.java
    │   │       ├── ProgramResponseDto.java
    │   │       └── RegistrationResponseDto.java
    │   ├── entity
    │   │   ├── Program.java
    │   │   ├── Registration.java
    │   │   └── enums
    │   │       ├── ProgramSearchStatus.java
    │   │       ├── ProgramStatus.java
    │   │       ├── RegistrationSearchStatus.java
    │   │       └── RegistrationStatus.java
    │   ├── repository
    │   │   ├── ProgramRepository.java
    │   │   └── RegistrationRepository.java
    │   └── service
    │       ├── ProgramService.java
    │       └── ProgramServiceImpl.java
    ├── reservation
    │   ├── controller
    │   │   ├── ENUM
    │   │   │   └── ReservationFindOption.java
    │   │   └── ReservationController.java
    │   ├── dto
    │   │   ├── reqeust
    │   │   │   ├── ChangeReservationRequestDto.java
    │   │   │   └── ReservationRequestDto.java
    │   │   └── response
    │   │       └── ReservationResponseDto.java
    │   ├── entity
    │   │   ├── ENUM
    │   │   │   └── ReservationStatus.java
    │   │   └── Reservation.java
    │   ├── repository
    │   │   └── ReservationRepository.java
    │   └── service
    │       ├── ReservationService.java
    │       └── ReservationServiceImpl.java
    └── user
        ├── controller
        │   ├── AdminController.java
        │   ├── AdminTrainerScheduleController.java
        │   ├── MemberController.java
        │   ├── SocialLoginController.java
        │   ├── TrainerScheduleController.java
        │   └── enums
        │       └── MemberSearchOption.java
        ├── dto
        │   ├── employee
        │   │   ├── request
        │   │   │   ├── TrainerRequestDto.java
        │   │   │   ├── TrainerScheduleChangeRequestDto.java
        │   │   │   └── TrainerScheduleRequestDto.java
        │   │   └── response
        │   │       ├── TrainerResponseDto.java
        │   │       ├── TrainerScheduleRegistrationResponseDto.java
        │   │       ├── TrainerScheduleResponseDto.java
        │   │       └── TrainerSubResponseDto.java
        │   └── member
        │       ├── request
        │       │   ├── EmailSendRequestDto.java
        │       │   ├── EmailValidationRequestDto.java
        │       │   ├── MemberChangePasswordRequestDto.java
        │       │   ├── MemberEditRequestDto.java
        │       │   ├── MemberSignInRequestDto.java
        │       │   └── MemberSignUpRequestDto.java
        │       └── response
        │           ├── EmployeeSemiResponseDto.java
        │           ├── JwtResponseDto.java
        │           ├── MemberResponseDto.java
        │           └── MemberSemiResponseDto.java
        ├── entity
        │   ├── Employee.java
        │   ├── EmployeeSchedule.java
        │   ├── Member.java
        │   ├── WorkTime.java
        │   └── enums
        │       ├── Gender.java
        │       ├── MemberRole.java
        │       ├── ScheduleStatus.java
        │       └── Week.java
        ├── repository
        │   ├── EmployeeRepository.java
        │   ├── MemberRepository.java
        │   └── WorkTimeRepository.java
        └── service
            ├── AuthService.java
            ├── EmailService.java
            ├── MemberService.java
            ├── MemberServiceImpl.java
            ├── SocialLoginService.java
            ├── WorktimeService.java
            └── WorktimeServiceImpl.java
```

