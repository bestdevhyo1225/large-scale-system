# 대규모 시스템 설계 기초

## :pushpin: 참고 서적

- [가상 면접 사례로 배우는 대규모 시스템 설계 기초](http://www.yes24.com/Product/Goods/102819435)

## :pushpin: 시스템 설계 리스트

- [SNS 피드 시스템 설계](https://github.com/bestdevhyo1225/large-system-design#pushpin-sns-%ED%94%BC%EB%93%9C-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%84%A4%EA%B3%84)
- [쿠폰 이벤트 선착순 시스템 설계](https://github.com/bestdevhyo1225/large-system-design#pushpin-%EC%BF%A0%ED%8F%B0-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%84%A4%EA%B3%84)

## :pushpin: SNS 피드 시스템 설계

### :white_check_mark: 소스 코드

- [large-scale-system:sns-feed-service-v2](https://github.com/bestdevhyo1225/large-system-design/tree/main/sns-feed-service-v2)
- [kotlin-multi-module-template](https://github.com/bestdevhyo1225/kotlin-multi-module-template) (`2023.06.27 ~ 진행중` -
  다시 한 번 만들어보고 있음)

### :white_check_mark: SNS 피드 서비스 아키텍처

<img width="1000" alt="image" src="https://user-images.githubusercontent.com/23515771/208303790-ec90177c-87d0-42fe-9a34-f964ff0650be.png">

### :white_check_mark: 팬 아웃 Push 모델

#### 프로세스

1. `Post` 를 데이터베이스에 저장한다.
2. `Post 캐시`, `PostIdsByMemberId 캐시(회원이 등록한 Post Id 리스트)` 를 레디스에 캐싱한다.
    - `PostIdsByMemberId` 는 `Sorted Set` 컬렉션을 사용하여, `등록순` 으로 `postId` 를 캐싱한다.
3. `Post` 를 생성한 회원의 `팔로워 Id 리스트` 를 조회한다.
4. `Feed` 이벤트를 Kafka 브로커로 전송한다.
5. `Event Worker` 서버에서는 Kafka 브로커에서 `Feed` 이벤트를 수신한다.
6. `Feed 캐시` 를 `팔로워` 의 피드에 `등록순` 으로 `memberId, postId` 를 캐싱한다.

### :white_check_mark: Post 캐시

#### 전략

- `post:ids:member:$memberId` Key에 `postId` 를 저장한다.
    - 만료 시간은 `30일` 을 부여한다.
    - Post 페이지네이션을 활용하기 위해 `memberId` 기준으로 `postId` 를 `SortedSet` 자료구조에 적재한다.
- `post:$id` Key에는 Post 캐시를 저장한다.
    - 만료 시간은 `12시간` 을 부여한다.
    - 단 건 조회시, `get` 명령을 사용한다.
    - 여러 건 요청시, `mget` 명령어를 사용한다.

#### 캐시 메모리 계산 결과

- `240,000` 건의 캐시를 저장하는데, `92.3 MB` 를 사용함.
    - `1 GB` 의 경우, 대략 `2,400,000` 건의 Post 관련 캐시를 저장할 수 있음.
    - `10 GB` 의 경우, 대략 `24,000,000` 건의 Post 관련 캐시를 저장할 수 있음.
    - `32 GB` 의 경우, 대략 `76,800,000` 건의 Post 관련 캐시를 저장할 수 있음.

### :white_check_mark: Feed 캐시

#### 전략

- `member:$id:feeds` Key에 `postId` 를 저장한다.
    - `SortedSet` 자료구조를 사용하여, 등록순으로 저장한다.
    - 최대 `800` 개만 저장이 가능하다.
    - `800` 개가 넘어가는 경우, `zremRangeByRank` 을 통해 가장 오래된 `postId` 1개를 제거한다.
    - 마지막 로그인 날짜가 `30일` 이 지난 회원들의 경우, 데이터를 삭제한다.
        - `Feed` 캐시의 경우, `팔로잉` 한 사람이 등록할 때마다 만료시간이 갱신되기 때문에 만료시간을 부여하지 않았다.

### :white_check_mark: Redis 메모리 최적화 관련 참고

- 캐시 데이터가 억 단위를 넘어가는
  경우, [Redis에 심플한 key-value 로 수 억개의 데이터 저장하기](https://charsyam.wordpress.com/2011/11/06/redis%EC%97%90-%EC%8B%AC%ED%94%8C%ED%95%9C-key-value-%EB%A1%9C-%EC%88%98-%EC%96%B5%EA%B0%9C%EC%9D%98-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A0%80%EC%9E%A5%ED%95%98%EA%B8%B0/)
  을 참고해서 `Hashes` 자료구조로 개선하자.

### :white_check_mark: 성능 테스트

#### 인스턴스 사양

> **Amazon MSK**

- `kafka.t3.small`
    - `vCpu` : `2048`
    - `메모리(GiB)` : `2G`
    - `네트워크 대역폭 (Gbps)` : `5 Gbps`

> **Amazon Elasticache**

- `cache.t3.small`
    - `메모리(GiB)` : `1.37 GiB`
    - `Up to 5 Gigabit nework perfomance`

> **AWS ECS Fargate**

- `vCpu` : `2048` or `4096`
- `Memory` : `6G` or `8G`

> **Spring Boot Application**

- `Xms` : `4096MB (4G)`
- `Xmx` : `4096MB (4G)`
- `GC` : `ZGC`

#### API 서버 성능 테스트 결과

- 사용자 수 : `2,000명`
- Ramp Up 시간 (초) : `1초`
- 테스트 지속 시간 : `60초`

|  라벨   |  표본 수  | 평균(ms)  | 최소값(ms) | 최대값(ms) | 오류 (%) |    처리량     |
|:-----:|:------:|:-------:|:-------:|:-------:|:------:|:----------:|
| 회원 생성 | 79,690 | 1,495ms |  52ms   | 4,831ms | 0.00%  | 1293.7/sec |

|   라벨   |  표본 수  | 평균(ms)  | 최소값(ms) | 최대값(ms)  | 오류 (%) |    처리량    |
|:------:|:------:|:-------:|:-------:|:--------:|:------:|:---------:|
| 게시물 생성 | 43,119 | 2,822ms |  89ms   | 75,006ms | 0.05%  | 569.5/sec |

#### Query API 서버 성능 테스트 결과 (1)

- 사용자 수 : `2,000명`
- Ramp Up 시간 (초) : `1초`
- 테스트 지속 시간 : `60초`
- RateLimiter 의 limitForPeriod : `3,000`
- AWS ECS 인스턴스 수 : `1대`
- Tomcat Max Thread : `150`
- 게시물 리스트, 타임라인, 타임라인 새로고침, 상세 조회 총계

| 라벨 |  표본 수   | 평균(ms) | 최소값(ms) | 최대값(ms)  | 오류 (%) |    처리량     |
|:--:|:-------:|:------:|:-------:|:--------:|:------:|:----------:|
| 총계 | 191,195 | 622ms  |  20ms   | 66,046ms | 0.01%  | 2821.1/sec |
| 총계 | 160,074 | 737ms  |  17ms   | 48,889ms | 0.03%  | 2606.3/sec |
| 총계 | 170,529 | 696ms  |  10ms   | 75,003ms | 0.01%  | 2710.7/sec |

#### Query API 서버 성능 테스트 결과 (2)

- 사용자 수 : `2,000명`
- Ramp Up 시간 (초) : `1초`
- 테스트 지속 시간 : `60초`
- RateLimiter 의 limitForPeriod : `3,000`
- AWS ECS 인스턴스 수 : `2대`
- Tomcat Max Thread : `150`
- 게시물 리스트, 타임라인, 타임라인 새로고침, 상세 조회 총계

| 라벨 |  표본 수   | 평균(ms) | 최소값(ms) | 최대값(ms)  | 오류 (%) |    처리량     |
|:--:|:-------:|:------:|:-------:|:--------:|:------:|:----------:|
| 총계 | 298,464 | 396ms  |   9ms   | 60,014ms | 0.00%  | 4925.9/sec |
| 총계 | 304,285 | 388ms  |  10ms   | 59,919ms | 0.00%  | 5017.3/sec |
| 총계 | 309,815 | 286ms  |  10ms   | 60,473ms | 0.00%  | 5109.6/sec |

#### Query API 서버 성능 테스트 결과 (임시)

- 사용자 수 : `2,000명`
- Ramp Up 시간 (초) : `1초`
- 테스트 지속 시간 : `60초`
- RateLimiter 의 limitForPeriod : `3,000`
- AWS ECS 인스턴스 수 : `1대`
- Tomcat Max Thread : `200`
    - 스레드 수가 늘어나도 성능이 좋아짐을 보장할 수
      없다. ([스레드 컨텍스트 스위칭](https://www.inflearn.com/questions/252332/%EC%8A%A4%EB%A0%88%EB%93%9C-%EC%BB%A8%ED%85%8D%EC%8A%A4%ED%8A%B8-%EC%8A%A4%EC%9C%84%EC%B9%AD))
    - 같은 4코어의 CPU를 사용한다면, 스레드 풀이 늘어날 수록 컨텍스트 스위칭 비용은 늘어난다.
        - `200` 개의 스레드 / `4` 코어 = `50` 번의 컨텍스트 스위칭
        - `150` 개의 스레드 / `4` 코어 = `37.5` 번의 컨텍스트 스위칭
- 게시물 리스트, 타임라인, 타임라인 새로고침, 상세 조회 총계

| 라벨 |  표본 수   | 평균(ms) | 최소값(ms) | 최대값(ms)  | 오류 (%) |    처리량     |
|:--:|:-------:|:------:|:-------:|:--------:|:------:|:----------:|
| 총계 | 161,657 | 735ms  |  15ms   | 67,120ms | 0.00%  | 2340.7/sec |

### :white_check_mark: 정리

`QUERY API (Read 서버)` 의 `테스트 결과 (1)`, `테스트 결과 (2)` 에 따르면, 인스턴스 `1대` 당 평균 `2,700.0/sec` TPS의 성능 지표를 보이고 있으며, `2대` 의
경우 `5,000.0/sec` TPS 성능 지표를 보이고 있다. 만약 `50,000.0/sec` TPS를 처리해야 한다면, `20대 이상` 의 서버를 구축해야한다.

## :pushpin: 쿠폰 이벤트 선착순 시스템 설계

### :white_check_mark: 소스 코드

- [coupon-service-v2](https://github.com/bestdevhyo1225/large-system-design/tree/main/coupon-service-v2)

### :white_check_mark: 쿠폰 서비스 아키텍처 - V2

<img width="1834" alt="image" src="https://user-images.githubusercontent.com/23515771/197327876-5b53d114-a32f-4fe5-b346-1d20e7a5973d.png">

#### Write Through 패턴 사용

- 선착순의 경우, DB에 갑작스런 쓰기 요청이 몰리게 되면 DB 서버가 죽을 수도 있다. 따라서 임시적으로 Redis 서버에 캐시 데이터를 적재하고 Kafka를 활용해서 DB에 쓰기 작업을 처리한다.
