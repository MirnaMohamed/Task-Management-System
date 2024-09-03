package banquemisr.challenge05.taskmanagementservice.filter;

import banquemisr.challenge05.taskmanagementservice.model.Task;
import jakarta.persistence.criteria.*;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {
    public static Specification<Task> columnEqual(List<TaskSearchDto> filterDTOList)
    {
        return new Specification<>()
        {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(@NonNull Root<Task> root, CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder)
            {
                List<Predicate> predicates = new ArrayList<>();
                filterDTOList.forEach(filter ->
                {
                    //check if the column matches the value
                    Predicate predicate = criteriaBuilder.equal(root.get(filter.getColumnName()),filter.getColumnValue());
                    predicates.add(predicate);
                });

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
