package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); // 파라미터가 길어지면 답이 없음 2개 이상 넘어가면 NameQuery로 해결하는게 좋음

    List<Member> findHeloBy();

    List<Member> findTop3HelloBy();

    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age") // 이름이 없는 NamedQuery길래 애플리케이션 로딩시점에 파싱 후 문법 오류가 발생하게 됨
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); // 컬렉션
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalByUsername(String username); // 단건 Optional

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
//    Slice<Member> findByAge(int age, Pageable pageable);
//    List<Member> findByAge(int age, Pageable pageable);

//    @Query(value = "select m from Member m) // 이런식으로 JPQL의 핵심적인 부분만 작성하면 페이징이나 where문 같은 짜잘한 것들은 알아서 다 해준다.
//    Page<Member> findByAge(int age, Pageable pageable); // sorting조건이 너무 복잡하면 Pageable로 해결이 안된다. JPQL에 직접 작성해줘야 한다.
//    List<Member> findTop3ByAge(int age); // 단순하게 앞에서 3건만 조회하고 싶다하면 이렇게 해줘도 된다.

    // @Modifying 어노테이션이 있어야 JPQL의 executeUpdate() 메소드를 호출한다.
    // 벌크 연산은 DB에 직접 쿼리를 날리기 때문에 1차 캐시에 있는 데이터를 먼저 flush()로 DB에 보내고 벌크 연산을 수행한다.
    // 벌크 연산은 영속성 컨텍스트를 무시하고 DB에 바로 쿼리를 날리기 때문에 영속성 컨텍스트 입장에서 데이터 변경을 알 수 없다.
    // 그래서 벌크 연산 이후에 flush()를 통해 1차 캐시를 날려야 데이터 정합성을 보장할 수 있다.
    // 그걸 자동으로 해주는게 clearAutomatically = true 옵션이다.
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

//    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    // select for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);
}
