package fastcampus.aop.pjt07_recorder

// 상태에 따라 다른 UI를 표시하기 위해 상태를 미리 정의
enum class State {
    BEFORE_RECORDING,
    ON_RECORDING,
    AFTER_RECORDING,
    ON_PLAYING
}