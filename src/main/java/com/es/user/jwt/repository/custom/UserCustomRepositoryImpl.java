package com.es.user.jwt.repository.custom;

import com.es.user.jwt.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserCustomRepositoryImpl {

    @Autowired
    private UserCustomRepository userCustomRepository;

    public Page<User> findByCondition(User userParam, Pageable pageable) {
        return userCustomRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNoneBlank(userParam.getName())) {
                //liked的查询条件
                predicates.add(cb.like(root.get("name"), "%" + userParam.getName() + "%"));
            }
            if (Objects.nonNull(userParam.getAddress())) {
                //greaterThan大于等于查询条件
                predicates.add(cb.greaterThan(root.get("address"), userParam.getAddress()));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageable);
    }


    public List<User> findBetweenAge(int start, int end) {
        Specification between = SpecificationFactory.isBetween("age", start, end);
        List<User> all = userCustomRepository.findAll(between);
        return all;
    }

    public List<User> findGreaterThanAge(int min) {
        Specification age = SpecificationFactory.greaterThan("age", min);
        List<User> all = userCustomRepository.findAll(age);
        return all;
    }
}

