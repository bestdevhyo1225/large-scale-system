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
- 단축 전 `URL` 의 평균 길이를 `100` 이라 가정했을때, 10년 동안 필요한 저장 용량은 `3,650억 x 100바이트 = 36.5TB` 이다.

### 서버 실행 및 종료

> 권한 관련 명령어 실행

```shell
chmod +x ./shortened-url-server/start.sh
chmod +x ./shortened-url-server/stop.sh
```

> 서버 실행

- 도커 컨테이너 기반의 `URL 단축 서버` 가 실행된다.

```shell
./shortened-url-server/start.sh
```

> 서버 종료

- 도커 컨테이너의 `URL 단축 서버` 를 종료하고, `도커 컨테이너, 이미지, 볼륨, 이미지` 를 모두 삭제한다.

```shell
./shortened-url-server/stop.sh
```

### 부하 테스트 결과 (JMeter)

- `Samples`: 테스트 서버로 보낸 요청의 수
- `Average` : 평균 응답 시간(ms)
- `Min` : 최소 응답 시간(ms)
- `Max` : 최대 응답 시간(ms)
- `Throughput` : 단위 시간당 대상 서버에서 처리되는 요청의 수 (JMeter에서는 시간 단위를 보통 `TPS(Transaction Per Second)` 로 표현)

> Cache 적용 전, 결과 (MySQL 사용)

- `60초` 동안, `1초` 마다 `1,000명` 의 유저가 요청을 보낸 상황

| Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: |
| 26,935회 | 2,250ms | 41ms | 4,012ms | 0.00% | 434.4/sec |

> Cache 적용 후, 결과 (MySQL, Redis 사용)

- `60초` 동안, `1초` 마다 `1,000명` 의 유저가 요청을 보낸 상황

| Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: |
| 443,332회 | 134ms | 15ms | 1,193ms | 0.01% | 7,373.3/sec |
| 460,072회 | 129ms | 15ms | 1,178ms | 0.02% | 7,630.7/sec |

- `60초` 동안, `1초` 마다 `1,000명` 의 유저가 요청을 보낸 상황
- `command timeout`, `shutdown timeout` 옵션을 디폴트 값으로 설정

| Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: |
| 528,573회 | 112ms | 5ms | 1,251ms | 0.00% | 8,790.2/sec |

- `60초` 동안, `1초` 마다 `1,500명` 의 유저가 요청을 보낸 상황
- `command timeout`, `shutdown timeout` 옵션을 디폴트 값으로 설정

| Samples | Average | Min | Max | Erros (%) | Throughput |
| :-: | :-: | :-: | :-: | :-: | :-: |
| 641,980회 | 138ms | 7ms | 1,162ms | 0.00% | 10,660.4/sec |
