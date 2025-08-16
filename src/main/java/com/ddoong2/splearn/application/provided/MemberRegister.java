package com.ddoong2.splearn.application.provided;

import com.ddoong2.splearn.domain.Member;
import com.ddoong2.splearn.domain.MemberRegisterRequest;
import jakarta.validation.Valid;

/**
 * 회원의 등록과 관련된 기능을 제공한다.
 */
public interface MemberRegister {
    Member register(@Valid MemberRegisterRequest registerRequest);

    Member activate(Long memberId);
}
