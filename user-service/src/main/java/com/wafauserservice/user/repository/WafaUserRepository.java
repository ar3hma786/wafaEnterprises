package com.wafauserservice.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wafauserservice.user.domain.ROLE;
import com.wafauserservice.user.model.WafaUser;

public interface WafaUserRepository extends JpaRepository<WafaUser, Long> {

    WafaUser findByUsername(String username);

    @Query("DELETE FROM WafaUser u WHERE u.cardNumber = :cardNumber AND u.role != :admin AND u.role != :superAdmin")
    void deleteByCardNumber(@Param("cardNumber") String cardNumber, @Param("admin") ROLE admin, @Param("superAdmin") ROLE superAdmin);

    WafaUser findByCardNumber(String cardNumber);

    @Query("SELECT u FROM WafaUser u WHERE u.role = :role AND " +
           "(u.cardNumber = :cardNumber OR u.city = :city OR u.careOff1 = :careOff1 OR u.careOff2 = :careOff2 OR STR(u.phoneNo) LIKE %:phoneNo%)")
    WafaUser searchByQuery(@Param("role") ROLE role,
                           @Param("cardNumber") String cardNumber,
                           @Param("city") String city,
                           @Param("careOff1") String careOff1,
                           @Param("careOff2") String careOff2,
                           @Param("phoneNo") Long phoneNo);

    @Query("SELECT u FROM WafaUser u WHERE u.username = :username AND u.role = :role")
    WafaUser findByUsernameAndRole(@Param("username") String username, @Param("role") ROLE role);

    @Query("SELECT u FROM WafaUser u WHERE " +
           "(u.username LIKE %:query% OR u.cardNumber LIKE %:query% OR u.city LIKE %:query% OR " +
           "u.careOff1 LIKE %:query% OR u.careOff2 LIKE %:query% OR STR(u.phoneNo) LIKE %:query%) AND " +
           "(u.role = :userRole OR u.role = :adminRole)")
    List<WafaUser> searchAllByQuery(@Param("query") String query,
                                 @Param("userRole") ROLE userRole,
                                 @Param("adminRole") ROLE adminRole);
}
