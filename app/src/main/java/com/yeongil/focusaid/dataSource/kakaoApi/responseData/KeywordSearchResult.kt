package com.yeongil.focusaid.dataSource.kakaoApi.responseData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KeywordSearchResult (
    val meta: Meta,
    val documents: List<Document>
) {
    @Serializable
    data class Document(
        val id: String,
        @SerialName("place_name") val placeName: String,
        @SerialName("category_name") val categoryName: String,
        @SerialName("category_group_code") val categoryGroupCode: String,
        @SerialName("category_group_name") val categoryGroupName: String,
        val phone: String,
        @SerialName("address_name") val addressName: String,
        @SerialName("road_address_name") val roadAddressName: String,
        val x: Double,
        val y: Double,
        @SerialName("place_url") val placeURL: String,
        val distance: String
    )

    @Serializable
    data class Meta(
        @SerialName("total_count") val totalCount: Int,
        @SerialName("pageable_count") val pageableCount: Int,
        @SerialName("is_end") val isEnd: Boolean,
        @SerialName("same_name") val sameName: SameName
    )

    @Serializable
    data class SameName(
        val region: List<String>,
        val keyword: String,
        @SerialName("selected_region") val selectedRegion: String
    )
}