package hello.DB.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.DB.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.DB.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;
    @BeforeEach
    void beforeEach() throws Exception {
//기본 DriverManager - 항상 새로운 커넥션 획득 //
// DriverManagerDataSource dataSource =
// new DriverManagerDataSource(URL, USERNAME, PASSWORD);
//커넥션 풀링: HikariProxyConnection -> JdbcConnection
HikariDataSource dataSource = new HikariDataSource();
dataSource.setJdbcUrl(URL);
dataSource.setUsername(USERNAME);
dataSource.setPassword(PASSWORD);
        repository = new MemberRepositoryV1(dataSource);
    }
    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("memberA", 10000);
        repository.save(member);
        //findbyid
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(member);
        //update
        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        //delete
        repository.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

    }
}
