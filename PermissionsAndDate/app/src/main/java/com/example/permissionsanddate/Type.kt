package com.example.permissionsanddate
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.Instant

sealed class Type : Parcelable {
    abstract val createdAt: Instant

    @Parcelize
    data class Location(
        val id: Long,
        override val createdAt: Instant,
        val latitude: Double,
        val longitude: Double,
        val speed: Float,
        val imageLink: String
    ) : Type() {

        companion object {
            private val listOfLinks: List<String> = listOf(
                "https://core-renderer-tiles.maps.yandex.net/tiles?l=map&v=22.04.21-0-b220426173400&x=19809&y=10272&z=15&lang=ru_RU",
                "https://core-renderer-tiles.maps.yandex.net/tiles?l=map&v=22.04.21-0-b220426173400&x=19809&y=10273&z=15&lang=ru_RU",
                "https://core-renderer-tiles.maps.yandex.net/tiles?l=map&v=22.04.21-0-b220426173400&x=19810&y=10273&z=15&lang=ru_RU",
                "https://core-renderer-tiles.maps.yandex.net/tiles?l=map&v=22.03.17-0-b220203150200&x=19667&y=10707&z=15&scale=1&lang=ru_RU&ads=enabled",
                "https://core-renderer-tiles.maps.yandex.net/tiles?l=map&v=22.03.30-0-b220203150200&x=13983&y=5295&z=14&scale=1&lang=ru_RU&ads=enabled",
                "https://core-renderer-tiles.maps.yandex.net/tiles?l=map&v=22.04.21-0-b220426173400&x=3046&y=1274&z=12&scale=1&lang=ru_RU&ads=enabled"
            )

            fun getRandomImageLink(): String {
                return listOfLinks.random()
            }
        }
    }
}
