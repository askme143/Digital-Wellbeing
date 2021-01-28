package com.yeongil.digitalwellbeing.dataSource.kakaoApi.responseData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LatLngToAddressResponse(
    val documents: List<Document>,
    val meta: Meta
) {
    @Serializable
    data class Meta(
        @SerialName("total_count") val totalCount: Int
    )

    @Serializable
    data class Document(
        val address: Address,
        @SerialName("road_address") val roadAddress: RoadAddress?
    )

    @Serializable
    data class Address(
        @SerialName("address_name") val addressName: String,
        @SerialName("region_1depth_name") val region1depthName: String,
        @SerialName("region_2depth_name") val region2depthName: String,
        @SerialName("region_3depth_name") val region3depthName: String,
        @SerialName("mountain_yn") val mountainYN: String,
        @SerialName("main_address_no") val mainAddressNo: String,
        @SerialName("sub_address_no") val subAddressNo: String,
        @SerialName("zip_code") val zipCode: String,
    )

    @Serializable
    data class RoadAddress(
        @SerialName("address_name") val addressName: String,
        @SerialName("region_1depth_name") val region1depthName: String,
        @SerialName("region_2depth_name") val region2depthName: String,
        @SerialName("region_3depth_name") val region3depthName: String,
        @SerialName("road_name") val roadName: String,
        @SerialName("underground_yn") val undergroundYN: String,
        @SerialName("main_building_no") val mainBuildingNo: String,
        @SerialName("sub_building_no") val subBuildingNo: String,
        @SerialName("building_name") val buildingName: String,
        @SerialName("zone_no") val zoneNo: String
    )
}