package com.cocodan.triplan.post.schedule.repository;

import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.vo.SchedulePostSortingRule;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.spot.domain.vo.City;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.cocodan.triplan.post.schedule.domain.QSchedulePost.schedulePost;
import static com.cocodan.triplan.schedule.domain.QSchedule.schedule;
import static com.cocodan.triplan.schedule.domain.QScheduleTheme.scheduleTheme;

public class CustomSchedulePostRepositoryImpl implements CustomSchedulePostRepository {

    private final JPAQueryFactory queryFactory;

    public CustomSchedulePostRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<SchedulePost> search(String search, City city, Theme theme, SchedulePostSortingRule sortRule) {
        JPAQuery<SchedulePost> query = queryFactory
                .select(schedulePost)
                .from(schedulePost);

        if (!theme.equals(Theme.ALL)) {
            query.join(schedule.scheduleThemes, scheduleTheme)
                    .where(isEqualToTheme(theme));
        }

        query.where(
                isEqualToCity(city),
                isContainedInTitleOrContent(search)
        );

        sortRule.sort(query);

        return query.fetch();
    }

    private BooleanExpression isEqualToTheme(Theme theme) {
        return theme == Theme.ALL ? null : scheduleTheme.theme.eq(theme);
    }

    private Predicate isEqualToCity(City city) {
        return city == City.ALL ? null : schedulePost.city.eq(city);
    }

    private BooleanExpression isContainedInTitleOrContent(String search) {
        return isContainedInContent(search)
                .or(isContainedInTitle(search));
    }

    private BooleanExpression isContainedInContent(String search) {
        return schedulePost.content.contains(search);
    }

    private BooleanExpression isContainedInTitle(String search) {
        return schedulePost.title.contains(search);
    }
}
