# 대규모 시스템 설계 기초

## :pushpin: 참고 서적

- [가상 면접 사례로 배우는 대규모 시스템 설계 기초](http://www.yes24.com/Product/Goods/102819435)

## :pushpin: 시스템 설계 리스트

- :white_check_mark: [URL 단축기 설계](https://github.com/bestdevhyo1225/large-system-design#pushpin-url-%EB%8B%A8%EC%B6%95%EA%B8%B0-%EC%84%A4%EA%B3%84)
- :white_check_mark: [SNS 피드 시스템 설계](https://github.com/bestdevhyo1225/large-system-design#pushpin-sns-%ED%94%BC%EB%93%9C-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%84%A4%EA%B3%84)
    - [sns-feed-service-v2 소스 코드](https://github.com/bestdevhyo1225/large-system-design/tree/main/sns-feed-service-v2)
- :white_check_mark: [쿠폰 이벤트 선착순 시스템 설계](https://github.com/bestdevhyo1225/large-system-design#pushpin-%EC%BF%A0%ED%8F%B0-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%84%A4%EA%B3%84)
    - [coupon-service-v2 소스 코드](https://github.com/bestdevhyo1225/large-system-design/tree/main/coupon-service-v2)

## :pushpin: URL 단축기 설계

### :white_check_mark: 요구 사항

- 긴 `URL` 을 단축해서 짧은 길이의 `URL` 을 사용자에게 제공해야 한다.
- 매일 `1억` 개의 단축기를 만들어야 한다.
- 단축 `URL` 에는 `숫자(0 ~ 9)`, `영문자(a ~ z, A ~ Z)` 까지만 사용할 수 있다.
- 시스템 단순화를 위해 갱신, 삭제는 할 수 없다.

### :white_check_mark: 개략적 추정

- `쓰기 연산` : 매일 `1억` 개의 단축 `URL` 생성
- `초당 쓰기 연산` : `1억 / 24시간 / 3600초 = 1,160회` 발생한다.
- `읽기 연산` : 읽기 연산과 쓰기 연산의 비율을 `10 : 1` 로 가정했을때, 초당 `11,600회` 발생한다.
- 10년간 `URL` 단축 서버 운영하게 된다면, `1억 x 356일 x 10년 = 3,650억` 개의 레코드를 보관해야 한다.
    - `n = 7` 인 경우, `3.5조` 의 해시 값을 만들 수 있다.
    - `n = 8` 인 경우, `218조` 의 해시 값을 만들 수 있다.
- 단축 전 `URL` 의 평균 길이를 `100` 이라 가정했을때, 10년 동안 필요한 저장 용량은 `3,650억 x 100바이트 = 36.5TB` 이다.

### :white_check_mark: 부하 테스트 결과 (JMeter)

- `Number Of Threads (users)` : 접근할 사용자의 수 (스레드 수)
- `Ramp-up Period (seoncds)` : 위에서 설정한 사용자 수의 도달할 때까지 걸리는 시간
    - 예를 들어, `Number of Threads` 값이 `1,000` 이고, `Ramp-Up period` 값이 `10` 일 때, 1,000명의 사용자(Thread)를 생성할 때 까지 10초가 걸린다는
      의미이다. 즉, `1초 동안
      100명의 유저가 요청을 한다는 뜻이다.`
- `Samples`: 테스트 서버로 보낸 요청의 수
- `Average` : 평균 응답 시간(ms)
- `Min` : 최소 응답 시간(ms)
- `Max` : 최대 응답 시간(ms)
- `Throughput` : 단위 시간당 대상 서버에서 처리되는 요청의 수 (JMeter에서는 시간 단위를 보통 `TPS(Transaction Per Second)` 로 표현)

#### :arrow_forward: 단일 모듈 서버에서 실행한 결과

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| URL 조회 요청 | 25,003회 | 990ms | 11ms | 2,379ms | 0.00% | 411.6/sec |
| URL 생성 요청 | 24,495회 | 1,439ms | 409ms | 3,714ms | 0.00% | 408.1/sec |

#### :arrow_forward: Command, Query 모듈 서버를 분리한 결과

- `Number Of Threads (users)` : 1,000명, `Ramp-up Period (seoncds)` : 1초

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| URL 조회 요청 | 644,924회 | 89ms | 9ms | 1,109ms | 0.00% | 10723.4/sec |
| URL 생성 요청 | 50,774회 | 1,182ms | 20ms | 2,743ms | 0.00% | 830.1/sec |

#### :arrow_forward: Query 모듈 서버 결과

- `Number Of Threads (users)` : 2,000명, `Ramp-up Period (seoncds)` : 1초
- Redis 요청시, `Circuit Breaker` 적용 (fallback 처리)

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| URL 조회 요청 | 1,294,603회 | 184ms | 9ms | 1,469ms | 0.00% | 10742.2/sec |

- `Number Of Threads (users)` : 2,000명, `Ramp-up Period (seoncds)` : 1초
- `WebFlux`, `R2DBC`, `Redis Reactive` 적용

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| URL 조회 요청 | 1,413,954회 | 168ms | 3ms | 1,385ms | 0.00% | 11760.6/sec |

#### :arrow_forward: Command 모듈 서버 결과

- `Number Of Threads (users)` : 1,500명, `Ramp-up Period (seoncds)` : 1초
- 단축 URL이 중복으로 생성되는 문제가 있음.

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| URL 생성 요청 | 72,116회 | 2,508ms | 18ms | 4,303ms | 0.00% | 588.1/sec |

- `Number Of Threads (users)` : 1,500명, `Ramp-up Period (seoncds)` : 1초
- 단축 URL 기능에서 암호화 URL 기능으로 변경했음.
- 중복 생성되는 URL를 막기 위해 `Redisson` 클라이언트를 활용한 `분산 락` 을 통해 동시성 문제를 보완했음.

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| URL 생성 요청 | 29,256회 | 6,277ms | 30ms | 8,350ms | 0.00% | 231.8/sec |

- `Number Of Threads (users)` : 1,500명, `Ramp-up Period (seoncds)` : 1초
- 분산 락의 성능이 좋지 않아서, `Unique 컬럼` 을 지정하여, 중복 발생시 조회 처리하는 방식으로 해결했음.

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| URL 생성 요청 | 79,369회 | 2,270ms | 21ms | 3,410ms | 0.00% | 651.7/sec |

### :white_check_mark: 정리

`단일 모듈 서버` 에서는 `411.6 TPS` 의 쓰기 작업, `408.1 TPS` 의 읽기 작업이 처리된다. 대략 `1대의 서버` 가 `800 TPS` 정도의 성능을 내는데, 초당 `11,600회` 의 처리
성능을 감당하려면, `최소 15대의 서버` 가 필요하다.

CQRS 패턴을 적용한 `Command, Query` 모듈 서버에서는 `651.7 TPS` 의 쓰기 작업, `11760.6 TPS` 의 읽기 작업이 처리된다. 이를 통해 필요한 서버의 수를 측정한다면, 쓰기 작업을
수행하는 `Command` 서버는 `최소 2대의 서버` 가 필요하며, `Query` 서버도 `최소 2대 서버` 가 필요하다.

## :pushpin: SNS 피드 시스템 설계

### :white_check_mark: 소스 코드

- [sns-feed-service-v2](https://github.com/bestdevhyo1225/large-system-design/tree/main/sns-feed-service-v2)

### :white_check_mark: SNS 피드 서비스 아키텍처

<img width="1000" alt="image" src="https://user-images.githubusercontent.com/23515771/208303790-ec90177c-87d0-42fe-9a34-f964ff0650be.png">

### :white_check_mark: 팬 아웃 Push 모델

#### :arrow_forward: 프로세스

<img width="1000" alt="image" src="https://user-images.githubusercontent.com/23515771/191925405-713596e6-a47c-4745-8862-309799f71558.png">

1. `Post` 를 데이터베이스에 저장한다.

2. `PostCache`, `PostViewCount`, `PostKeys(Post Id 리스트를 담고 있는 데이터)` 를 레디스에 캐싱한다.

    - `PostKeys` 는 `Sorted Set` 컬렉션을 사용하여, 포스팅을 저장한 순서대로 `Id` 를 캐싱한다.

    - `PostKeys` 는 최대 `100,000` 개만 저장한다.

        - `zremRangeByRank` 를 활용해서 `100,000` 개를 유지한다.

3. 사용자를 팔로우한 `Followee` 리스트를 조회한다.

4. `Feed` 이벤트를 만들어 카프카를 통해 메시지를 발행한다.

5. `Feed Event Worker` 서버에서는 `Feed` 이벤트를 수신한다.

6. `FeedCache` 를 만들어 `Followee` 의 Id를 기준으로 `Sorted Set` 컬렉션을 활용하여, `등록 순` 으로 피드 캐시를 저장한다.

### :white_check_mark: Post 캐시 처리

#### :arrow_forward: 프로세스

- `post:ids:member:$memberId` Key에 `postId` 를 저장하고, 만료 시간은 `30일` 을 부여한다.

    - Post 페이지네이션을 활용하기 위해 `memberId` 기준으로 `postId` 를 `SortedSet` 자료구조에 적재한다.

- `post:$id` Key에는 Post 캐시를 저장한다.

    - 단 건 조회시, `get` 명령을 사용한다.

    - 페이지네이션 요청시, Redis 클러스터 사용할 것을 고려해 `mget` 을 사용하지 않고, `pipeline` 기능을 활용해서 `get` 을 통한 여러 건을 조회한다.

#### :arrow_forward: 캐시 메모리 계산 결과

> Post 캐시

- `240,000` 건의 캐시를 저장하는데, `92.3 MB` 를 사용함.
    - `1 GB` 의 경우, 대략 `2,400,000` 건의 Post 관련 캐시를 저장할 수 있음.
    - `10 GB` 의 경우, 대략 `24,000,000` 건의 Post 관련 캐시를 저장할 수 있음.
    - `32 GB` 의 경우, 대략 `76,800,000` 건의 Post 관련 캐시를 저장할 수 있음.

> 메모리 관련 참고 자료

- 캐시 데이터가 억 단위를 넘어가는
  경우, [Redis에 심플한 key-value 로 수 억개의 데이터 저장하기](https://charsyam.wordpress.com/2011/11/06/redis%EC%97%90-%EC%8B%AC%ED%94%8C%ED%95%9C-key-value-%EB%A1%9C-%EC%88%98-%EC%96%B5%EA%B0%9C%EC%9D%98-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A0%80%EC%9E%A5%ED%95%98%EA%B8%B0/)
  을 참고해서 `Hashes` 자료구조로 개선하자.

### :white_check_mark: 성능 테스트

#### :arrow_forward: 인스턴스 사양

> Amazon MSK

- `kafka.t3.small`
    - `vCpu` : `2048`
    - `메모리(GiB)` : `2G`
    - `네트워크 대역폭 (Gbps)` : `5 Gbps`

> Amazon Elasticache

- `cache.t3.small`
    - `메모리(GiB)` : `1.37 GiB`
    - `Up to 5 Gigabit nework perfomance`

> AWS ECS Fargate

- `vCpu` : `2048` or `4096`
- `Memory` : `6G` or `8G`

> Spring Boot Application

- `Xms` : `4096MB (4G)`
- `Xmx` : `4096MB (4G)`
- `GC` : `ZGC`

#### :arrow_forward: 성능 테스트 결과

> API

- `사용자 수` : `2,000명`
- `Ramp Up 시간 (초)` : `1초`
- `테스트 지속 시간` : `60초`

| 라벨 | 표본 수 | 평균(ms) | 최소값(ms) | 최대값(ms) | 오류 (%) | 처리량 |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| 회원 생성 | 79,690 | 1,495ms | 52ms | 4,831ms | 0.00% | 1293.7/sec |

| 라벨 | 표본 수 | 평균(ms) | 최소값(ms) | 최대값(ms) | 오류 (%) | 처리량 |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| 게시물 생성 | 43,119 | 2,822ms | 89ms | 75,006ms | 0.05% | 569.5/sec |

> Query API

- RateLimiter 의 `limitForPeriod` 값이 `3,000`
- AWS ECS `vCpu` : `4096`
- AWS ECS `Memory` : `8G`
- AWS ECS 인스턴스 수 : `1대`
- Tomcat Max Thread: `150`
- 게시물 리스트, 타임라인, 타임라인 새로고침, 상세 조회 총계

| 라벨 | 표본 수 | 평균(ms) | 최소값(ms) | 최대값(ms) | 오류 (%) | 처리량 |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| 총계 | 191,195 | 622ms | 20ms | 66,046ms | 0.01% | 2821.1/sec |
| 총계 | 160,074 | 737ms | 17ms | 48,889ms | 0.03% | 2606.3/sec |
| 총계 | 170,529 | 696ms | 10ms | 75,003ms | 0.01% | 2710.7/sec |

- 나머지는 위의 조건과 동일
- AWS ECS 인스턴스 수 : `2대`

| 라벨 | 표본 수 | 평균(ms) | 최소값(ms) | 최대값(ms) | 오류 (%) | 처리량 |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| 총계 | 298,464 | 396ms | 9ms | 60,014ms | 0.00% | 4925.9/sec |
| 총계 | 304,285 | 388ms | 10ms | 59,919ms | 0.00% | 5017.3/sec |
| 총계 | 309,815 | 286ms | 10ms | 60,473ms | 0.00% | 5109.6/sec |

- 나머지는 위의 조건과 동일
- Tomcat Max Thread: `200`
    - 스레드 수가 늘어나도 성능이 좋아짐을 보장할 수
      없다. ([스레드 컨텍스트 스위칭](https://www.inflearn.com/questions/252332/%EC%8A%A4%EB%A0%88%EB%93%9C-%EC%BB%A8%ED%85%8D%EC%8A%A4%ED%8A%B8-%EC%8A%A4%EC%9C%84%EC%B9%AD))
    - 같은 4코어의 CPU를 사용한다면, 스레드 풀이 늘어날 수록 컨텍스트 스위칭 비용은 늘어난다.
        - `200` 개의 스레드 / `4` 코어 = `50` 번의 컨텍스트 스위칭
        - `150` 개의 스레드 / `4` 코어 = `37.5` 번의 컨텍스트 스위칭

| 라벨 | 표본 수 | 평균(ms) | 최소값(ms) | 최대값(ms) | 오류 (%) | 처리량 |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| 총계 | 161,657 | 735ms | 15ms | 67,120ms | 0.00% | 2340.7/sec |

### :white_check_mark: 정리

`QUERY API (Read 서버)` 의 경우, 인스턴스 `1대` 당 평균 `2,700.0/sec` TPS의 성능 지표를 보이고 있으며, `2대` 의 경우 `5,000.0/sec` TPS 성능 지표를 보이고 있다.
만약 `50,000.0/sec` TPS를 처리해야 한다면, `20대 이상` 의 서버를 구축해야한다.

## :pushpin: 쿠폰 이벤트 선착순 시스템 설계

### :white_check_mark: 소스 코드

- [coupon-service-v2](https://github.com/bestdevhyo1225/large-system-design/tree/main/coupon-service-v2)

### :white_check_mark: 쿠폰 서비스 아키텍처 - V2

<img width="1834" alt="image" src="https://user-images.githubusercontent.com/23515771/197327876-5b53d114-a32f-4fe5-b346-1d20e7a5973d.png">

#### :arrow_forward: Write Through 패턴 사용

- 선착순의 경우, DB에 갑작스런 쓰기 요청이 몰리게 되면 DB 서버가 죽을 수도 있다. 따라서 임시적으로 Redis 서버에 캐시 데이터를 적재하고 Kafka를 활용해서 DB에 쓰기 작업을 처리한다.
