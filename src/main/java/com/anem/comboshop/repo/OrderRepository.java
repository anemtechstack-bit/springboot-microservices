/*
 * package com.anem.comboshop.repo;
 * 
 * import com.anem.comboshop.domain.CustomerOrder; import
 * org.springframework.data.jpa.repository.JpaRepository; import
 * org.springframework.data.jpa.repository.Query; import
 * org.springframework.data.repository.query.Param;
 * 
 * import java.util.List; import java.util.Optional;
 * 
 * public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
 * 
 * List<CustomerOrder> findByUsernameOrderByCreatedAtDesc(String username);
 * 
 * @Query(""" select o from CustomerOrder o left join fetch o.items i left join
 * fetch i.product where o.id = :id and o.username = :username """)
 * Optional<CustomerOrder> findByIdAndUsernameWithItems(@Param("id") Long id,
 * 
 * @Param("username") String username); }
 */

package com.anem.comboshop.repo;

import com.anem.comboshop.domain.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByUsernameOrderByCreatedAtDesc(String username);

    @Query("""
        select distinct o from CustomerOrder o
        left join fetch o.items i
        left join fetch i.product
        where o.username = :username
        order by o.createdAt desc
    """)
    List<CustomerOrder> findByUsernameWithItems(@Param("username") String username);

    @Query("""
        select distinct o from CustomerOrder o
        left join fetch o.items i
        left join fetch i.product
        where o.id = :id and o.username = :username
    """)
    Optional<CustomerOrder> findByIdAndUsernameWithItems(@Param("id") Long id,
                                                         @Param("username") String username);
}