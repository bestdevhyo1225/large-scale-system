# 대규모 시스템 설계 기초

## 참고 서적

- [가상 면접 사례로 배우는 대규모 시스템 설계 기초](http://www.yes24.com/Product/Goods/102819435)

## 시스템 설계 리스트

- [URL 단축기 설계](https://github.com/bestdevhyo1225/large-system-design#url-%EB%8B%A8%EC%B6%95%EA%B8%B0-%EC%84%A4%EA%B3%84)
- [SNS 피드 시스템 설계](https://github.com/bestdevhyo1225/large-system-design#sns-%ED%94%BC%EB%93%9C-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%84%A4%EA%B3%84)
- [쿠폰 이벤트 선착순 시스템 설계]()

## URL 단축기 설계

### 요구 사항

- 긴 `URL` 을 단축해서 짧은 길이의 `URL` 을 사용자에게 제공해야 한다.
- 매일 `1억` 개의 단축기를 만들어야 한다.
- 단축 `URL` 에는 `숫자(0 ~ 9)`, `영문자(a ~ z, A ~ Z)` 까지만 사용할 수 있다.
- 시스템 단순화를 위해 갱신, 삭제는 할 수 없다.

### 개략적 추정

- `쓰기 연산` : 매일 `1억` 개의 단축 `URL` 생성
- `초당 쓰기 연산` : `1억 / 24시간 / 3600초 = 1,160회` 발생한다.
- `읽기 연산` : 읽기 연산과 쓰기 연산의 비율을 `10 : 1` 로 가정했을때, 초당 `11,600회` 발생한다.
- 10년간 `URL` 단축 서버 운영하게 된다면, `1억 x 356일 x 10년 = 3,650억` 개의 레코드를 보관해야 한다.
  - `n = 7` 인 경우, `3.5조` 의 해시 값을 만들 수 있다.
  - `n = 8` 인 경우, `218조` 의 해시 값을 만들 수 있다.
- 단축 전 `URL` 의 평균 길이를 `100` 이라 가정했을때, 10년 동안 필요한 저장 용량은 `3,650억 x 100바이트 = 36.5TB` 이다.

### 부하 테스트 결과 (JMeter)

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

> 단일 모듈 서버에서 실행한 결과

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| URL 조회 요청 | 25,003회 | 990ms | 11ms | 2,379ms | 0.00% | 411.6/sec |
| URL 생성 요청 | 24,495회 | 1,439ms | 409ms | 3,714ms | 0.00% | 408.1/sec |

> Command, Query 모듈 서버를 분리한 결과

- `Number Of Threads (users)` : 1,000명, `Ramp-up Period (seoncds)` : 1초

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| URL 조회 요청 | 644,924회 | 89ms | 9ms | 1,109ms | 0.00% | 10723.4/sec |
| URL 생성 요청 | 50,774회 | 1,182ms | 20ms | 2,743ms | 0.00% | 830.1/sec |

> Query 모듈 서버 결과

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

> Command 모듈 서버 결과

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

### 정리

`단일 모듈 서버` 에서는 `411.6 TPS` 의 쓰기 작업, `408.1 TPS` 의 읽기 작업이 처리된다. 대략 `1대의 서버` 가 `800 TPS` 정도의 성능을 내는데, 초당 `11,600회` 의 처리
성능을 감당하려면, `최소 15대의 서버` 가 필요하다.

CQRS 패턴을 적용한 `Command, Query` 모듈 서버에서는 `651.7 TPS` 의 쓰기 작업, `11760.6 TPS` 의 읽기 작업이 처리된다. 이를 통해 필요한 서버의 수를 측정한다면, 쓰기 작업을
수행하는 `Command` 서버는 `최소 2대의 서버` 가 필요하며, `Query` 서버도 `최소 2대 서버` 가 필요하다.

## SNS 피드 시스템 설계

### 팬 아웃(포스팅 전송) 프로세스

<img width="1474" alt="image" src="https://user-images.githubusercontent.com/23515771/191925405-713596e6-a47c-4745-8862-309799f71558.png">

1. `Post` 를 데이터베이스에 저장한다.

2. `PostCache`, `PostViewCount`, `PostKeys(Post Id 리스트를 담고 있는 데이터)` 를 레디스에 캐싱한다.

    - `PostKeys` 는 `Sorted Set` 컬렉션을 사용하여, 포스팅을 저장한 순서대로 `Id` 를 캐싱한다.

    - `PostKeys` 는 최대 `100,000` 개만 저장한다.

        - `zremRangeByRank` 를 활용해서 `100,000` 개를 유지한다.

3. 사용자를 팔로우한 `Followee` 리스트를 조회한다.

4. `Feed` 이벤트를 만들어 카프카를 통해 메시지를 발행한다.

5. `Feed Event Worker` 서버에서는 `Feed` 이벤트를 수신한다.

6. `FeedCache` 를 만들어 `Followee` 의 Id를 기준으로 `Sorted Set` 컬렉션을 활용하여, `등록 순` 으로 피드 캐시를 저장한다.

### 팬 아웃(포스팅 전송) 성능 테스트 결과

- `Number Of Threads (users)` : 2,000명, `Ramp-up Period (seoncds)` : 1초

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| /api/v1/posts | 16,328회 | 3,824ms | 1ms | 6,707ms | 0.02% | 476.8/sec |
| /api/v1/posts | 17,136회 | 3,649ms | 2ms | 8,303ms | 0.06% | 504.3/sec |

### 피드 조회 성능 테스트 결과

- `Number Of Threads (users)` : 1,000명, `Ramp-up Period (seoncds)` : 1초

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| /api/v1/feeds/{memberId}/posts | 142,327회 | 843ms | 19ms | 3,813ms | 0.00% | 1175.4/sec |
| /api/v1/feeds/{memberId}/posts | 105,393회 | 1,138ms | 31ms | 3,847ms | 0.00% | 870.6/sec |
| /api/v1/feeds/{memberId}/posts | 122,928회 | 975ms | 163ms | 2,192ms | 0.00% | 1016.2/sec |

## 쿠폰 이벤트 선착순 시스템 설계

### 쿠폰 이벤트 선착순 프로세스

<img width="1840" alt="image" src="https://user-images.githubusercontent.com/23515771/195261911-864a4666-6616-43da-a048-66afc576349b.png">
