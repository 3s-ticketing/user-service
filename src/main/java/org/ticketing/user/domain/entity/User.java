package org.ticketing.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ticketing.common.domain.BaseEntity;
import org.ticketing.user.domain.enums.UserRole;
import org.ticketing.user.domain.enums.UserStatus;

@Entity
@Table(name = "p_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private UserStatus status;

    @Column(name = "slack_user_id", length = 50)
    private String slackUserId;

    public static User createGeneral(String email, String name, String phone) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.phone = phone;
        user.role = UserRole.GENERAL;
        user.status = UserStatus.APPROVED; // 일반 회원은 즉시 승인 상태로 생성
        return user;
    }

    public static User createClubAdmin(String email, String name, String phone) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.phone = phone;
        user.role = UserRole.CLUB_ADMIN;
        user.status = UserStatus.PENDING; // 구단 관리자는 승인 대기 상태로 생성
        return user;
    }

    public void update(String name, String phone) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (phone != null && !phone.isBlank()) {
            this.phone = phone;
        }
    }

    public void updateSlackUserId(String slackUserId) {
        this.slackUserId = slackUserId;
    }

    public void approve() {
        this.status = UserStatus.APPROVED;
    }

    public void reject() {
        this.status = UserStatus.REJECTED;
    }

    public void delete() {
        if (this.status == UserStatus.DELETED) {
            return;
        }

        this.status = UserStatus.DELETED;
        super.delete(null);
    }

    public boolean isActive() {
        return this.status == UserStatus.APPROVED && this.deletedAt == null;
    }
}
