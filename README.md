# Intro

**Intro(단축키,기본규칙) -> 개발과정 -> 기능들 로직(EX:로그인) -> 폴더구조 순서로 정리하겠다.**

**총 기능**

* **"로그인", "플래너" , "타이머(클라단에서 해결한다 함)", "캐릭터(경험치)", "팔로잉", "알림", "아이템" 기능**

<br>

**단축키**

* `Alt + Insert` : getter, setter, constructor 등 자동 생성
* `Ctrl + Alt + O` : 사용안하는 import 자동 정리(제거 등)
* `Ctrl + Alt + L` : 자동 정렬 -> Google Java Convention이나 Default 로 사용중
* `Ctrl + Alt + V` : 변수 선언부를 자동 작성
* `Ctrl + Alt + M` : 코드 리팩토링하기 쉽게끔 함수 자동 생성
* `Ctrl+T->extra method` : 코드 리팩토링하기 쉽게끔 드래그한 코드를 하나의 함수로 자동 생성
* `Alt + Shift + Down/Up` : 코드 한줄을 위, 아래 자리 이동 가능
* `Ctrl + D` : 코드 한줄 바로 아래에 복제
* `Ctrl + Alt + Shift` : 멀티 커서 가능
* `Shift + F6` : 변수명을 한번에 바꿀 때 사용
* `Alt + 1 ` : 왼쪽 프로젝트 폴더 구조 열기
* `Alt + F12` : 터미널 창 열기

<br>

**개발 환경**

* IDE: IntelliJ
* Build Tool: Gradle - Groovy
* java : 11 -> 17
* spring boot : '2.7.11' -> '3.1.2'
* dependencies : JUnit4 -> JUnit5
* database: jdbc:h2:tcp://localhost/~/lvpldb/lvpl

<br>

**기본규칙(TIP)**

* **네이밍**
  - **Database**
    - 테이블명 형식으로 `ORDER 또는 order` 사용 **=> 대문자 or 소문자**
    - 컬럼명 형식으로 `order_id` 사용 **=> 스네이크 케이스**
    - 스프링에선 테이블 매핑 마지막에 전부 **"대문자"**로 바꿔주는것 같음
      - DB보니까 대문자로 전부 네이밍 되어있길래,,
  - **JPA -> ORM(객체 관계 매핑)**
    - 엔티티명 형식으로 `OrderItem` 사용 **=> 파스칼 케이스**
    - 필드명 형식으로 `orderId` 사용 **=> 카멜 케이스**  
      - **스프링 부트는 자동으로 필드명을 `orderId -> order_id` 로 컬럼명 찾아서 매핑**
      - **엔티티명은 `OrderItem -> ORDERITEM` 처럼 "대문자"로 바꿔주는건 여전하고, 다른 규칙은 없으므로 네이밍 작업 필요시 직접 테이블명과 매핑**
* **테이블과 엔티티 설계**
  * **N:N** 관계는 **1:N, N:1**로 풀고, 외래키는 **N**에 사용
  * **1:1**의 경우 **상황에 따라** 사용 - 보통은 `주 테이블에 외래키` 사용
    * 주 테이블 외래키 단방향 - 단점 : 값 없으면 외래 키에 null 허용
    * 대상 테이블에 외래키 양방향 - 단점 : 무조건 즉시로딩
  * **상속**의 경우 JOINED, SINGLE_TABLE 등등 전략 중에서 **JOINED 전략**을 많이 사용
    * SINGLE_TABLE : 부모, 자식테이블 전부 합쳐서 하나의 테이블로 구성
    * JOINED : 부모, 자식들 테이블 생성 및 Join으로 조회
  * 개발과정에선 **Getter, Setter**를 열어두고 나중에 리펙토링으로 **Setter** 들은 제거
    * 엔티티에서의 **비지니스 메서드** 구현은 **Setter** 제거 효과
    * **setter를 최대한 사용하지 않게끔 DTO 방식 권장**
  * 엔티티 설계 때 **연관관계는 단방향 우선 개발(테스트)** 후 양방향 관계 추가
  * **무조건 "지연 로딩" 개발** 및 `@XToOne` 은 기본이 **즉시 로딩**이므로 **지연로딩**으로 전부 변경
  * **ENUM** 데이터는 반드시 `@Enumerated(EnumType.STRING)` 로 옵션 지정 필수
  * **컬렉션(List같은것들)은 필드에서 바로 초기화가 효과적**
  * **의존성 주입(DI)은 스프링 필드 주입 대신에 생성자 주입을 사용**
    * 즉, `@RequiredArgsConstructor, final` 를 사용
  * 엔티티에서 `연관관계 편의 메서드, 비지니스 메서드, 생성 메서드, 조회 메서드` 등등 많은것을 개발하기를 권장
    * **`도메인 모델 패턴` 방식을 행하고 있기 때문**
* **엔티티 매핑**
  * **N:1,1:N** 의 경우 **"N:1 단(or양)방향"** 권장
  * **1:1** 의 경우 **"주 테이블 외래키 단방향"** 권장
  * **"양방향" 연관관계 코드로 작성시(개인적인 생각)** 연관관계 편의 메서드와 mappedBy 를 세트로 같이 작성
* **추가정보**
  * **"지연 로딩" 에서 "즉시 로딩"의 효과를 얻는법은 fetch join을 활용**
  * **동적 쿼리**는 **Querydsl** 을 권장
  * **API 응답 스펙에 맞추어 별도의 DTO를 반환 권장 (엔티티 노출 금지 목적)**
  * **setter를 최대한 사용하지 않게끔 DTO 방식 권장**
  * **JSON 반환시 꼭 마지막에 객체로 감싸서 반환**
  * **DTO와 DI(의존성 주입) 구분할 것**
  * **준영속 엔티티를 수정할 때 Merge방식보다는 Dirty Checking 방법 권장**

<br><br>

# 개발과정

* **요구사항 분석(대략적 기능)**
* **구체적인 요구사항 목록(상세한 기능)**
* **설계 시작**
  * 도메인 모델 분석(간략히)
  * 테이블 설계(DB)
  * 엔티티 설계(JPA)
  * **ERDCloud 툴을 사용한 설계**
* **코드 구현 (각 파트별 TDD도 함께)**
  * 도메인 구현 -> 엔티티를 의미하며, 모든 계층에서 사용
  * 레퍼지토리 구현 -> DB와 상호작용
  * 서비스 구현 -> 비지니스 로직 & 트랜잭션
    * `도메인 모델 패턴` : 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 방식
    * `트랜잭션 스크립트 패턴` : 엔티티에는 비지니스 로직이 거의 없고 서비스 계층이 담당하는 방식
    * **참고로 `도메인 모델 패턴` 방식으로 진행 중**

  * 컨트롤러 구현 -> 웹 계층과 상호작용 (API 포함)

<br><br>

## 1. 요구사항 분석

**내용이 조금 수정되어서 다를 수 있음**

![image](https://user-images.githubusercontent.com/80165014/236459680-a1ac2775-9f12-4c0f-9c2b-4171aa1baa50.png)

<br><br>

## 2. 구체적인 요구사항 목록

**내용이 조금 수정되어서 다를 수 있음**

![image](https://user-images.githubusercontent.com/105353163/243077791-cb3ef086-fbb6-42bc-bf60-d346905a5fb5.png) 

<br>

![image](https://user-images.githubusercontent.com/105353163/243077817-d405819e-5684-4def-a647-e8759e04b958.png) 

<br><br>

## 3. 설계시작

**내용이 조금 수정되어서 다를 수 있음**



![image](https://user-images.githubusercontent.com/105353163/243077831-b869af6d-4c9b-4a9b-a8d8-5ceae6f38fbd.png) 

<br>

### 3-1. 도메인 모델 분석

**내용이 조금 수정되어서 다를 수 있음**

![image](https://user-images.githubusercontent.com/105353163/243077840-c16f7956-a82d-478b-8534-907c959e6466.png) 

<br>

### 3-2. 테이블 설계

**내용이 조금 수정되어서 다를 수 있음**

![image](https://user-images.githubusercontent.com/105353163/243077849-947a0afc-2439-4306-a092-fcb32dab8bc1.png) 

<br>

### 3-3. 엔티티 설계

**내용이 조금 수정되어서 다를 수 있음**

![image](https://user-images.githubusercontent.com/105353163/243077854-3bedda7f-726b-42e0-8cf6-9034f5aa93a8.png) 

<br>

### 3-4. ERDCloud

**여긴 제대로 정리**

- 사용 Table: (11개) Member, Profile, Lists, Task, TaskStatus, Character, CharacterItem, Exp, Follow, Notification, Item(상점대용.독립관계)  
- Timer 생략: 타이머 부분은 클라에서 타임시간 받으면 경험치 업뎃

[![ERDCloud](https://user-images.githubusercontent.com/80165014/236461896-b89f1ef7-660c-4f3f-8a92-842adb3db44c.png)](https://www.erdcloud.com/d/ZThXeWGTiKuj23yzT "ERDCloud로 이동하기")

<br><br>

# 기능들 로직

## 1. 로그인 처리 로직

**토큰방식이 아닌 쿠키 세션 방식을 사용 (쿠키 세션방식 공부 목적)**

* 맨 처음에 로그인을 하면 서버에서 세션Id를 담은 쿠키를 클라에 응답으로 준다.
* 클라는 요청시 항상 쿠키에 세션Id가 포함되어 전달하게되고,  
  서버는 전달받은 쿠키 정보로 "세션 저장소"를 조회해서 회원임을 인증한다 => 메모리에 "세션 저장소(톰캣이 관리)" 존재

<br>

**<클라이언트>**

* **클라 상에서 로그인 기록 있으면 "소셜 로그인" 화면 없이 그냥 바로 => 서버로 uid 전송**
  * API 주소는 "로그인" 주소를 준다.
* **클라 상에서 로그인 기록 없으면 "소셜 로그인" 화면 및 로그인 시도 => 이때, 서버로 uid 전송**
  * API 주소는 "회원가입" 주소를 준다.
* **클라 상에서 로그아웃을 요청하는 경우는 쿠키 정보가 있어서 바로 API 호출 및 "소셜 로그인" 화면으로 이동.**
  * 물론, 클라 상의 로그인 기록도 꼭 지워줘야 나중에 "회원가입"으로 잘 넘어감.
  * API 주소는 "로그아웃" 주소를 준다.
* **마지막으로 앱의 종료 이벤트때 "로그아웃 API"  호출 코드를 작성한다. => 사용X**
  * onDestroy같이 앱 종료 이벤트때 웹뷰에서 얻은 쿠키정보를 request에 담아서 로그아웃 API 호출해주기
  * **=> 정정 : 그냥 쿠키 만료 시간을 설정해두겠다.**

<br>

**<서버>**

* **로그인 API => 받은 uid로 회원판단 시도!! ("회원 저장소"에서 확인!!)**
  - **회원 이라면,**
    * `HttpSession` 로 세션Id를 생성해서 "회원 저장소"에서 받은 회원정보(=memberA)와 함께 "세션 저장소"에 기록

    * 세션Id를 응답 쿠키로 전달

  - **회원 아니라면,** 
    - 회원이 아니라고 클라에게 전송 (클라는 위 <클라>파트의 2번 카테고리를 행하면 됨)


* **회원가입 API => 받은 uid로 회원판단 시도!! ("회원 저장소"에서 확인!!)**
  - **첫 가입 회원이면,**
    * uid, 기타정보 등등을 "회원 저장소"에 기록하고, `HttpSession`로 세션Id를 생성해서 회원정보(=memberA)와 함께 "세션 저장소"에 기록

    * 세션Id를 응답 쿠키로 전달

  - **이미 가입한 중복 회원이면,**
    - 이미 회원이라고 클라에게 전송 (클라는 위 <클라>파트의 1번 카테고리를 행하면 됨)


* **로그아웃 API**
  - 쿠키 정보에 세션Id를 활용해서 해당 세션을 "세션 저장소"에서 제거

<br><br>

## 2. 플래너 기능

**기본적인 CRUD 방식 구현**

* 플래너의 모든 일정(lists) 조회
* 플래너의 날짜별 일정(lists) 조회
* 일정 하루단위(lists) 삭제
* 일정1개(task) 추가
  * 자동으로 해당 날짜에 맞는 lists에 매핑해서 일정 추가
* 일정1개(task) 삭제
* 일정1개(task) 수정

<br><br>

## 3. 나머지 기능 - 캐릭터

API 명세서 작성했으니 기능들 자세한건 이곳을 참고

**API 명세서 참고 : https://documenter.getpostman.com/view/21970313/2s93mBwec5**

<br><br>

# Folder Structure

**API 명세서 : https://documenter.getpostman.com/view/21970313/2s93mBwec5**

<br>

* **[`/lepl/src/main/resources/application.yml`](./lepl/src/main/resources/application.yml)**
  * **db연결 등등 환경 설정**
* **[`/lepl/src/main/java/com/lepl/leplApplication.java`](./lepl/src/main/java/com/lepl/leplApplication.java)**
  * **제일 최상의 루트 파일이며, 이 파일을 실행해서 서버를 오픈**
* **[`/lepl/src/main/java/com/lepl/aop/TimeTraceAop.java`](./lepl/src/main/java/com/lepl/aop/TimeTraceAop.java)**
  * **AOP 설정 - 성능 테스트 위해 "함수 실행시간 측정 AOP"**

* **[`/lepl/src/main/java/com/lepl/api/argumentresolver/Login.java`](./lepl/src/main/java/com/lepl/api/argumentresolver/Login.java)**
  * **위에서 언급한 @Login 어노테이션을 등록하기 위한 인터페이스**
  * @Target, @Retention 어노테이션 활용
  * **[`/lepl/src/main/java/com/lepl/api/argumentresolver/LoginMemberArgumentResolver.java`](./lepl/src/main/java/com/lepl/api/argumentresolver/LoginMemberArgumentResolver.java)**
    * `HandlerMethodArgumentResolver` 인터페이스를 구현해서 `supportsParameter, resolveArgument` 를 오버라이딩
    * supportsParameter 함수가 true 면 resolveArgument 함수를 실행하는 구조
      * **@Login 어노테이션인지 판단하고 true면 세션에서 memberId 구해 옴**
* **[`/lepl/src/main/java/com/lepl/api/interceptor/MemberCheckInterceptor.java`](./lepl/src/main/java/com/lepl/api/interceptor/MemberCheckInterceptor.java)**
  * **인터셉터 등록할 클래스**
  * `HandlerInterceptor` 인터페이스를 구현해서 `preHandle` 을 오버라이딩
  * 컨트롤러 접근전에 인터셉터에서 사용자인지 인증을 한다.
* **[`/lepl/src/main/java/com/lepl/api/member/MemberApiController.java`](./lepl/src/main/java/com/lepl/api/member/MemberApiController.java)**
  * **컨트롤러(api) - 회원 관련**
  * **자세한 것은 API 명세서 확인**
* **[`/lepl/src/main/java/com/lepl/api/task/TaskApiController.java`](./lepl/src/main/java/com/lepl/api/task/TaskApiController.java)**
  * **컨트롤러(api) - 일정 관련**
  * **자세한 것은 API 명세서 확인**
  * [`/lepl/src/main/java/com/lepl/api/task/ListsApiController.java`](./lepl/src/main/java/com/lepl/api/task/ListsApiController.java)
    * 일정에서 Lists 관련
* **[`/lepl/src/main/java/com/lepl/api/character/CharacterItemApiController.java`](./lepl/src/main/java/com/lepl/api/character/CharacterItemApiController.java)**
  * **컨트롤러(api) - 캐릭터 관련**
  * **자세한 것은 API 명세서 확인**
    * **[`/lepl/src/main/java/com/lepl/api/character/ExpApiController.java`](./lepl/src/main/java/com/lepl/api/character/ExpApiController.java)**
      * 캐릭터의 경험치 관련
    * **[`/lepl/src/main/java/com/lepl/api/character/FollowApiController.java`](./lepl/src/main/java/com/lepl/api/character/FollowApiController.java)**
      * 캐릭터의 팔로잉 관련
    * **[`/lepl/src/main/java/com/lepl/api/character/ItemApiController.java`](./lepl/src/main/java/com/lepl/api/character/ItemApiController.java)**
      * 아이템 관련
    * **[`/lepl/src/main/java/com/lepl/api/character/NotificationApiController.java`](./lepl/src/main/java/com/lepl/api/character/NotificationApiController.java)**
      * 알림 관련
* **[`/lepl/src/main/java/com/lepl/config/ApiConfig.java`](./lepl/src/main/java/com/lepl/config/ApiConfig.java)**
  * **설정파일(설정 4개정도 추가) - @Configuration 사용**
  * **`WebMvcConfigurer` 인터페이스 구현해서 오버라이딩(`addArgumentResolvers, addInterceptors, addResourceHandlers, addCorsMappings`) 사용**
    * **`addArgumentResolvers` 의 경우 `HandlerMethodArgumentResolver` 를 추가**
      * `LoginMemberArgumentResolver` 는 `HandlerMethodArgumentResolver` 인터페이스를 구현
      * @Login 어노테이션을 등록 및 해당 어노테이션을 구현함
      * **간단히 @Login 어노테이션을 사용해서 세션 메모리에 등록된 memberId를 얻기 위함**
    * **`addInterceptors` 의 경우 인터셉터(클라 요청을 가로챔)를 등록 `HandlerInterceptor` 를 추가**
      * `MemberCheckInterceptor` 는 `HandlerInterceptor` 인터페이스를 구현
      * **회원인지 인증하기 위함**
      * 모든 경로(`"/**"`)에 대해 실행되며, 
      * 몇 가지 경로(`"/", "/api/v1/members/login", "/api/v1/members/register", "/api/v1/members/logout", "/css/**", "/*.ico", "/error"`)는 인터셉터가 적용되지 않도록 설정
    * `addResourceHandlers` 의 경우 정적이미지 경로 핸들링 + 브라우저 캐시 적용
    * `addCorsMappings` 의 경우 CORS 해결
  * **[`/lepl/src/main/java/com/lepl/config/CacheConfig.java`](./lepl/src/main/java/com/lepl/config/CacheConfig.java)**
    * `CacheManager` 2개 구현 - 서버단 메모리 캐시 사용 커스텀
* **[`/lepl/src/main/java/com/lepl/domain/character/Character.java`](./lepl/src/main/java/com/lepl/domain/character/Character.java)**
  * **도메인(엔티티) - 캐릭터 관련**
  * [`/lepl/src/main/java/com/lepl/domain/character/CharacterItem.java`](./lepl/src/main/java/com/lepl/domain/character/CharacterItem.java)
    * 캐릭터가 가진 아이템 목록
  * [`/lepl/src/main/java/com/lepl/domain/character/Exp.java`](./lepl/src/main/java/com/lepl/domain/character/Exp.java)
    * 경험치
  * [`/lepl/src/main/java/com/lepl/domain/character/Follow.java`](./lepl/src/main/java/com/lepl/domain/character/Follow.java)
    * 팔로우
  * [`/lepl/src/main/java/com/lepl/domain/character/Item.java`](./lepl/src/main/java/com/lepl/domain/character/Item.java)
    * 아이템
  * [`/lepl/src/main/java/com/lepl/domain/character/Notification.java`](./lepl/src/main/java/com/lepl/domain/character/Notification.java)
    * 알림
* **[`/lepl/src/main/java/com/lepl/domain/member/Member.java`](./lepl/src/main/java/com/lepl/domain/member/Member.java)**
  * **도메인(엔티티) - 회원 관련**
  * uid(회원 로그인용)가 필수
  * 프로필, 캐릭터, 일정들 관계

  * [`/lepl/src/main/java/com/lepl/domain/member/Profile.java`](./lepl/src/main/java/com/lepl/domain/member/Profile.java)
    * 회원 프로필(정보)
* **[`/lepl/src/main/java/com/lepl/domain/task/Task.java`](./lepl/src/main/java/com/lepl/domain/task/Task.java)**

  * **도메인(엔티티) - 일정(플래너) 관련**

  * [`/lepl/src/main/java/com/lepl/domain/task/TaskStatus.java`](./lepl/src/main/java/com/lepl/domain/task/TaskStatus.java)
    * 상태 : 일정완료 유무, 타이머사용 유무
  * [`/lepl/src/main/java/com/lepl/domain/task/Lists.java`](./lepl/src/main/java/com/lepl/domain/task/Lists.java)
    * 날짜별로 Task(일정)들을 하나로(Lists) 묶은 구조
  * [`/lepl/src/main/java/com/lepl/domain/task/timer/Timer.java`](./lepl/src/main/java/com/lepl/domain/task/timer/Timer.java)
    * 일정(플래너)의 타이머 기능 관련 -> 삭제했음. 클라로 책임전가
    * 하나의 Task(일정)을 타이머 사용시 활용
    * 타이머 시작과 끝시간을 기록
    * [`/lepl/src/main/java/com/lepl/domain/task/timer/TimerStatus.java`](./lepl/src/main/java/com/lepl/domain/task/timer/TimerStatus.java)
      * 상태 : ALLOW, FOCUS (허용앱 사용상태와 집중상태로 구분)
* **[`/lepl/src/main/java/com/lepl/Repository/member/MemberRepository.java`](./lepl/src/main/java/com/lepl/Repository/member/MemberRepository.java)**
  * **레퍼지토리 - 회원 관련**
  * save, findOne, findByUid, findAllWithPage 기능
* **[`/lepl/src/main/java/com/lepl/Repository/task/TaskRepository.java`](./lepl/src/main/java/com/lepl/Repository/task/TaskRepository.java)**
  * **레퍼지토리 - 일정 관련**
  * save, findOne, findAll, findOneWithMember, remove, updateAll 기능
  * [`/lepl/src/main/java/com/lepl/Repository/task/TaskStatusRepository.java`](./lepl/src/main/java/com/lepl/Repository/task/TaskStatusRepository.java)
  * [`/lepl/src/main/java/com/lepl/Repository/task/ListsRepository.java`](./lepl/src/main/java/com/lepl/Repository/task/ListsRepository.java)
    * 일정에서 Lists 관련
    * save, findByCurrent, findAllWithMemberTask, findByDateWithMemberTask, findOneWithMemberTask, remove 기능
* **[`/lepl/src/main/java/com/lepl/Repository/character/CharacterRepository.java`](./lepl/src/main/java/com/lepl/Repository/character/CharacterRepository.java)**
  * **레퍼지토리 - 캐릭터 관련**
  * save, findOne, remove, findCharacterWithMember 기능
  * [`/lepl/src/main/java/com/lepl/Repository/character/CharacterItemRepository.java`](./lepl/src/main/java/com/lepl/Repository/character/CharacterItemRepository.java)
    * 캐릭터 아이템 관련
    * save, findOne, findAll, remove, findAllWithMemberItem, updateStatus 기능
  * [`/lepl/src/main/java/com/lepl/Repository/character/ExpRepository.java`](./lepl/src/main/java/com/lepl/Repository/character/ExpRepository.java)
    * 경험치 관련
    * save, findOne, remove, findOneWithMember, initPointToday 기능
  * [`/lepl/src/main/java/com/lepl/Repository/character/FollowRepository.java`](./lepl/src/main/java/com/lepl/Repository/character/FollowRepository.java)
    * 팔로우 관련
    * save, findOne, findAll, findById, remove, findAllWithFollowing, findAllWithFollower 기능
  * [`/lepl/src/main/java/com/lepl/Repository/character/ItemRepository.java`](./lepl/src/main/java/com/lepl/Repository/character/ItemRepository.java)
    * 아이템 관련
    * save, findOne, findAll, remove, findByName, updatePurchase 기능
  * [`/lepl/src/main/java/com/lepl/Repository/character/NotificationRepository.java`](./lepl/src/main/java/com/lepl/Repository/character/NotificationRepository.java)
    * 알림 관련
    * save, findOne, findAll, remove, findAllWithCharacter 기능
* **[`/lepl/src/main/java/com/lepl/Service/member/MemberService.java`](./lepl/src/main/java/com/lepl/Service/member/MemberService.java)**
  * **서비스 - 회원 관련**
  * join(중복검증 포함), findOne, findByUid, {findAllWithPage, initCacheMembers}(=회원 최신순 조회+캐시) 기능
* **[`/lepl/src/main/java/com/lepl/Service/task/TaskServiceV2.java`](./lepl/src/main/java/com/lepl/Service/task/TaskServiceV2.java)**
  * **서비스 - 일정 관련** -> 모니터링 추가(비지니스 로직)
  * join, findOne, findOneWithMember, remove, update(일정수정), updateState(일정상태수정), updateAll(일정수정여러개한번에)
  * [`/lepl/src/main/java/com/lepl/Service/task/config/TaskConfigV2.java`](./lepl/src/main/java/com/lepl/Service/task/config/TaskConfigV2.java)
    * **모니터링(비지니스 로직) 위해 추가 설정 -> @Timed 사용 위해**
  * [`/lepl/src/main/java/com/lepl/Service/task/TaskStatusService.java`](./lepl/src/main/java/com/lepl/Service/task/TaskStatusService.java)
  * [`/lepl/src/main/java/com/lepl/Service/task/ListsService.java`](./lepl/src/main/java/com/lepl/Service/task/ListsService.java)
    * 일정에서 Lists 관련
    * findAllWithMemberTask(멤버일정모두찾기), findByDateWithMemberTask(멤버일정날짜로찾기), join, findByCurrent(현재날짜로찾기), findOneWithMemberTask, remove, updateTime(타이머종료시경험치업뎃)
* **[`/lepl/src/main/java/com/lepl/Service/character/CharacterService.java`](./lepl/src/main/java/com/lepl/Service/character/CharacterService.java)**
  * **서비스 - 캐릭터 관련**
  * join, findOne, findCharacterWithMember, remove
  * [`/lepl/src/main/java/com/lepl/Service/character/CharacterItemService.java`](./lepl/src/main/java/com/lepl/Service/character/CharacterItemService.java)
    * **서비스 - 캐릭터 아이템 관련**
    * join, findOne, findAll, remove, findAllWithMemberItem(사용자소유아이템전체조회), updateStatus(아이템착용여부업뎃)
  * [`/lepl/src/main/java/com/lepl/Service/character/ExpService.java`](./lepl/src/main/java/com/lepl/Service/character/ExpService.java)
    * **서비스 - 경험치 관련**
    * join, findOne, findOneWithMember, remove, update, initPointToday(하루제한경험치량초기화)
  * [`/lepl/src/main/java/com/lepl/Service/character/FollowService.java`](./lepl/src/main/java/com/lepl/Service/character/FollowService.java)
    * **서비스 - 팔로우 관련**
    * join, validateDuplicateFollow(중복검증), findOne, findAll, remove, findAllWithFollowing(팔로잉조회), findAllWithFollower(팔로워조회)
  * [`/lepl/src/main/java/com/lepl/Service/character/ItemService.java`](./lepl/src/main/java/com/lepl/Service/character/ItemService.java)
    * **서비스 - 아이템 관련**
    * save, findOne, findAll, remove, findByName, updatePurchase(상품구매량=재고수)
  * [`/lepl/src/main/java/com/lepl/Service/character/NotificationService.java`](./lepl/src/main/java/com/lepl/Service/character/NotificationService.java)
    * **서비스 - 알림 관련**
    * join, findOne, findAll, remove, findAllWithCharacter

- **[`/lepl/src/main/java/com/lepl/util/ApiControllerAdvice.java`](./lepl/src/main/java/com/lepl/util/ApiControllerAdvice.java)**
  * **Util - 컨트롤러 예외(Exception)처리 관련**
  * **[`/lepl/src/main/java/com/lepl/util/ApiResponse.java`](./lepl/src/main/java/com/lepl/util/ApiResponse.java)**
    * **Util - API 응답 양식 지정**
    * 컨트롤러에서 Valid 사용 + 제네릭(타입문제 해결) 목적
  * **[`/lepl/src/main/java/com/lepl/util/ErrorResult.java`](./lepl/src/main/java/com/lepl/util/ErrorResult.java)**
    * **Util - API 예외처리 응답 양식 관련**
    * 말 그대로 예외(Exception)처리 관련
  * **[`/lepl/src/main/java/com/lepl/util/Messages.java`](./lepl/src/main/java/com/lepl/util/Messages.java)**
    * **Util - 메시지 관련**
    * 메시지 국제화 관리하게 messages.properties 를 활용하면 되긴 하지만 그냥 이걸로 끝냈다.
