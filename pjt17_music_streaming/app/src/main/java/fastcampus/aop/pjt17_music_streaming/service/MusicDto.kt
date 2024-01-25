package fastcampus.aop.pjt17_music_streaming.service

data class MusicDto(
    // 서버에서 전달 받은 값을 Entity에 넣고
    // View에서 표시할 값을 Model로 만들어서 매핑
    val musics: List<MusicEntity>
)
