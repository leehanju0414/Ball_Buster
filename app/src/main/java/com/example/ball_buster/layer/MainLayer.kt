package com.example.ball_buster.scene

enum class MainLayer {
    BG,         // 배경
    PLAYER,     // 좌우로 움직이며 작살을 쏘는 주인공 캐릭터
    HARPOON,    // 하늘로 솟구치는 작살
    BALL,       // 통통 튀어다니는 공들
    UI,         // 점수, 남은 시간, 목숨 등
    TOUCH       // 이동 및 발사 버튼 등을 처리할 터치 영역
}