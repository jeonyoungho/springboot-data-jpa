package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.*;

@EnableJpaAuditing // (modifyOnCreate = false) // update할땐 null로 넣고 싶다면 옵션을 주면 된다. 하지만 권장하는 방법은 아니다.
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		// 실제에는 SpringSecurityText에서 Session정보를 가져와서 걔의 id를 꺼내써야한다
		return () -> of(UUID.randomUUID().toString()); // 인터페이스에서 메서드 하나면 람다로 바꿀 수 있음

//		return new AuditorAware<String>() {
//			@Override
//			public Optional<String> getCurrentAuditor() {
//				return of(UUID.randomUUID().toString());
//			}
//		};
	}
}
