package hello.DB.repository;

import hello.DB.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository=new MemberRepositoryV0();
    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("member2", 10000);
        repository.save(member);
        //findbyid
        Member findMember=repository.findById(member.getMemberId());
        log.info("findMEmber={}",findMember);
        Assertions.assertThat(findMember).isEqualTo(member);
    }

}