package com.cocodan.triplan.member.repository;

import com.cocodan.triplan.member.domain.Group;
import com.cocodan.triplan.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
