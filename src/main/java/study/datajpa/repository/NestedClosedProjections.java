package study.datajpa.repository;

public interface NestedClosedProjections {

    String getUsername(); // 첫번째는 정확하게 최적화 해서 불러옴
    TeamInfo getTeam(); // 두번째부턴 최적화가 안된다 (전체 테이블 필드 다 불러옴)

    interface TeamInfo {
        String getName();
    }
}
