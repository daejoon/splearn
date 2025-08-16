package com.ddoong2.splearn.application.member.provided;

import com.ddoong2.splearn.domain.member.Member;

/**
 * 회원을 조회한다
 */
public interface MemberFinder {
    Member find(Long memberId);
}
