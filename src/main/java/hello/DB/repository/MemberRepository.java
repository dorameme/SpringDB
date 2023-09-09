package hello.DB.repository;

import hello.DB.domain.Member;

public interface MemberRepository {
    Member save(Member memeber);
    Member findById(String memberId);
    void update(String memberId,int money);
    void delete(String memberId);
}
