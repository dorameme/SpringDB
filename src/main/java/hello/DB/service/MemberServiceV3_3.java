package hello.DB.service;

import hello.DB.domain.Member;
import hello.DB.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
//트랜잭션 aop적용
@Slf4j
public class MemberServiceV3_3 {
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_3(MemberRepositoryV3 memberRepository){
        this.memberRepository = memberRepository;
    }

    @Transactional // 이 로직이 수행될때 트랜잭션 AOP가 적용됨
    public void accountTransfer(String fromId,String toId,int money) throws SQLException {
                bizLogic(fromId, toId, money);//비즈니스 로직에 집중할 수 있음 언체크예외가 발생하면 롤백

    }
    private void bizLogic(String fromId,String toId,int money) throws SQLException{
        Member fromMember =memberRepository.findById(fromId);
        Member toMember=memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney()-money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney()+money);
    }
    private void validation(Member toMember){
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외발생");
        }
    }


}
