package hello.DB.service;


import hello.DB.domain.Member;
import hello.DB.repository.MemberRepositoryV1;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import java.sql.SQLException;
import static hello.DB.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.sql.SQLException;

//기본동작 트랜잭션이 없어서 문제발생
class MemberServiceV1Test {

    public static final String Member_A="memberA";
    public static final String Member_B="memberB";
    public static final String Member_EX="ex";
    private MemberRepositoryV1 memberRepository;
    private MemberServiceV1 memberService;

    @BeforeEach
    void before(){
        DriverManagerDataSource dataSource=new DriverManagerDataSource(URL,USERNAME, PASSWORD);
        memberRepository=new MemberRepositoryV1(dataSource);
        memberService =new MemberServiceV1(memberRepository);
    }

    @AfterEach
    void after() throws SQLException{
        memberRepository.delete(Member_EX);
        memberRepository.delete(Member_A);
        memberRepository.delete(Member_B);
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException{
        //given
        Member memberA = new Member(Member_A, 10000);
        Member memberB = new Member(Member_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);
        //then
        Member findMemberA=memberRepository.findById(memberA.getMemberId());
        Member findMemberB=memberRepository.findById(memberB.getMemberId());
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
    }
    @Test
    @DisplayName("이체중 예외")
    void accountTransferEx() throws SQLException{
        //given
        Member memberA = new Member(Member_A, 10000);
        Member memberEx = new Member(Member_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);

        //when
        assertThatThrownBy(()->memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);
        //then
        Member findMemberA=memberRepository.findById(memberA.getMemberId());
        Member findMemberB=memberRepository.findById(memberEx.getMemberId());
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
    }
}