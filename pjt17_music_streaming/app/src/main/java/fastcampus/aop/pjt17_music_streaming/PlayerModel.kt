package fastcampus.aop.pjt17_music_streaming

data class PlayerModel(
    private val playMusicList: List<MusicModel> = emptyList(),
    var currentPosition: Int = -1,
    var isWatchingPlayListView: Boolean = true
) {
    fun getAdapterModels(): List<MusicModel> {
        // 원본 리스트를 변경해서 반환할 경우 주소가 연동되어 PlayListAdapter의 diffUtil이
        // isPlaying이 변경된 아이템이 무엇인지 파악할 수 없음
        // 따라서 복사된 리스트를 반환
        return playMusicList.mapIndexed { index, musicModel ->
            val newItem = musicModel.copy(
                isPlaying = index == currentPosition
            )
            newItem
        }
    }

    fun updateCurrentPosition(musicModel: MusicModel) {
        currentPosition = playMusicList.indexOf(musicModel)
    }

    fun nextMusic(): MusicModel? {
        if (playMusicList.isEmpty()) return null

        currentPosition = if ((currentPosition + 1) == playMusicList.size) 0 else currentPosition + 1
        return playMusicList[currentPosition]
    }

    fun prevMusic(): MusicModel? {
        if (playMusicList.isEmpty()) return null

        currentPosition = if ((currentPosition - 1) < 0) playMusicList.lastIndex else currentPosition - 1
        return playMusicList[currentPosition]
    }

    fun currentMusicModel(): MusicModel? {
        if (playMusicList.isEmpty()) return null

        return playMusicList[currentPosition]
    }
}
