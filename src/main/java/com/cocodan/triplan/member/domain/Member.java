package com.cocodan.triplan.member.domain;

import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class Member extends BaseEntity {
    private static final int BASIC_AGE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "passwd")
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth", nullable = false)
    private String birth;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_image", nullable = true)
    private String profileImage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public Member(String email, String name, String birth, GenderType gender, String nickname, String profileImage, String passwd, Group group) {
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.password = passwd;
        this.group = group;
    }

    @Builder
    private Member(Long id, String email, String name, String birth, GenderType gender, String nickname, String profileImage) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public void changeValues(String name, String nickname, String profileImage) {
        this.name = name.isBlank() ? this.name : name;
        this.nickname = nickname.isBlank() ? this.nickname : nickname;
        this.profileImage = profileImage.isBlank() ? this.profileImage : profileImage;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
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

    public Group getGroup() {
        return group;
    }

    public int getAge() {
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);

        int birthYear = getBirthYear(birth);

        return currentYear - birthYear + BASIC_AGE;
    }

    private int getBirthYear(String birth) {
        return Integer.parseInt(birth.split("-")[0]);
    }
}
