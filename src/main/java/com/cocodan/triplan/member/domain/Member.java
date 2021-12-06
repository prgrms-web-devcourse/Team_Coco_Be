package com.cocodan.triplan.member.domain;

import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "birth", nullable = false)
    private String birth;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_image", nullable = true)
    private String profileImage;

    @Builder
    public Member(String email, String name, String phoneNumber, String birth, GenderType gender, String nickname, String profileImage) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.gender = gender;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public void changeValues(String email, String name, String phoneNumber, String birth, String gender, String nickname, String profileImage) {
        this.email = email.isBlank() ? this.email : email;
        this.name = name.isBlank() ? this.name : name;
        this.phoneNumber = phoneNumber.isBlank() ? this.phoneNumber : phoneNumber;
        this.birth = birth.isBlank() ? this.birth : birth;
        this.gender = gender.isBlank() ? this.gender : GenderType.of(gender);
        this.nickname = nickname.isBlank() ? this.nickname : nickname;
        this.profileImage = profileImage.isBlank() ? this.profileImage : profileImage;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBirth() {
        return birth;
    }

    public GenderType getGender() {
        return gender;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }
}