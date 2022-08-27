# 대규모 시스템 설계 기초

## 참고 서적

- [가상 면접 사례로 배우는 대규모 시스템 설계 기초](http://www.yes24.com/Product/Goods/102819435)

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

- `Number Of Threads (users)` : 2,000명, `Ramp-up Period (seoncds)` : 1초
- Redis 요청시, `Circuit Breaker` 적용 (fallback 처리)

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| URL 조회 요청 | 1,294,603회 | 184ms | 9ms | 1,469ms | 0.00% | 10742.2/sec |

- `Number Of Threads (users)` : 1,500명, `Ramp-up Period (seoncds)` : 1초
- 단축 URL이 중복으로 생성되는 문제가 있음.

| Label | Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| URL 생성 요청 | 72,116회 | 2,508ms | 18ms | 4,303ms | 0.00% | 588.1/sec |

### 정리

`단일 모듈 서버` 에서는 `411.6 TPS` 의 쓰기 작업, `408.1 TPS` 의 읽기 작업이 처리된다. 대략 `1대의 서버` 가 `800 TPS` 정도의 성능을 내는데, 초당 `11,600회` 의 처리
성능을 감당하려면, `최소 15대의 서버` 가 필요하다.

CQRS 패턴을 적용한 `Command, Query` 모듈 서버에서는 `588.1 TPS` 의 쓰기 작업, `10723.4 TPS` 의 읽기 작업이 처리된다. 이를 통해 필요한 서버의 수를 측정한다면, 쓰기 작업을
수행하는 `Command` 서버는 `최소 2 ~ 3대의 서버` 가 필요하며, `Query` 서버도 `최소 2대 서버` 가 필요하다.
