package com.kla.lms.klamp.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class GenericJoinSpecification<U, T> implements Serializable, Specification<T> {
	private static final long serialVersionUID = 1L;

	private List<SearchCriteria> criteriaList;
	private String parentName;

	public GenericJoinSpecification() {
		this.criteriaList = new ArrayList<>();
	}
	
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public void add(SearchCriteria criteria) {
		criteriaList.add(criteria);
	}

    public Predicate pathPredicate(Path<U> joinedEntity, CriteriaQuery<?> query, CriteriaBuilder builder, String key, Object value) {
        return builder.equal(joinedEntity.get(key), value);
    }
	
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Join<U,T> join = root.join(parentName, JoinType.INNER);
		
		// create a new predicate list
		List<Predicate> predicates = new ArrayList<>();

		// add add criteria to predicates
		for (SearchCriteria criteria : criteriaList) {

			if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {

				predicates.add(builder.greaterThan(join.get(criteria.getKey()), criteria.getValue().toString()));

			} else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {

				predicates.add(builder.lessThan(join.get(criteria.getKey()), criteria.getValue().toString()));

			} else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {

				predicates
						.add(builder.greaterThanOrEqualTo(join.get(criteria.getKey()), criteria.getValue().toString()));

			} else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {

				predicates.add(builder.lessThanOrEqualTo(join.get(criteria.getKey()), criteria.getValue().toString()));

			} else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {

				predicates.add(builder.notEqual(join.get(criteria.getKey()), criteria.getValue()));

			} else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {

				predicates.add(builder.equal(join.get(criteria.getKey()), criteria.getValue()));

			} else if (criteria.getOperation().equals(SearchOperation.MATCH)) {

				predicates.add(builder.like(builder.lower(join.get(criteria.getKey())),
						"%" + criteria.getValue().toString().toLowerCase() + "%"));

			} else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {

				predicates.add(builder.like(builder.lower(join.get(criteria.getKey())),
						criteria.getValue().toString().toLowerCase() + "%"));
				
			}
		}
		return builder.and(predicates.toArray(new Predicate[0]));
	}
}