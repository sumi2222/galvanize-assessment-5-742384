package com.galvanize.badgearamareceptiondesk.repository;

import com.galvanize.badgearamareceptiondesk.enums.VisitorStatus;
import com.galvanize.badgearamareceptiondesk.entity.Visit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends CrudRepository<Visit, Long> {
   // List<Visit> findAllByPhoneNumberAndOrderByRegisterDateDesc(Long phoneNumber, Date date);

    void deleteByPhoneNumber(Long phoneNumber);
    Optional<Visit> findFirstByPhoneNumberAndStatusOrderByRegisterDateDesc(Long phoneNumber, VisitorStatus status);
    List<Visit> findAllByPhoneNumberOrderByRegisterDateDesc(Long phoneNumber);
    List<Visit> findAllByStatus(VisitorStatus status);
    List<Visit> findAllByStatusOrStatus(VisitorStatus status1, VisitorStatus status2);
    Optional<Visit> findFirstByPhoneNumberOrderByRegisterDateDesc(Long phoneNumber);
}
