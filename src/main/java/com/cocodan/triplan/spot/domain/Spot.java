package com.cocodan.triplan.spot.domain;

import com.cocodan.triplan.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Spot extends BaseEntity implements Persistable<Long> {

    public static final int PHONE_MAX_LENGTH = 13;
    public static final int PHONE_MIN_LENGTH = 10;

    public static final int ADDRESS_MIN_LENGTH = 5;
    public static final int ADDRESS_MAX_LENGTH = 100;

    public static final int PLACE_NAME_MIN_LENGTH = 1;
    public static final int PLACE_NAME_MAX_LENGTH = 100;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String placeName;

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "road_address_name")
    private String roadAddressName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Builder
    public Spot(Long id, String placeName, String addressName, String roadAddressName, String phone, double latitude, double longitude) {
        this.id = id;
        this.placeName = placeName;
        this.addressName = addressName;
        this.roadAddressName = roadAddressName;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null ;
    }

    public String getPlaceName() {
        return placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getRoadAddressName() {
        return roadAddressName;
    }

    public String getPhone() {
        return phone;
    }
}
