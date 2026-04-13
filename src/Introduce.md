# 📊 공랩 (Kong Lab.)

> 뉴스와 시장 데이터를 기반으로  
> 오늘 이슈가 있는 종목을 선별하고,  
> AI가 그 이유를 쉽게 설명해주는 서비스

---

## 🧠 프로젝트 목표

공랩(Kong Lab.)은  
단순한 “주식 추천”이 아닌,

- 오늘 왜 이 종목이 주목받는지
- 어떤 뉴스가 영향을 미쳤는지
- 관심 종목 중 무엇이 움직이는지

를 **AI 기반으로 분석하여 보여주는 서비스**입니다.

---

## 🚀 MVP 기능

초기 버전에서는 아래 기능을 구현합니다.

1. 오늘 이슈 종목 리스트 조회
2. 종목 상세 조회
3. 종목별 뉴스 목록 조회
4. AI 이슈 요약 조회
5. 관심 종목 등록 / 삭제 / 조회

---

## 🧩 기술 스택

### Backend
- Java 17 or 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Lombok

### Frontend
- Vue 3
- Vite
- Axios

### Tools
- Cursor (AI IDE)
- ChatGPT
- DBeaver or DataGrip

---

## 🏗 프로젝트 구조

com.konglab
├─ common
│  ├─ config
│  ├─ exception
│  ├─ response
│  └─ util
├─ stock
├─ news
├─ issue
├─ watchlist
└─ ai

각 도메인 내부 구조:
- controller
- service
- repository
- entity
- dto

---

## 🗃 엔티티 설계

### 1. Stock
- 종목 정보
- ticker, name, marketType
- currentPrice, changeRate

### 2. News
- 종목 관련 뉴스
- title, summary, source
- url, publishedAt, sentiment

### 3. IssueSummary
- AI 분석 결과
- shortSummary
- issueScore
- keywords

### 4. Watchlist
- 관심 종목 저장

---

## 🔌 API 설계

### 📌 이슈 종목
- GET /api/issues/today

### 📌 종목
- GET /api/stocks/{stockId}

### 📌 뉴스
- GET /api/stocks/{stockId}/news

### 📌 AI 요약
- GET /api/stocks/{stockId}/issue-summary

### 📌 관심 종목
- GET /api/watchlist
- POST /api/watchlist/{stockId}
- DELETE /api/watchlist/{stockId}

---

## 🔄 데이터 흐름

1. 종목 / 뉴스 데이터 수집 (초기: 샘플 데이터)
2. DB 저장
3. 뉴스 기반 AI 요약 생성
4. 이슈 점수 계산
5. 이슈 종목 리스트 제공

---

## 🧪 데이터 전략

### 1차 (MVP)
- 샘플 데이터 사용
- 종목 5개 정도
- 뉴스 3~5개씩

### 2차
- 외부 API 연동
- 뉴스 자동 수집

---

## 🛢 DB 설정

### DB 이름
kong_lab

---

## ⚙️ 개발 워크플로우 (AI 기반)

### 1️⃣ 설계
- ChatGPT와 구조 설계

### 2️⃣ 구현
- Cursor로 코드 생성 및 수정

### 3️⃣ 확장
- AI 요약 / 자동화 기능 추가

---

## 🧑‍💻 개발 순서

1. Spring Boot 프로젝트 생성
2. PostgreSQL 연결
3. 패키지 구조 생성
4. 엔티티 작성
5. Repository 작성
6. API 구현
7. 샘플 데이터 삽입
8. Vue 화면 개발
9. AI 요약 기능 추가

---

## 🎯 핵심 컨셉

“추천”이 아닌 “이해를 돕는 분석 서비스”

---

## 📌 향후 확장 계획

- 외부 뉴스 API 연동
- 실시간 데이터 처리
- 사용자 로그인
- 개인화 추천
- 알림 기능
- 배치 처리

---

## 💡 개발 철학

- 빠르게 만들고 → 점진적으로 개선
- AI를 활용한 생산성 극대화
- 실무형 구조 유지

---

# Ground Rules

## DB Naming Rule

- table: 단수형 snake_case
- column: snake_case
- pk: {entity}_id
- fk: {entity}_id