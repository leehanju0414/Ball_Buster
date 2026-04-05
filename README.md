# Ball_Buster

#
* **발표 영상 자료**:


## 1. 개발 범위
* 공과 작살 객체의 생성과 소멸 관리하기. 
* 플레이어 및 작살과 공과의 충돌 판정을 구현.
* 기기 성능에 영향받지 않도록 Delta Time을 적용하여 공의 물리 이동을 구현.
* Title Scene, MainGame Scene, GameOver Scene 간의 상태 관리 및 전환을 구현.
* 스프라이트 시트를 활용하여 작살 발사, 공 파괴, 플레이어 이동 애니메이션을 구현.

* * **캐릭터**: * 플레이어 객체 1종 (좌/우 이동, 피격 모션)
  * 적 공 객체 4단계 (Large -> Medium -> Small -> Tiny)
* **화면 구성**:
  * Title Scene (로고 및 조작법)
  * Main Game Scene (최소 레벨 1, 레벨 2 구성)
  * Game Over / Clear Scene
* **오브젝트/아이템**:
  * 플레이어 투사체 (기본 작살)
  * 장애물 블록 (공이 부딪혀 튕기는 구조물)
* **사운드**: 배경음악 2곡(타이틀, 메인), 효과음 4종(발사, 터짐, 피격, 클리어)


---


## 2. 게임 컨셉
* 끊임없이 튕겨 오르는 거대한 공들을 작살로 쏴서 가장 작은 크기로 쪼개어 모두 소탕하는 2D 아케이드 액션 게임 (고전 명작 '팡' 모작)

* <img width="921" height="696" alt="image" src="https://github.com/user-attachments/assets/94c995c9-f3b9-4355-b4c2-f26d41b6a579" />

(출처: https://www.youtube.com/watch?v=WMmApkBgpxI&t=101s | AracdeExtreme)

* **핵심 메카닉**:
  * **수직 공격 및 분열**: 플레이어는 좌우로만 이동하며 위로 작살을 쏩니다. 작살에 맞은 공은 터지며 한 단계 작은 크기의 공 2개로 분열해 양옆으로 튕겨 나갑니다.
  * **중력과 체공 시간**: 공의 크기에 따라 튕기는 높이와 체공 시간이 다르게 적용되어, 플레이어는 타이밍을 맞춰 피하거나 공격해야 합니다.


---


## 4. 예상 게임 실행 흐름

1. **Title 화면**: 게임 로고와 [Touch to Start] 텍스트가 깜빡임.
   
   <img width="334" height="555" alt="image" src="https://github.com/user-attachments/assets/d74c1bb9-87fd-4d94-b416-9908171f4f49" />

2. **Main 플레이 화면**: 플레이어는 하단에서 좌우로 움직이고, 다양한 크기의 공들이 포물선을 그리며 화면을 튕겨 다님.
   
   <img width="335" height="557" alt="image" src="https://github.com/user-attachments/assets/07364d89-bd29-47fb-b753-33beffdab85d" />


3. **오브젝트 충돌/분열**: 플레이어가 위로 작살을 쏘아 큰 공을 맞추면, 2개의 작은 공으로 갈라져 튕기는 연출.
   
   <img width="323" height="551" alt="image" src="https://github.com/user-attachments/assets/8f3e56d3-ee4f-4ca2-a417-f04c73d62a35" />

4. **Game Over / Clear**: 플레이어가 공에 닿아 목숨을 모두 잃는다면 Game Over 씬으로 전환되며 재시작 버튼 출력.
   
   <img width="332" height="556" alt="image" src="https://github.com/user-attachments/assets/81377da8-2850-4e53-a3a4-986864bc6a80" />



---


## 5. 개발 일정

* **1주차 (4/6 ~ 4/12)**: 프로젝트 세팅 및 Framework 기본 구조 구축, Title과 Main Scene 전환 구현
* **2주차 (4/13 ~ 4/19)**: 플레이어 오브젝트 추가 및 좌우 이동 로직 구현
* **3주차 (4/20 ~ 4/26)**: 투사체(작살) 발사 로직 및 공 오브젝트 추가, 중력 포물선 이동 구현
* **4주차 (4/27 ~ 5/3)**: 충돌 구현 및 충돌 시 공 분열 로직 구현
* **5주차 (5/4 ~ 5/10)**: 분열하는 공 객체 최적화 및 장애물 블록 구현
* **6주차 (5/11 ~ 5/17)**: UI(목숨, 점수 등) 구현, 스테이지 단계 구현
* **7주차 (5/18 ~ 5/24)**: 애니메이션(공 터짐 이펙트 등) 적용 및 사운드 리소스 추가
* **8주차 (5/25 ~ 5/31)**: 전체 밸런스 조정, 버그 수정
