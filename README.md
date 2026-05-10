# Ball_Buster

#
* **발표 영상 자료**:

*1차: https://youtu.be/Zc92kJFUXNI

*2차: https://youtu.be/poyAtwNgx8s

---


## 1. 게임 소개
* 끊임없이 튕겨 오르는 거대한 공들을 작살로 쏴서 가장 작은 크기로 쪼개어 모두 소탕하는 2D 아케이드 액션 게임 (고전 명작 '팡' 모작)

* <img width="921" height="696" alt="image" src="https://github.com/user-attachments/assets/94c995c9-f3b9-4355-b4c2-f26d41b6a579" />

(출처: https://www.youtube.com/watch?v=WMmApkBgpxI&t=101s | AracdeExtreme)

* **핵심 메카닉**:
  * **수직 공격 및 분열**: 플레이어는 좌우로만 이동하며 위로 작살을 쏩니다. 작살에 맞은 공은 터지며 한 단계 작은 크기의 공 2개로 분열해 양옆으로 튕겨 나갑니다.
  * **크기마다 다른 속도**: 공의 크기에 따라 튕기는 높이와 체공 시간이 다르게 적용되어, 플레이어는 타이밍을 맞춰 피하거나 공격해야 합니다.


---


## 2. 진행 상황
프레임워크 연동 및 Scene구성: 60% ( 게임 오버, Pause Scene 구현 진행 중 )

플레이어 조작: 100%

작살 발사 관련 로직: 100%

공 중력 물리 엔진 및 바닥 바운스 구현: 90% (속도 및 튀기는 높이 밸런스 패치 진행 중)

충돌 판정 및 공 분열 로직: 70% (장애물 구현 진행중)

UI 관련 구현: 0% (진행 중)

스테이지 구현: 0% (진행 중)

그래픽 리소스 적용: 0% (진행 중)

사운드 리소스 적용: 0% (진행 중)

---


## 3. git commit

<img width="647" height="620" alt="image" src="https://github.com/user-attachments/assets/a7505c34-7b9e-4f06-b54d-178d6c0d75bf" />

|주차|기간|횟수|
|------|---|---|
|1주차|04.05 ~ 04.11|12회|
|2주차|04.12 ~ 04.18|4회|
|3~4주차|04.19 ~ 05.02|0회|
|5주차|05.03 ~ 05.09|5회|


---


## 4. 변경된 사항

 **화면 방향 변경 (세로 모드 -> 가로 모드)**
 세로 모드의 경우 공간이 부족해 플레이어가 분열하는 공을 피하기 어렵다고 판단하여 가로 모드로 게임을 개발하도록 목표를 변경하였습니다.
 
 또한 세로 모드의 경우 좌측 조이스틱과 우측 작살 발사 터치를 하기 위해서 화면을 너무 가려 시야가 답답해진다는 것도 목표 변경의 이유입니다.


---

## 5. Activity 구성

 **GameActivity**: 현재 게임의 유일한 Activity로 해상도를 고정하여 게임의 물리적 환경을 통제하고, 메인 Scene을 호출하여 본격적인 게임 루프를 시작하는 진입점 역할을 합니다.


---

## 6. Scene 구성 및 전환 관계

 **Scene 구성**: Title, Main, GameOver, Clear, Pause

 **전환 관계**
 Title Scene -> 버튼 터치 -> Main Scene
 
 Main Scene -> 플레이어 체력 소진 -> GameOver Scene
 
 Main Scene -> 모든 공 소멸 -> Clear Scene
 
 Main Scene -> Pause 버튼 터치 -> Pause Scene


---

## 7. MainScene 핵심 game object

<img width="2424" height="1080" alt="image" src="https://github.com/user-attachments/assets/d5b6ef7f-acff-4232-9d0e-a7adc5cb6a32" />


**1. Player**
구성: 화면 하단에 위치하며, 현재 파란색 네모로 렌더링 됨.

상호작용: JoyStick의 angle과 power 데이터를 받아 좌우 속력을 계산함. 오른쪽 화면 터치 시 Harpoon 객체를 world에 동적으로 추가함.

핵심 코드: dx = maxSpeed * joystick.power * cos(joystick.angle) 식을 통해 조이스틱 입력값을 삼각함수 기반의 부드러운 가속도 이동으로 변환.

**2. Ball**
구성: level 변수(1~3)를 가지며, 레벨에 따라 반지름과 바운스 높이가 결정됨. 빨간색 원형으로 렌더링.

상호작용: 바닥 및 벽과 충돌 시 반발 계수 적용.

핵심 코드: dy += gravity * dt 로 중력을 구현하고, split() 함수를 통해 피격 시 자신의 레벨을 1 낮춘 두 개의 새로운 Ball 인스턴스를 반대 방향 으로 튕겨내며 기존 객체는 파기함.

**3. Harpoon**
구성: 플레이어 위치에서 생성되어 위로 솟구치는 노란색 직사각형 투사체.

상호작용: Y 좌표가 0 미만(화면 상단 이탈)이 되면 자기 자신을 world에서 해제함.

핵심 코드: y -= speed * dt 로 등속 수직 상승 운동을 수행하며 정확한 타격 영역을 갱신.

**4. CollisionChecker**
구성: 렌더링되지 않는 로직 전용 객체.

상호작용: MainLayer.HARPOON과 MainLayer.BALL 레이어에 접근.

핵심 코드: world.forEachReversedAt 함수를 사용하여 두 레이어의 객체들을 역순으로 안전하게 이중 순회함. RectF.intersects로 AABB 충돌을 검사하고, 충돌 시 Ball.split() 호출.
