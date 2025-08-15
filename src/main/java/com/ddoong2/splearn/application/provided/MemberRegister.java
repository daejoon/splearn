package com.ddoong2.splearn.application.provided;

import com.ddoong2.splearn.domain.Member;
import com.ddoong2.splearn.domain.MemberRegisterRequest;

public interface MemberRegister {
    /**
     * 주어진 회원 등록 요청 정보를 기반으로 회원을 등록합니다.
     *
     * @param registerRequest 회원 등록 요청 정보를 포함하는 객체
     * @return 등록된 회원 객체
     */
    Member register(MemberRegisterRequest registerRequest);
}
