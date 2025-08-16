package com.ddoong2.splearn.application.provided;

import com.ddoong2.splearn.domain.Member;

/**
 * 회원을 조회한다
 */
public interface MemberFinder {
    Member find(Long memberId);
}
