package org.ticketing.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.ticketing.user.domain.entity.User;
import org.ticketing.user.infrastructure.repository.JpaUserRepository;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceApplicationTests {

	@Autowired
	private JpaUserRepository jpaUserRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void user_저장시_BaseEntity의_생성시간이_자동으로_저장된다() {
		// given
		User user = User.createGeneral(
			"test@example.com",
			"테스트유저",
			"010-1234-5678"
		);

		// when
		User savedUser = jpaUserRepository.saveAndFlush(user);

		// then
		assertThat(savedUser.getCreatedAt()).isNotNull();
	}

	@Test
	void user_수정시_BaseEntity의_수정시간이_자동으로_저장된다() {
		// given
		User user = User.createClubAdmin(
			"admin@example.com",
			"구단관리자",
			"010-1234-5678"
		);

		User savedUser = jpaUserRepository.saveAndFlush(user);

		// when
		savedUser.approve();
		User modifiedUser = jpaUserRepository.saveAndFlush(savedUser);

		// then
		assertThat(modifiedUser.getModifiedAt()).isNotNull();
	}
}
