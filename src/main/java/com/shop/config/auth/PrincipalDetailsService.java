package com.shop.config.auth;


import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service //Ioc
@Slf4j
public class PrincipalDetailsService implements UserDetailsService{

  private final MemberRepository memberRepository;

  //1.패스워드는 알아서 체킹하니깐 신경쓸 필요 없다
  //2.리턴이 잘되면 자동으로 User세션을 만든다.

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member memberEntity=memberRepository.findByEmail(email);
    if(memberEntity==null) {
      throw new UsernameNotFoundException(email);
    }
   log.info("**** 로그인 성공  ");
  return new PrincipalDetails(memberEntity);
  }






}
